/**
 * @file monitor.c
 * @brief Drone simulation monitoring and collision detection system
 * 
 * This file implements the monitoring subsystem for the drone simulation,
 * consisting of three main thread functions:
 * 1. Collision detection and tracking
 * 2. Report generation and file output
 * 3. Status monitoring and logging
 * 
 * The monitoring system uses pthread synchronization to safely access
 * shared memory and coordinate between multiple monitoring threads.
 * 
 * Key Features:
 * - Real-time collision detection using 3D spatial matrices
 * - Automatic report generation with timestamped collision events
 * - Continuous status monitoring with periodic updates
 * - Thread-safe access to shared drone position data
 * 
 */

/*============================================================================*/
/*                              INCLUDES                                     */
/*============================================================================*/

/* Standard library includes */
#include <stdio.h>      /* For printf, fprintf, fopen, etc. */
#include <stdlib.h>     /* For general utility functions */
#include <unistd.h>     /* For sleep, usleep functions */
#include <time.h>       /* For time tracking and timestamps */
#include <pthread.h>    /* For pthread threading functions */

/* Project-specific headers */
#include "main.h"       /* Contains global variables and main program definitions */
#include "monitor.h"    /* Contains function prototypes for this monitoring module */
#include "types.h"

/*============================================================================*/
/*                         EXTERNAL GLOBAL VARIABLES                         */
/*============================================================================*/

/* External global variables from main.c - shared across the entire program */
extern SharedMemory *shared_data;    /* Pointer to shared memory containing drone positions */
extern Drone drones[MAX_DRONES];     /* Array of drone structures with movement data */
extern DroneExecutionHistory droneHistory[MAX_DRONES];
extern const char DIRECTIONS[];
extern pthread_mutex_t wind_mutex;

/*============================================================================*/
/*                        STATIC DATA STRUCTURES                            */
/*============================================================================*/

/**
 * @brief 3D collision detection matrices
 * 
 * These matrices track drone positions and collisions in 3D space.
 * Static keyword means these variables are only accessible within this file.
 * 
 * These arrays are defined as `static` to move their storage from the **stack** to the
 * **data segment** (global memory area). Without the `static` keyword, declaring such
 * large arrays inside a function would place them on the stack, which has limited space,
 * typically a few megabytes. This led to a **Bus error** (invalid memory access) during
 * execution due to **stack overflow**.
 * 
 * Using `static` ensures the program allocates memory for these large matrices at
 * compile-time in a memory-safe region, avoiding runtime crashes.
 * 
 * #TODO: Maybe dynamically allocating these matrices instead of static allocation.
 *       These arrays consume a large amount of memory, especially with large values 
 *       for MAX_X, MAX_Y, MAX_Z, and MAX_DRONES. If the simulation needs to scale,
 *       dynamic allocation using `malloc` or `calloc` would be better
 */

/* 3D matrix marking positions where collisions have occurred (1 = collision, 0 = safe) */
static int collision_matrix[MAX_X][MAX_Y][MAX_Z] = {0};

/* 3D matrix counting how many drones are at each position */
static int position_counts[MAX_X][MAX_Y][MAX_Z] = {0};

/* 3D matrix storing which specific drones are at each position
 * Fourth dimension [MAX_DRONES] allows up to MAX_DRONES at same position */
static int position_map[MAX_X][MAX_Y][MAX_Z][MAX_DRONES] = {0};

/*============================================================================*/
/*                          FUNCTION IMPLEMENTATIONS                         */
/*============================================================================*/

/**
 * @brief Main collision detection thread function
 *
 * This thread continuously monitors all active drone positions and detects
 * when multiple drones occupy the same 3D coordinate. It uses a spatial
 * hashing approach with 3D matrices for efficient collision detection.
 *
 * @param arg Thread argument (unused, required by pthread interface)
 * @return NULL when thread terminates (standard pthread return)
 *
 * @note Thread Safety: Uses semaphores to safely read drone positions from
 *       shared memory and mutexes to safely update collision data structures.
 */
