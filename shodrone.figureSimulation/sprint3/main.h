/**
 * @file main.h
 * @brief Master header for the Enhanced Drone Simulation System
 * 
 * This file declares shared constants, global variables, and function
 * prototypes used across the main simulation module and its subcomponents.
 * 
 * Responsibilities:
 * - Define common interfaces for communication between modules
 * - Declare external global state shared between threads and processes
 * - Centralize system-wide constants and configuration
 * 
 * Included by:
 * - main.c, drone.c, monitor.c, shared_resources.c, utils.c
 */

#ifndef MAIN_H
#define MAIN_H

/*============================================================================*/
/*                            SYSTEM INCLUDES                                */
/*============================================================================*/

// Standard I/O and memory operations
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

// Signal handling and file descriptors
#include <signal.h>
#include <fcntl.h>
#include <errno.h>

// Threads and synchronization primitives
#include <pthread.h>
#include <semaphore.h>

// Shared memory and system types
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <dirent.h>

/*============================================================================*/
/*                             PROJECT INCLUDES                              */
/*============================================================================*/

#include "types.h"  ///< Defines Drone, Position, Wind, SharedMemory, ...
#include "monitor.h"

/*============================================================================*/
/*                             GLOBAL CONSTANTS                              */
/*============================================================================*/

// Folder containing all figure subdirectories (e.g., figureData/figure1/)
#define FIGURE_FOLDER "figureData"

// Number of collision events to store in ring buffer
#define COLLISION_BUFFER_SIZE 100

// Name of output report file generated at the end of simulation
#define REPORT_FILE "drone_simulation_report.txt"


/*============================================================================*/
/*                    EXTERN GLOBAL SHARED VARIABLES                         */
/*============================================================================*/

/* -------------------- Drone Information (loaded from file) -------------------- */
extern Drone drones[MAX_DRONES];      ///< Array storing each drone loaded from figure
extern int drone_count;               ///< Number of drones loaded for simulation

/* -------------------- Shared Memory & Semaphores -------------------- */
extern SharedMemory *shared_data;     ///< Pointer to the shared memory segment
extern int shm_fd;                    ///< File descriptor for shared memory object
extern sem_t *sem_positions[MAX_DRONES]; ///< Named semaphore (1 per drone) for access control
extern pid_t pids[MAX_DRONES];        ///< PIDs of forked child processes for each drone

/* -------------------- Thread Handles (Parent Only) -------------------- */
extern pthread_t collision_thread;    ///< Thread responsible for detecting collisions
extern pthread_t report_thread;       ///< Thread that writes collision report
extern pthread_t monitor_thread;      ///< Thread that tracks drone completion/status

/* -------------------- Thread Synchronization -------------------- */
extern pthread_mutex_t collision_mutex;      ///< Protects collision_buffer[] access
extern pthread_mutex_t report_mutex;         ///< Protects file writing and report counters
extern pthread_cond_t collision_detected;    ///< Signaled when a collision is detected

/* -------------------- Simulation Runtime State -------------------- */
extern CollisionEvent collision_buffer[COLLISION_BUFFER_SIZE]; ///< Ring buffer of collision logs
extern int collision_count;           ///< Total number of collisions detected
extern int new_collision_flag;        ///< Flag used to notify threads of a new event
extern int simulation_running;        ///< Global flag used to control simulation lifecycle

/* -------------------- Report Data Storage ---------------------- */
extern DroneExecutionHistory droneHistory[MAX_DRONES];		///< Store the report data


/*============================================================================*/
/*                           FUNCTION PROTOTYPES                             */
/*============================================================================*/

/* -------------------- Signal Handling (main.c) -------------------- */

/**
 * @brief Installs signal handlers (SIGINT, SIGTERM)
 */
void setup_signal_handlers(void);

/* -------------------- Shared Memory Functions (shared_resources.c) -------------------- */

/**
 * @brief Creates and maps shared memory
 * @return 0 on success, -1 on failure
 */
int setup_shared_memory(void);

/**
 * @brief Unmaps and unlinks shared memory
 */
void cleanup_shared_memory(void);

/* -------------------- Semaphore Functions (shared_resources.c) -------------------- */

/**
 * @brief Initializes per-drone named semaphores
 * @param drone_count Number of semaphores to create
 * @return 0 on success, -1 on failure
 */
int setup_semaphores(int drone_count);

/**
 * @brief Closes and unlinks all semaphores
 * @param drone_count Number of semaphores to cleanup
 */
void cleanup_semaphores(int drone_count);

/* -------------------- Drone Behavior Functions (drone.c) -------------------- */

/**
 * @brief Executes a single drone's movement sequence in its own process
 * @param drone_index Index of drone to simulate
 */
void drone_process(int drone_index);

/**
 * @brief Loads a drone's path from a `.txt` file
 * @param filepath File containing movement path
 * @param drone Pointer to struct to populate
 * @return 0 on success, -1 on failure
 */
int load_drone_routine(const char *filepath, Drone *drone);

/**
 * @brief Prints debug info about a drone
 * @param drone Pointer to struct
 */
void print_drone_info(const Drone *drone);

/**
 * @brief Validates all movement steps are within bounds
 * @param drone Pointer to struct
 * @return 0 if valid, -1 if any invalid coordinates
 */
int validate_drone_path(const Drone *drone);

/**
 * @brief Alternative loader function (optional use)
 */
int load_single_drone(const char *filepath, Drone *drone);

/* -------------------- User Interaction (utils.c) -------------------- */

/**
 * @brief Prints available figure directories (e.g., figure1, figure2)
 */
void list_figures(const char *folder);

/**
 * @brief Prompts user to select a figure (stores input in buffer)
 */
void ask_user_for_figure(char *selected_figure, size_t size);

/**
 * @brief Loads all drone `.txt` files from a figure folder
 */
int load_figure(const char *figure_path, Drone *drones, int *drone_count);

/**
 * @brief Waits for user confirmation (ENTER to start, Q to cancel)
 */
void wait_for_user_input(void);


/* -------------------- Monitoring & Reporting (monitor.c) -------------------- */

/**
 * @brief Thread entrypoint: watches shared memory and logs collisions
 */
void *collision_monitor_thread(void *arg);

/**
 * @brief Thread entrypoint: writes out simulation_report.log
 */
void *report_generation_thread(void *arg);

/**
 * @brief Thread entrypoint: monitors for simulation completion
 */
void *status_monitor_thread(void *arg);

#endif
