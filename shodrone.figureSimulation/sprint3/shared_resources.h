/**
 * @file shared_resources.h
 * @brief Header file for shared memory and synchronization resource management
 * 
 * This header file contains function prototypes for managing shared memory
 * segments and POSIX named semaphores used in the multi-process drone
 * simulation system. The module provides a centralized interface for
 * setting up and cleaning up inter-process communication resources.
 * 
 * The shared resources system manages:
 * 1. POSIX Shared Memory: Used for storing drone position data and simulation state
 * 2. Named Semaphores: Used for synchronizing access to individual drone data slots
 * 
 * @note Dependencies:
 *       - types.h: Contains shared data structure definitions and simulation constants
 *       - semaphore.h: Required for POSIX named semaphore operations
 *       - sys/mman.h: Required for POSIX shared memory operations (included by source files)
 *       - fcntl.h: Required for shared memory creation flags (included by source files)
 * 
 * @note Resource Management:
 *       All functions in this module follow RAII-style resource management where
 *       each setup function has a corresponding cleanup function that must be
 *       called to prevent resource leaks and system pollution.
 * 
 * @note Multi-Process Safety:
 *       All shared resources created by this module are designed for safe
 *       concurrent access across multiple processes using appropriate
 *       synchronization primitives.
 */

/*============================================================================*/
/*                              HEADER GUARD                                 */
/*============================================================================*/

#ifndef SHARED_RESOURCES_H
#define SHARED_RESOURCES_H

/*============================================================================*/
/*                                INCLUDES                                   */
/*============================================================================*/

/* Include project-specific type definitions
 * Contains shared data structures and simulation configuration constants */
#include "types.h"

/* Include POSIX semaphore header for named semaphore operations
 * Required for sem_t type and semaphore function prototypes */
#include <semaphore.h>

/*============================================================================*/
/*                         FUNCTION PROTOTYPES                               */
/*============================================================================*/

/**
 * @brief Initializes the shared memory segment for drone simulation data
 * 
 * Creates and maps a POSIX shared memory segment sized to accommodate all
 * shared simulation data including drone positions, status flags, and
 * synchronization counters. The shared memory is created with read/write
 * permissions and is immediately mapped into the calling process's address space.
 * 
 * @return 0 on successful shared memory creation and mapping, -1 on failure
 * 
 * @note Key Responsibilities:
 *       - Create POSIX shared memory object with unique name
 *       - Set appropriate size based on simulation requirements
 *       - Map shared memory into process address space with read/write access
 *       - Initialize shared memory contents to safe default values
 *       - Set proper permissions for multi-process access
 * 
 * @note Memory Layout:
 *       The shared memory segment contains:
 *       - Drone position arrays (x, y, z coordinates for each drone)
 *       - Simulation control flags (running status, termination signals)
 *       - Collision detection counters and event buffers
 *       - Timestamp information for synchronization
 * 
 * @note Error Conditions:
 *       Function returns -1 if any of the following occur:
 *       - shm_open() fails (insufficient permissions, name conflicts)
 *       - ftruncate() fails (disk space issues, invalid file descriptor)
 *       - mmap() fails (insufficient virtual memory, invalid parameters)
 *       - Memory initialization fails
 * 
 * @note Resource Cleanup:
 *       Must be paired with cleanup_shared_memory() to prevent:
 *       - Shared memory object persistence after program termination
 *       - Virtual memory leaks in the process address space
 *       - System resource exhaustion
 * 
 * @note Usage Pattern:
 *       Should be called once during simulation initialization, before
 *       any child processes are created or drone simulation begins.
 * 
 * @code
 * if (setup_shared_memory() != 0) {
 *     perror("Failed to initialize shared memory");
 *     exit(EXIT_FAILURE);
 * }
 * printf("Shared memory initialized successfully\n");
 * @endcode
 */
int setup_shared_memory(void);