void *collision_monitor_thread(void *arg)
{
    /* Thread startup notification */
    printf("[Collision Thread] Started\n");

    /* Time step counter for collision event tracking */
    int time_step = 0;

    /* Main monitoring loop - continues until simulation stops */
    while (simulation_running)
    {
        /*--------------------------------------------------------------------*/
        /* RESET DETECTION MATRICES FOR NEW TIME STEP                        */
        /*--------------------------------------------------------------------*/

        /* Clear all matrices to start fresh collision detection for this time step */
        memset(collision_matrix, 0, sizeof(collision_matrix));   /* All positions marked as safe */
        memset(position_counts, 0, sizeof(position_counts));     /* All positions have 0 drones */

        /*--------------------------------------------------------------------*/
        /* READ ACTUAL WIND DATA WITH MUTEX                       			  */
        /*--------------------------------------------------------------------*/

        pthread_mutex_lock(&wind_mutex);
        char wind_dir = shared_data->wind.direction;
        int wind_vel = shared_data->wind.velocity;
        pthread_mutex_unlock(&wind_mutex);

        /*--------------------------------------------------------------------*/
        /* SAMPLE CURRENT POSITIONS FROM SHARED MEMORY                       */
        /*--------------------------------------------------------------------*/

        /* Read current position of each drone and populate detection matrices */
        for (int i = 0; i < drone_count; i++)
        {
            /* Skip drones that are no longer active */
            if (!shared_data->active[i])
                continue;

            /* Acquire semaphore to safely read drone position from shared memory */
            sem_wait(sem_positions[i]);

            /* Copy current drone position */
            int x = shared_data->positions[i].x;
            int y = shared_data->positions[i].y;
            int z = shared_data->positions[i].z;

            /* Release semaphore after reading */
            sem_post(sem_positions[i]);

            // Adjust offset based on wind velocity
            int pos_offset = 0;
            if (wind_vel >= SECOND_LEVEL_WIND)
                pos_offset = 2;
            else if (wind_vel >= FIRST_LEVEL_WIND)
                pos_offset = 1;

            if (pos_offset > 0) {
                switch (wind_dir) {
                    case 'N': y -= pos_offset; break; // North decreases Y
                    case 'S': y += pos_offset; break; // South increases Y
                    case 'E': x += pos_offset; break; // East increases X
                    case 'W': x -= pos_offset; break; // West decreases X
                    default: break; // Other cases ignore
                }
            }

            /* Store positions */
            if (time_step < MAX_TIME) {
                droneHistory[i].positions[time_step].x = x;
                droneHistory[i].positions[time_step].y = y;
                droneHistory[i].positions[time_step].z = z;
            }

            /* Validate if position is within simulation bounds */
            if (x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y && z >= 0 && z < MAX_Z)
            {
                /* Fetch count of drones currently in this cell */
                int count = position_counts[x][y][z];

                /* Prevent exceeding storage in 4D matrix */
                if (count < MAX_DRONES)
                {
                    /* Register drone index in this 3D cell */
                    position_map[x][y][z][count] = i;
                    /* Increment drone count at this cell */
                    position_counts[x][y][z]++;
                    /* Mark this cell as a collision if more than 1 drone */
                    if (position_counts[x][y][z] > 1)
                    {
                        collision_matrix[x][y][z] = 1;
                    }
                }
            }
        }

        /*--------------------------------------------------------------------*/
        /* COLLISION DETECTION AND EVENT LOGGING                             */
        /*--------------------------------------------------------------------*/

        /* Traverse the entire 3D grid to find and log collisions */
        for (int x = 0; x < MAX_X; x++)
        {
            for (int y = 0; y < MAX_Y; y++)
            {
                for (int z = 0; z < MAX_Z; z++)
                {
                    /* If collision recorded at this cell and multiple drones are present */
                    if (collision_matrix[x][y][z] && position_counts[x][y][z] > 1)
                    {
                        /* Acquire lock to safely write to shared collision buffer */
                        pthread_mutex_lock(&collision_mutex);

                        /* If buffer has space to log collision */
                        if (collision_count < COLLISION_BUFFER_SIZE)
                        {
                            /* Get reference to next slot in buffer */
                            CollisionEvent *event = &collision_buffer[collision_count];

                            /* Fill collision data */
                            event->time_step = time_step;
                            event->x = x;
                            event->y = y;
                            event->z = z;
                            event->num_drones = position_counts[x][y][z];
                            event->timestamp = time(NULL);

                            /* Save IDs of involved drones */
                            for (int d = 0; d < event->num_drones; d++)
                            {
                                event->drone_ids[d] = position_map[x][y][z][d];
                            }

                            /* Update shared tracking info */
                            collision_count++;
                            new_collision_flag = 1;

                            /* Log collision in console */
                            printf("[COLLISION DETECTED] Time %d at (%d,%d,%d) - %d drones involved\n",
                                   time_step, x, y, z, event->num_drones);
                        }

                        /* Notify report thread of new collision */
                        pthread_cond_signal(&collision_detected);

                        /* Release mutex lock */
                        pthread_mutex_unlock(&collision_mutex);

                        /* Check if collision threshold reached */
                        if (collision_count >= MAX_COLLISIONS) {
                            /* Inform that simulation will be stopped */
                            printf("[Collision Thread] Max collisions reached. Stopping simulation.\n");

                            /* Disable simulation */
                            simulation_running = 0;

                            /* Terminate all active drone processes */
                            for (int i = 0; i < drone_count; i++) {
                                kill(pids[i], SIGTERM);
                            }

                            /* Wake any threads waiting on condition variable */
                            pthread_cond_signal(&collision_detected);

                            /* Exit this thread cleanly */
                            pthread_exit(NULL);
                        }
                    }
                }
            }
        }

        /* Proceed to next time step */
        time_step++;

        /* Sleep to match simulation pace and reduce CPU usage */
        usleep(100000);
    }

    /* Shutdown message */
    printf("[Collision Thread] Stopped\n");
    return NULL;
}

