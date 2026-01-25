/**
 * @file main.c
 * @brief Entry point for the Enhanced Drone Simulation System
 * 
 * This file orchestrates the simulation lifecycle, including:
 * - Interactive figure selection and drone loading
 * - Shared memory and semaphore setup
 * - Forking of drone simulation processes
 * - Launching threads for monitoring and reporting
 * - Graceful termination via signal handling and cleanup
 * 
 * Design Highlights:
 * - Parent/child process model provides isolation and fault containment
 * - Shared memory offers fast inter-process communication of drone positions
 * - Named semaphores protect concurrent access to shared data
 * - Multi-threaded parent handles monitoring and reporting responsibilities
 */

#include "main.h"

/*============================================================================*/
/*                            GLOBAL VARIABLES                               */
/*============================================================================*/

// Drone data (loaded from selected figure)
Drone drones[MAX_DRONES];
int drone_count = 0;

// Shared memory and semaphore resources
SharedMemory *shared_data = NULL;
int shm_fd;
sem_t *sem_positions[MAX_DRONES];
pid_t pids[MAX_DRONES];  // Process IDs of drone children

// Monitoring/reporting threads
pthread_t collision_thread;
pthread_t report_thread;
pthread_t monitor_thread;
pthread_t env_thread;

// Thread synchronization
pthread_mutex_t collision_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t report_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t collision_detected = PTHREAD_COND_INITIALIZER;
pthread_mutex_t wind_mutex = PTHREAD_MUTEX_INITIALIZER;

// Collision event buffer
CollisionEvent collision_buffer[COLLISION_BUFFER_SIZE];
int collision_count = 0;
int new_collision_flag = 0;

// Simulation control flag (used for termination)
int simulation_running = 1;

// Report data storage
DroneExecutionHistory droneHistory[MAX_DRONES];

const char DIRECTIONS[] = { 'N', 'E', 'W', 'S' };

/*============================================================================*/
/*                          SIGNAL HANDLING                                  */
/*============================================================================*/

/**
 * @brief Signal handler for SIGINT and SIGTERM
 */
void handle_sigint(int sig) {
    (void)sig;  // Unused
    printf("\n[Main] Signal caught, shutting down simulation...\n");

    // Set simulation control flag to stop loops
    simulation_running = 0;

    // Wake threads waiting on condition variable
    pthread_cond_broadcast(&collision_detected);
}

/**
 * @brief Configures signal handlers for graceful shutdown
 */
void setup_signal_handlers(void) {
    struct sigaction sa;
    sa.sa_handler = handle_sigint;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;

    // Register handler for Ctrl+C and kill signals
    sigaction(SIGINT, &sa, NULL);
    sigaction(SIGTERM, &sa, NULL);
}

/*============================================================================*/
/*                          PROCESS MANAGEMENT                               */
/*============================================================================*/

/**
 * @brief Forks one child process per drone
 */
int fork_drones(void) {
    for (int i = 0; i < drone_count; i++) {
        // Fork the current process
        pid_t pid = fork();

        // Error handling
        if (pid < 0) {
            perror("[Main] fork failed");
            return -1;
        }

        // Child process: run drone routine
        else if (pid == 0) {
            drone_process(i);
            exit(0);  // Should never return
        }

        // Parent process: store PID for later
        else {
            pids[i] = pid;
        }
    }
    return 0;
}

/**
 * @brief Waits for all drone processes to complete
 */
void wait_for_drones(void) {
    for (int i = 0; i < drone_count; i++) {
        int status;
        waitpid(pids[i], &status, 0);  // Wait for process termination
        printf("[Main] Drone process %d (PID %d) terminated with status %d\n", i, pids[i], status);
    }
}

/*============================================================================*/
/*                             CLEANUP ROUTINE                               */
/*============================================================================*/

/**
 * @brief Performs cleanup of memory, semaphores, and thread primitives
 */
void cleanup_all(void) {
    printf("[Main] Cleaning up resources...\n");

    // Release semaphores and shared memory
    cleanup_semaphores(drone_count);
    cleanup_shared_memory();

    // Destroy mutexes and condition variable
    pthread_mutex_destroy(&collision_mutex);
    pthread_mutex_destroy(&report_mutex);
    pthread_cond_destroy(&collision_detected);

    printf("[Main] Cleanup complete.\n");
}

/*============================================================================*/
/*                             MAIN FUNCTION                                 */
/*============================================================================*/