/**
 * @brief Unmaps and unlinks the shared memory segment
 * 
 * Performs complete cleanup of shared memory resources created by
 * setup_shared_memory(). This includes unmapping the shared memory
 * from the process address space and unlinking the shared memory
 * object from the system namespace.
 * 
 * @note Key Responsibilities:
 *       - Unmap shared memory from process address space using munmap()
 *       - Unlink shared memory object from system namespace using shm_unlink()
 *       - Reset internal pointers and flags to prevent accidental access
 *       - Ensure complete resource deallocation
 * 
 * @note Call Requirements:
 *       - Must be called after setup_shared_memory() has been successfully executed
 *       - Should be called from the same process that created the shared memory
 *       - Must be called before program termination to prevent resource leaks
 * 
 * @note Error Handling:
 *       - Function attempts cleanup even if individual operations fail
 *       - Logs errors but continues with remaining cleanup operations
 *       - Designed to be safe to call multiple times (idempotent)
 * 
 * @note System Impact:
 *       Failure to call this function results in:
 *       - Persistent shared memory objects in /dev/shm
 *       - Virtual memory leaks in the process
 *       - Potential system resource exhaustion over time
 * 
 * @code
 * // At program termination or error cleanup
 * cleanup_shared_memory();
 * printf("Shared memory resources cleaned up\n");
 * @endcode
 */
void cleanup_shared_memory(void);

/**
 * @brief Creates and initializes named semaphores for drone synchronization
 * 
 * Creates a dedicated POSIX named semaphore for each drone in the simulation
 * to provide fine-grained synchronization control. Each semaphore controls
 * access to one drone's data slot in the shared memory segment, preventing
 * race conditions during concurrent read/write operations.
 * 
 * @param drone_count Total number of drones in simulation (must be > 0)
 * @return 0 on successful creation of all semaphores, -1 on any failure
 * 
 * @note Key Responsibilities:
 *       - Create unique named semaphore for each drone (indexed 0 to drone_count-1)
 *       - Initialize each semaphore with value 1 (binary semaphore behavior)
 *       - Set appropriate permissions for multi-process access
 *       - Validate drone_count parameter for reasonable limits
 *       - Store semaphore handles for later cleanup operations
 * 
 * @note Semaphore Naming Convention:
 *       Semaphores are named using the pattern: "/drone_sem_<index>"
 *       where <index> ranges from 0 to (drone_count - 1)
 *       Example: "/drone_sem_0", "/drone_sem_1", "/drone_sem_2", ...
 * 
 * @note Synchronization Model:
 *       - Each semaphore initialized to value 1 (unlocked state)
 *       - Binary semaphore behavior: 0 = locked, 1 = unlocked
 *       - Provides mutual exclusion for individual drone data access
 *       - Allows concurrent access to different drones' data
 * 
 * @note Parameter Validation:
 *       - drone_count must be positive (> 0)
 *       - Reasonable upper limit enforced to prevent resource exhaustion
 *       - Invalid parameters result in immediate function failure
 * 
 * @note Error Conditions:
 *       Function returns -1 if any of the following occur:
 *       - Invalid drone_count parameter (≤ 0 or exceeds system limits)
 *       - sem_open() fails for any semaphore (permission, naming, or resource issues)
 *       - System semaphore limit exceeded
 *       - Insufficient system resources for semaphore creation
 * 
 * @note Cleanup Requirements:
 *       Must be paired with cleanup_semaphores() using the same drone_count
 *       value to ensure all created semaphores are properly cleaned up.
 * 
 * @note Thread Safety:
 *       Function is not thread-safe and should only be called during
 *       single-threaded initialization phase of the program.
 * 
 * @code
 * int num_drones = 10;
 * if (setup_semaphores(num_drones) != 0) {
 *     perror("Failed to create drone semaphores");
 *     cleanup_shared_memory();  // Clean up any previously allocated resources
 *     exit(EXIT_FAILURE);
 * }
 * printf("Created %d drone synchronization semaphores\n", num_drones);
 * @endcode
 */
int setup_semaphores(int drone_count);