/**
 * @brief Generates detailed collision reports to file
 * 
 * This thread waits for collision events and writes detailed reports to
 * a file. It creates a comprehensive log including timestamps, drone IDs,
 * and collision coordinates. The thread uses condition variables to
 * efficiently wait for new collision events.
 * 
 * @param arg Thread argument (unused, required by pthread interface)
 * @return NULL when thread terminates, or NULL on file error
 * 
 * @note Report Format:
 *       - Header with simulation start time and drone count
 *       - Individual collision events with full details
 *       - Footer with summary statistics and end time
 */
void *report_generation_thread(void *arg) 
{   
    /* Thread startup notification */
    printf("[Report Thread] Started\n");

    /*------------------------------------------------------------------------*/
    /* OPEN REPORT FILE FOR WRITING                                          */
    /*------------------------------------------------------------------------*/
    
    /* Create or overwrite the report file */
    FILE *report_file = fopen(REPORT_FILE, "w");
    if (!report_file) 
    {
        /* Print error and exit thread if file cannot be created */
        perror("Failed to create report file");
        return NULL;
    }

    /*------------------------------------------------------------------------*/
    /* WRITE REPORT HEADER                                                   */
    /*------------------------------------------------------------------------*/
    
    /* Create report header with simulation metadata */
    fprintf(report_file, "=== DRONE SIMULATION REPORT ===\n");
    
    /* Record simulation start time with human-readable format */
    time_t start_time = time(NULL);
    fprintf(report_file, "Simulation started at: %s\n", ctime(&start_time));
    
    /* Record total number of drones in simulation */
    fprintf(report_file, "Number of drones: %d\n\n", drone_count);
    
    /* Force immediate write to file (don't wait for buffer to fill) */
    fflush(report_file);

    /*------------------------------------------------------------------------*/
    /* MAIN REPORT GENERATION LOOP                                           */
    /*------------------------------------------------------------------------*/
    
    /* Wait for collision events and write detailed reports */
    while (simulation_running) 
    {
        /* Acquire exclusive access to collision data structures */
        pthread_mutex_lock(&collision_mutex);

        /* Wait for collision notification or simulation end
         * This is an efficient wait - thread sleeps until signaled */
        while (!new_collision_flag && simulation_running) 
        {
           pthread_cond_wait(&collision_detected, &collision_mutex);
        }

        /* Check if simulation ended while waiting */
        if (!simulation_running) 
        {
            pthread_mutex_unlock(&collision_mutex);
            break;  /* Exit main loop to write report footer */
        }

        /*--------------------------------------------------------------------*/
        /* PROCESS NEW COLLISION EVENT                                        */
        /*--------------------------------------------------------------------*/
        
        /* Write detailed information about the most recent collision */
        if (new_collision_flag && collision_count > 0) 
        {
            /* Get pointer to most recent collision event */
            CollisionEvent *event = &collision_buffer[collision_count - 1];

            /* Write collision event header with sequential numbering */
            fprintf(report_file, "COLLISION EVENT #%d\n", collision_count);
            
            /* Write collision timing and location information */
            fprintf(report_file, "Time Step: %d, Position: (%d,%d,%d)\n",
                    event->time_step, event->x, event->y, event->z);
            
            /* Write human-readable timestamp */
            fprintf(report_file, "Timestamp: %s", ctime(&event->timestamp));
            
            /* Write list of involved drones */
            fprintf(report_file, "Drones involved (%d): ", event->num_drones);

            /* Format drone ID list with proper comma separation */
            for (int i = 0; i < event->num_drones; i++) 
            {
                fprintf(report_file, "%s%d", (i > 0) ? ", " : "", event->drone_ids[i]);
            }
            
            /* Add blank line for readability between events */
            fprintf(report_file, "\n\n");
            
            /* Force immediate write to file */
            fflush(report_file);

            /* Reset new collision flag - this event has been processed */
            new_collision_flag = 0;
        }

        /* Release exclusive access to collision data structures */
        pthread_mutex_unlock(&collision_mutex);
    }
    
    /*------------------------------------------------------------------------*/
    /* WRITE DRONE POSITION HISTORY                                          */
    /*------------------------------------------------------------------------*/

    fprintf(report_file, "\n=== DRONE EXECUTION HISTORY ===\n");
    for (int i = 0; i < drone_count; i++) 
    {
        fprintf(report_file, "Drone %d:\n", i);
        for (int t = 0; t < MAX_TIME; t++) 
        {
            Position pos = droneHistory[i].positions[t];

            // Só imprime posições válidas (podes ajustar conforme preferires)
            if (pos.x != 0 || pos.y != 0 || pos.z != 0 || t == 0) 
            {
                fprintf(report_file, "  Time %d: (%d, %d, %d)\n", t, pos.x, pos.y, pos.z);
            }
        }
        fprintf(report_file, "\n");
    }
    
    /*------------------------------------------------------------------------*/
    /* VALIDATE FIGURE BASED ON COLLISIONS                                   */
    /*------------------------------------------------------------------------*/

    if (collision_count == 0) {
        fprintf(report_file, "FIGURE VALIDATION: PASSED (no collisions)\n\n");
    } else {
        fprintf(report_file, "FIGURE VALIDATION: FAILED (%d collision(s) detected)\n\n", collision_count);
    }

    /*------------------------------------------------------------------------*/
    /* WRITE REPORT FOOTER                                                   */
    /*------------------------------------------------------------------------*/
    
    /* Add simulation summary and statistics */
    fprintf(report_file, "=== SIMULATION SUMMARY ===\n");
    fprintf(report_file, "Total collisions detected: %d\n", collision_count);
    
    /* Record simulation end time */
    time_t end_time = time(NULL);
    fprintf(report_file, "Simulation ended at: %s\n", ctime(&end_time));

    /* Close report file to ensure all data is written */
    fclose(report_file);

    /* Thread completion notification with report file location */
    printf("[Report Thread] Generated final report: %s\n", REPORT_FILE);
    return NULL;  /* Standard pthread return value */
}

