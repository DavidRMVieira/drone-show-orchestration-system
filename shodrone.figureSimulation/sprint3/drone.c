/**
 * @file drone.c
 * @brief Drone behavior and validation routines
 * 
 * This module handles:
 * - Loading drone movement paths from `.txt` files
 * - Validating those paths against simulation bounds
 * - Running drone behavior logic inside forked child processes
 * - Updating shared memory with synchronized access using semaphores
 * 
 * Design Rationale:
 * - Each drone runs independently via `fork()`
 * - Movement data is parsed from external files
 * - Synchronization is handled using named semaphores (one per drone)
 * 
 * Dependencies:
 * - `types.h`: For data structures like `Drone` and `Position`
 * - `main.h`: For shared memory, semaphores, and global control flags
 * - `drone.h`: For function declarations and usage by other modules
 */

#include <stdio.h>      // For file I/O operations
#include <stdlib.h>     // For dynamic memory and exit codes
#include <string.h>     // For string parsing
#include <unistd.h>     // For usleep()
#include <fcntl.h>      // For shm_open()
#include <sys/mman.h>   // For mmap()
#include <semaphore.h>  // For POSIX semaphores

#include "types.h"
#include "main.h"
#include "drone.h"

/**
 * @brief Loads a drone's movement routine from a text file
 * 
 * This function reads a `.txt` file containing lines of (x, y, z) coordinates
 * representing the drone's movement path. It performs full validation:
 * - Rejects if any line is out of bounds or malformed
 * - Rejects if the number of steps exceeds MAX_TIME
 * 
 * @param filepath Path to the drone movement file
 * @param drone Pointer to the Drone structure to populate
 * @return 0 on success, -1 on failure (invalid input or over limit)
 */
int load_drone_routine(const char *filepath, Drone *drone) {
    /* Show which file is being processed */
    printf("[Debug] Attempting to load drone from: %s\n", filepath);

    /* Open the file for reading */
    FILE *file = fopen(filepath, "r");
    if (!file) {
        perror("[Error] fopen");
        return -1;
    }

    /* Allocate memory for all potential steps */
    drone->movements = malloc(MAX_TIME * sizeof(Position));
    if (!drone->movements) {
        perror("[Error] malloc failed for movements");
        fclose(file);
        return -1;
    }

    /* Reset drone step counter */
    drone->duration = 0;

    /* Temporary buffer for line parsing */
    char line[256];
    int line_number = 0;

    /* Error flag for validation failures */
    int invalid_found = 0;

    /* Read line-by-line */
    while (fgets(line, sizeof(line), file)) {
        line_number++;

        /* Skip comments or whitespace lines */
        if (line[0] == '#' || line[0] == '\n' || line[0] == '\r' || line[0] == ' ')
            continue;

        /* Parse coordinates (accepts comma-separated or space-separated) */
        int x, y, z;
        int parsed = sscanf(line, "%d,%d,%d", &x, &y, &z);
        if (parsed < 3)
            parsed = sscanf(line, "%d %d %d", &x, &y, &z);

        /* Check bounds and step count limit */
        if (parsed == 3 && x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y && z >= 0 && z < MAX_Z) {
            if (drone->duration < MAX_TIME) {
                drone->movements[drone->duration++] = (Position){x, y, z};
            } else {
                /* Exceeded MAX_TIME limit */
                printf("[Warning] Too many steps in file (limit is %d). Aborting load.\n", MAX_TIME);
                invalid_found = 1;
                break;
            }
        } else {
            /* Invalid format or out-of-bounds */
            printf("[Warning] Invalid line %d: %s", line_number, line);
            invalid_found = 1;
        }
    }

    /* Cleanup: if errors occurred or file was empty, reject the drone */
    fclose(file);
    if (invalid_found || drone->duration == 0) {
        free(drone->movements);
        drone->movements = NULL;
        drone->duration = 0;
        return -1;
    }

    /* Successfully loaded and validated */
    return 0;
}

/**
 * @brief Prints a drone's basic info and first few movements
 * 
 * Useful for debugging after loading a drone file.
 * 
 * @param drone Pointer to the Drone structure
 */
void print_drone_info(const Drone *drone) {
    // Print the drone's ID, name, and number of movement steps
    printf("Drone %d (%s): %d movements\n", drone->id, drone->name, drone->duration);

    // Print the first 5 movements (or all, if fewer)
    for (int i = 0; i < drone->duration && i < 5; i++) {
        printf("  Step %d: (%d,%d,%d)\n", i,
               drone->movements[i].x,
               drone->movements[i].y,
               drone->movements[i].z);
    }

    // Inform user if there are more movements beyond what was printed
    if (drone->duration > 5) {
        printf("  ... and %d more steps\n", drone->duration - 5);
    }
}

/**
 * @brief Validates that all drone movements are within the simulation grid
 * 
 * @param drone Pointer to the Drone structure
 * @return 0 if all positions are valid, -1 if any are out of bounds
 */
int validate_drone_path(const Drone *drone) {
    // Iterate through each movement
    for (int i = 0; i < drone->duration; i++) {
        Position p = drone->movements[i];

        // Check that position is within valid grid bounds
        if (p.x < 0 || p.x >= MAX_X ||
            p.y < 0 || p.y >= MAX_Y ||
            p.z < 0 || p.z >= MAX_Z) {
            // If invalid, print an error message and return failure
            printf("[Validation Error] Step %d has invalid coordinates: (%d,%d,%d)\n",
                   i, p.x, p.y, p.z);
            return -1;
        }
    }

    // If all positions are valid, return success
    return 0;
}

/**
 * @brief Executes a drone's simulation routine in a child process
 * 
 * Updates shared memory in sync with other drones using semaphores.
 * 
 * @param index Index of this drone in global/shared arrays
 */
void drone_process(int index) {
    // === SHARED MEMORY SETUP ===

    // Attach to existing shared memory created by the parent
    shm_fd = shm_open("/drone_shm", O_RDWR, 0666);
    if (shm_fd == -1) {
        perror("[Drone] shm_open");
        exit(1);
    }

    // Map the shared memory into this process's address space
    shared_data = mmap(NULL, sizeof(SharedMemory),
                       PROT_READ | PROT_WRITE,
                       MAP_SHARED, shm_fd, 0);
    if (shared_data == MAP_FAILED) {
        perror("[Drone] mmap");
        exit(1);
    }

    // Confirm successful startup
    printf("[Drone %d] Started with shared_data at %p\n", index, shared_data);

    // === EXECUTE MOVEMENTS ===

    for (int t = 0; t < drones[index].duration && simulation_running; t++) {
        // Lock the semaphore for this drone to access its shared data
        sem_wait(sem_positions[index]);

        // Write current position into shared memory
        shared_data->positions[index] = drones[index].movements[t];

        // Mark the drone as active
        shared_data->active[index] = 1;

        // Unlock the semaphore to allow other processes to access
        sem_post(sem_positions[index]);

        // Sleep for 100ms to simulate time between steps
        usleep(100000);
    }

    // === MARK DRONE AS INACTIVE ===

    // Lock semaphore again to mark as inactive
    sem_wait(sem_positions[index]);
    shared_data->active[index] = 0;
    sem_post(sem_positions[index]);

    // Print debug message showing this drone finished
    printf("[Drone %d] Completed routine\n", index);

    // Exit the child process
    exit(0);
}