/**
 * @brief Closes and unlinks all named semaphores created for drone synchronization
 * 
 * Performs complete cleanup of all named semaphores created by setup_semaphores().
 * This includes closing semaphore handles in the current process and unlinking
 * the semaphore names from the system namespace to prevent resource persistence.
 * 
 * @param drone_count Number of semaphores to cleanup (must match setup_semaphores call)
 * 
 * @note Key Responsibilities:
 *       - Close all semaphore handles using sem_close()
 *       - Unlink all semaphore names from system namespace using sem_unlink()
 *       - Clean up internal tracking structures and pointers
 *       - Handle partial cleanup scenarios gracefully
 * 
 * @note Parameter Requirements:
 *       - drone_count must match the value used in setup_semaphores()
 *       - Function uses drone_count to determine semaphore names to clean up
 *       - Mismatched drone_count may result in incomplete cleanup
 * 
 * @note Cleanup Strategy:
 *       - Attempts to clean up all semaphores even if individual operations fail
 *       - Logs errors for debugging but continues with remaining cleanup
 *       - Designed to be robust against partial failure scenarios
 *       - Safe to call multiple times (idempotent behavior)
 * 
 * @note Error Handling:
 *       - Individual semaphore cleanup failures are logged but don't stop processing
 *       - Function continues attempting cleanup of remaining semaphores
 *       - Designed to handle cases where some semaphores may already be cleaned up
 * 
 * @note System Impact:
 *       Failure to call this function results in:
 *       - Persistent named semaphores in system namespace (/dev/shm)
 *       - System semaphore resource leaks
 *       - Potential conflicts with future program executions
 *       - Gradual system resource exhaustion
 * 
 * @note Call Requirements:
 *       - Should be called from the same process that created the semaphores
 *       - Must be called before program termination
 *       - Should be called even if setup_semaphores() partially failed
 * 
 * @code
 * // At program termination or error cleanup
 * int num_drones = 10;  // Same value used in setup_semaphores()
 * cleanup_semaphores(num_drones);
 * printf("Cleaned up %d drone semaphores\n", num_drones);
 * @endcode
 */
void cleanup_semaphores(int drone_count);

/*============================================================================*/
/*                               USAGE EXAMPLES                              */
/*============================================================================*/

/**
 * @example
 * @brief Complete shared resource initialization and cleanup example
 * 
 * This example demonstrates the proper initialization and cleanup sequence
 * for shared resources in a drone simulation program:
 * 
 * @code
 * #include "shared_resources.h"
 * #include <stdio.h>
 * #include <stdlib.h>
 * 
 * int main() {
 *     int drone_count = 20;
 *     
 *     // Initialize shared memory first
 *     if (setup_shared_memory() != 0) {
 *         fprintf(stderr, "Error: Failed to initialize shared memory\n");
 *         return EXIT_FAILURE;
 *     }
 *     printf("Shared memory initialized successfully\n");
 *     
 *     // Initialize semaphores for drone synchronization
 *     if (setup_semaphores(drone_count) != 0) {
 *         fprintf(stderr, "Error: Failed to create drone semaphores\n");
 *         cleanup_shared_memory();  // Clean up shared memory before exit
 *         return EXIT_FAILURE;
 *     }
 *     printf("Created %d drone semaphores successfully\n", drone_count);
 *     
 *     // Run simulation here...
 *     printf("Running drone simulation...\n");
 *     
 *     // Cleanup resources in reverse order of initialization
 *     cleanup_semaphores(drone_count);
 *     cleanup_shared_memory();
 *     printf("All shared resources cleaned up successfully\n");
 *     
 *     return EXIT_SUCCESS;
 * }
 * @endcode
 */

/**
 * @example
 * @brief Error handling and partial cleanup example
 * 
 * This example shows robust error handling with proper partial cleanup:
 * 
 * @code
 * #include "shared_resources.h"
 * 
 * int initialize_simulation_resources(int drone_count) {
 *     // Attempt shared memory initialization
 *     if (setup_shared_memory() != 0) {
 *         perror("setup_shared_memory failed");
 *         return -1;
 *     }
 *     
 *     // Attempt semaphore initialization
 *     if (setup_semaphores(drone_count) != 0) {
 *         perror("setup_semaphores failed");
 *         cleanup_shared_memory();  // Clean up what we successfully created
 *         return -1;
 *     }
 *     
 *     return 0;  // Success
 * }
 * 
 * void cleanup_simulation_resources(int drone_count) {
 *     // Always attempt full cleanup, even if initialization partially failed
 *     cleanup_semaphores(drone_count);
 *     cleanup_shared_memory();
 * }
 * @endcode
 */

/*============================================================================*/
/*                           END OF HEADER GUARD                             */
/*============================================================================*/

#endif // SHARED_RESOURCES_H