/**
 * @brief Provides periodic status updates during simulation
 * 
 * This thread runs continuously during the simulation and provides
 * periodic status updates to the console. It helps users understand
 * that the simulation is running and provides basic activity information.
 * 
 * @param arg Thread argument (unused, required by pthread interface)
 * @return NULL when thread terminates (never reached due to pthread_exit)
 * 
 * @note Current Implementation:
 *       - Prints status every 1 second
 *       - Shows total number of active drones
 *       - Can be extended to show more detailed drone status information
 */
void *status_monitor_thread(void *arg) 
{
    /*------------------------------------------------------------------------*/
    /* MAIN STATUS MONITORING LOOP                                           */
    /*------------------------------------------------------------------------*/
    
    /* Continue until simulation stops */
    while (simulation_running) 
    {
        /* Print current simulation status to console
         * Shows that simulation is active and provides drone count */
        printf("[Status Monitor] Simulation running. Active drones: %d\n", drone_count);

        /* Sleep for 1 second before next status update
         * This provides regular updates without overwhelming the console */
        sleep(1);  /* 1 second sleep (less frequent than collision monitoring) */
    }

    /* Terminate thread cleanly
     * pthread_exit is more explicit than return for thread termination */
    pthread_exit(NULL);
}

/**
 * @brief Simula e atualiza condições ambientais, como o vento
 * 
 * Esta thread é responsável por gerar vento aleatório em cada intervalo
 * de tempo. O vento afeta todos os drones da simulação. Os dados são
 * escritos na memória partilhada, onde os drones podem ler e adaptar
 * os seus movimentos.
 * 
 * @param arg Argumento da thread (não usado)
 * @return NULL
 */
void *environment_monitor_thread(void *arg)
{
    printf("[Environment Thread] Started\n");

    srand(time(NULL));

    while (simulation_running)
    {
        // Chooses a random direction
        char dir = DIRECTIONS[rand() % NUMBER_DIRECTIONS];

        // Chosses a random velocity between 0 and 31
        int velocity = rand() % 31;

        // Updates the wind data in the shared memory with mutex
        pthread_mutex_lock(&wind_mutex);
        shared_data->wind.direction = dir;
        shared_data->wind.velocity = velocity;
        pthread_mutex_unlock(&wind_mutex);

        printf("[Environment Thread] Wind updated: %c at %d\n", dir, velocity);

        usleep(500000); // 0.5 seconds between updates
    }

    printf("[Environment Thread] Stopped\n");
    pthread_exit(NULL);
}