/**
 * @brief Main control flow for simulation execution
 */
int main(void) {
    // === Banner ===
    printf("========================================\n");
    printf("   Drone Simulation System\n");
    printf("========================================\n");
    printf("Multi-process, multi-threaded 3D collision detection\n");
    printf("Features: Shared memory, semaphores, and threads real-time monitoring\n\n");

    // === Setup signal handling ===
    setup_signal_handlers();
    printf("[Main] Signal handlers configured\n");


    // === Phase 1: Get user input and load figure ===
    printf("\n=== Phase 1: Figure Selection ===\n");
    list_figures(FIGURE_FOLDER);  // Show available directories

    char figure[256];
    ask_user_for_figure(figure, sizeof(figure));  // Prompt user for figure name

    // Construct full path to selected figure directory
    char path[512];
    snprintf(path, sizeof(path), "%s/%s", FIGURE_FOLDER, figure);

    // Load drones from figure folder
    if (load_figure(path, drones, &drone_count) != 0 || drone_count == 0) {
        fprintf(stderr, "[Error] Could not load figure '%s' or no drones found\n", figure);
        printf("[System] No simulation report was generated due to a crash caused by drones' positions being out of bounds.\nFIGURE DID NOT PASS THE SIMULATION!\n");
        return 1;
    }

    // List loaded drones and their steps
    printf("[Main] Successfully loaded %d drones from figure '%s'\n", drone_count, figure);
    printf("\nLoaded Drones:\n");
    for (int i = 0; i < drone_count; i++) {
        printf("  %d. %s (%d movement steps)\n", i, drones[i].name, drones[i].duration);
    }

    // === Phase 2: IPC Setup ===
    printf("\n=== Phase 2: System Initialization ===\n");

    // Setup shared memory for drone state
    printf("[Main] Setting up shared memory...\n");
    if (setup_shared_memory() != 0) {
        fprintf(stderr, "[Error] Failed to initialize shared memory\n");
        return 1;
    }

    // Setup semaphores for drone synchronization
    printf("[Main] Initializing semaphores...\n");
    if (setup_semaphores(drone_count) != 0) {
        fprintf(stderr, "[Error] Failed to initialize semaphores\n");
        cleanup_shared_memory();
        return 1;
    }

    // === Phase 3: Fork drones ===
    printf("\n=== Phase 3: Forking Drone Processes ===\n");
    if (fork_drones() != 0) {
        fprintf(stderr, "[Error] Failed to fork drone processes\n");
        cleanup_all();
        return 1;
    }

    // === Phase 4: Start monitoring threads ===
    printf("\n=== Phase 4: Starting Monitoring Threads ===\n");

    // Start collision detection thread
    if (pthread_create(&collision_thread, NULL, collision_monitor_thread, NULL) != 0) {
        perror("[Main] Failed to create collision thread");
        simulation_running = 0;
        cleanup_all();
        return 1;
    }

    // Start environment thread
    if (pthread_create(&env_thread, NULL, environment_monitor_thread, NULL) != 0) {
        perror("[Main] Failed to create environment thread");
        simulation_running = 0;
        pthread_cancel(collision_thread);
        cleanup_all();
        return 1;
    }

    // Start report generation thread
    if (pthread_create(&report_thread, NULL, report_generation_thread, NULL) != 0) {
        perror("[Main] Failed to create report thread");
        simulation_running = 0;
        pthread_cancel(collision_thread);
        pthread_cancel(env_thread);
        cleanup_all();
        return 1;
    }

    // Start status monitor thread
    if (pthread_create(&monitor_thread, NULL, status_monitor_thread, NULL) != 0) {
        perror("[Main] Failed to create monitor thread");
        simulation_running = 0;
        pthread_cancel(collision_thread);
        pthread_cancel(report_thread);
        cleanup_all();
        return 1;
    }

    printf("[Main] Simulation running. Press Ctrl+C to stop.\n");

    // === Wait for all drones to finish ===
    wait_for_drones();

    // Signal threads to shut down gracefully
    simulation_running = 0;
    pthread_cond_broadcast(&collision_detected);

    // Join all threads
    pthread_join(collision_thread, NULL);
    pthread_join(env_thread, NULL);
    pthread_join(report_thread, NULL);
    pthread_join(monitor_thread, NULL);

    // Final cleanup of all resources
    cleanup_all();

    printf("[Main] Simulation terminated successfully.\n");
    return 0;
}
