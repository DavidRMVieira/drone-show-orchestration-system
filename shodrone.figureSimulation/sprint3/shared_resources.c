/**
 * @file shared_resources.c
 * @brief POSIX shared memory and semaphore management for drone simulation
 * 
 * This file implements the shared resource management system for the drone
 * simulation, providing thread-safe access to shared memory and semaphore
 * synchronization primitives. The system uses POSIX shared memory for
 * inter-process communication and named semaphores for position data protection.
 * 
 * Key Features:
 * - POSIX shared memory creation and management
 * - Named semaphore creation and cleanup for each drone
 * - Automatic resource cleanup and error handling
 * - Thread-safe access to drone position data
 * 
 * Resource Management:
 * - Shared memory segment: "/drone_shm" - stores all drone positions and status
 * - Named semaphores: "/drone_sem_N" - one semaphore per drone for position locking
 * 
 */

/*============================================================================*/
/*                                INCLUDES                                   */
/*============================================================================*/

/* Project-specific headers */
#include "shared_resources.h"   /* Function prototypes and data structures */

/* Standard library includes */
#include <stdio.h>              /* For printf, perror, snprintf */
#include <fcntl.h>              /* For file control options (O_CREAT, O_RDWR, etc.) */
#include <unistd.h>             /* For close, ftruncate */
#include <sys/mman.h>           /* For mmap, munmap, shm_open, shm_unlink */
#include <string.h>             /* For memset */
#include <errno.h>              /* For errno handling */

/*============================================================================*/
/*                         EXTERNAL GLOBAL VARIABLES                         */
/*============================================================================*/

/* External global variables defined in main.c */
extern SharedMemory *shared_data;           /* Pointer to mapped shared memory */
extern int shm_fd;                          /* Shared memory file descriptor */
extern sem_t *sem_positions[MAX_DRONES];    /* Array of semaphore pointers for drone positions */

/*============================================================================*/
/*                          FUNCTION IMPLEMENTATIONS                         */
/*============================================================================*/

/**
 * @brief Creates and maps POSIX shared memory segment
 * 
 * This function creates a new POSIX shared memory object, sizes it appropriately
 * for the SharedMemory structure, and maps it into the process address space.
 * The shared memory is initialized to zero for safety.
 * 
 * @return 0 on success, -1 on failure
 * 
 * @note Memory Layout:
 *       The shared memory segment contains the entire SharedMemory structure
 *       including drone positions, active status flags, and other shared data.
 * 
 * @note Error Handling:
 *       On failure, automatically cleans up any partially created resources
 *       (file descriptor, shared memory object) before returning error.
 * 
 * @note Shared Memory Name: "/drone_shm"
 *       This name is used system-wide and must be unique. The leading "/"
 *       is required by POSIX shared memory naming conventions.
 */
int setup_shared_memory(void) 
{
    /*------------------------------------------------------------------------*/
    /* CREATE SHARED MEMORY OBJECT                                           */
    /*------------------------------------------------------------------------*/
    
    /* Open or create shared memory object with read/write permissions
     * O_CREAT: Create if it doesn't exist
     * O_RDWR: Open for reading and writing
     * 0666: Permissions (readable/writable by owner, group, others) */
    shm_fd = shm_open("/drone_shm", O_CREAT | O_RDWR, 0666);
    if (shm_fd == -1) 
    {
        perror("[SharedMemory] shm_open failed");
        return -1;
    }

    /*------------------------------------------------------------------------*/
    /* RESIZE SHARED MEMORY SEGMENT                                          */
    /*------------------------------------------------------------------------*/
    
    /* Resize shared memory to fit SharedMemory struct
     * This is required because newly created shared memory objects have size 0 */
    if (ftruncate(shm_fd, sizeof(SharedMemory)) == -1) 
    {
        perror("[SharedMemory] ftruncate failed");
        
        /* Cleanup on failure */
        close(shm_fd);
        shm_unlink("/drone_shm");
        return -1;
    }

    /*------------------------------------------------------------------------*/
    /* MAP SHARED MEMORY INTO PROCESS ADDRESS SPACE                          */
    /*------------------------------------------------------------------------*/
    
    /* Map shared memory into process address space
     * NULL: Let system choose the address
     * sizeof(SharedMemory): Size of mapping
     * PROT_READ | PROT_WRITE: Allow reading and writing
     * MAP_SHARED: Changes are shared with other processes
     * shm_fd: File descriptor of shared memory object
     * 0: Offset from beginning of shared memory object */
    shared_data = mmap(NULL, sizeof(SharedMemory),
                       PROT_READ | PROT_WRITE,
                       MAP_SHARED, shm_fd, 0);
    if (shared_data == MAP_FAILED) 
    {
        perror("[SharedMemory] mmap failed");
        
        /* Cleanup on failure */
        close(shm_fd);
        shm_unlink("/drone_shm");
        return -1;
    }

    /*------------------------------------------------------------------------*/
    /* INITIALIZE SHARED MEMORY CONTENTS                                     */
    /*------------------------------------------------------------------------*/
    
    /* Zero out shared memory region for safety
     * This ensures all drone positions start at (0,0,0) and all flags are false */
    memset(shared_data, 0, sizeof(SharedMemory));
    
    printf("[SharedMemory] Successfully created and mapped shared memory segment\n");
    return 0;
}

/**
 * @brief Unmaps and unlinks the shared memory segment
 * 
 * This function performs complete cleanup of the shared memory resources.
 * It unmaps the shared memory from the process address space, closes the
 * file descriptor, and removes the shared memory object from the system.
 * 
 * @note Cleanup Order:
 *       1. Unmap shared memory from process address space
 *       2. Close shared memory file descriptor
 *       3. Unlink shared memory object from system
 * 
 * @note Safe to call multiple times:
 *       Function checks for valid pointers/descriptors before cleanup
 *       and sets them to safe values after cleanup.
 */
void cleanup_shared_memory(void) 
{
    /*------------------------------------------------------------------------*/
    /* UNMAP SHARED MEMORY FROM PROCESS ADDRESS SPACE                        */
    /*------------------------------------------------------------------------*/
    
    /* Unmap shared memory if it was successfully mapped */
    if (shared_data) 
    {
        /* Unmap the shared memory region */
        if (munmap(shared_data, sizeof(SharedMemory)) == -1) 
        {
            perror("[SharedMemory] munmap failed");
        }
        
        /* Set pointer to NULL to prevent accidental access */
        shared_data = NULL;
    }

    /*------------------------------------------------------------------------*/
    /* CLOSE FILE DESCRIPTOR AND UNLINK SHARED MEMORY OBJECT                 */
    /*------------------------------------------------------------------------*/
    
    /* Close file descriptor and unlink shared memory object if valid */
    if (shm_fd != -1) 
    {
        /* Close the shared memory file descriptor */
        if (close(shm_fd) == -1) 
        {
            perror("[SharedMemory] close failed");
        }
        
        /* Remove shared memory object from the system
         * This ensures the shared memory object is deleted when the last
         * process using it terminates */
        if (shm_unlink("/drone_shm") == -1) 
        {
            perror("[SharedMemory] shm_unlink failed");
        }
        
        /* Set file descriptor to invalid value */
        shm_fd = -1;
    }
    
    printf("[SharedMemory] Shared memory cleanup completed\n");
}

/**
 * @brief Creates and initializes named semaphores for each drone
 * 
 * This function creates a named semaphore for each drone in the simulation.
 * Each semaphore protects access to that drone's position data, preventing
 * race conditions between drone movement threads and monitoring threads.
 * 
 * @param drone_count Number of drones (and semaphores) to create
 * @return 0 on success, -1 on failure
 * 
 * @note Semaphore Naming:
 *       Semaphores are named "/drone_sem_N" where N is the drone index (0-based).
 *       The leading "/" is required by POSIX semaphore naming conventions.
 * 
 * @note Initial Value:
 *       All semaphores are created with initial value 1 (unlocked state).
 *       This allows the first sem_wait() call to succeed immediately.
 * 
 * @note Error Handling:
 *       On failure, automatically cleans up any semaphores that were
 *       successfully created before the failure occurred.
 */
int setup_semaphores(int drone_count) 
{
    char sem_name[64];  /* Buffer for semaphore name generation */

    printf("[Semaphores] Creating %d semaphores for drone position protection\n", drone_count);

    /*------------------------------------------------------------------------*/
    /* CREATE SEMAPHORE FOR EACH DRONE                                       */
    /*------------------------------------------------------------------------*/
    
    for (int i = 0; i < drone_count; i++) 
    {
        /*--------------------------------------------------------------------*/
        /* GENERATE UNIQUE SEMAPHORE NAME                                     */
        /*--------------------------------------------------------------------*/
        
        /* Generate unique semaphore name for each drone
         * Format: "/drone_sem_0", "/drone_sem_1", etc. */
        snprintf(sem_name, sizeof(sem_name), "/drone_sem_%d", i);

        /*--------------------------------------------------------------------*/
        /* CLEANUP EXISTING SEMAPHORE (IF ANY)                               */
        /*--------------------------------------------------------------------*/
        
        /* Unlink first to avoid conflicts from previous runs
         * This is safe to call even if semaphore doesn't exist */
        sem_unlink(sem_name);

        /*--------------------------------------------------------------------*/
        /* CREATE NEW SEMAPHORE                                              */
        /*--------------------------------------------------------------------*/
        
        /* Create semaphore with initial value 1 (unlocked)
         * O_CREAT | O_EXCL: Create new semaphore, fail if already exists
         * 0666: Permissions (readable/writable by owner, group, others)
         * 1: Initial semaphore value (unlocked state) */
        sem_positions[i] = sem_open(sem_name, O_CREAT | O_EXCL, 0666, 1);
        if (sem_positions[i] == SEM_FAILED) 
        {
            perror("[Semaphores] sem_open failed");
            printf("[Semaphores] Failed to create semaphore for drone %d\n", i);

            /* Cleanup semaphores already created on failure */
            cleanup_semaphores(i);
            return -1;
        }
    }

    printf("[Semaphores] Successfully created all %d semaphores\n", drone_count);
    return 0;
}

/**
 * @brief Closes and unlinks all drone semaphores
 * 
 * This function performs complete cleanup of all drone semaphores.
 * It closes each semaphore descriptor and removes the named semaphore
 * from the system. This function is safe to call during error cleanup
 * (partial semaphore creation) or normal program termination.
 * 
 * @param drone_count Number of semaphores to clean up
 * 
 * @note Cleanup Process:
 *       1. Close semaphore descriptor (if valid)
 *       2. Unlink named semaphore from system
 *       3. Set semaphore pointer to NULL for safety
 * 
 * @note Safe for Partial Cleanup:
 *       Function checks each semaphore pointer before cleanup,
 *       making it safe to use during error recovery when only
 *       some semaphores were successfully created.
 */
void cleanup_semaphores(int drone_count) 
{
    char sem_name[64];  /* Buffer for semaphore name generation */

    printf("[Semaphores] Cleaning up %d semaphores\n", drone_count);

    /*------------------------------------------------------------------------*/
    /* CLEANUP EACH SEMAPHORE                                                */
    /*------------------------------------------------------------------------*/
    
    for (int i = 0; i < drone_count; i++) 
    {
        /* Only cleanup if semaphore was successfully created */
        if (sem_positions[i] != NULL && sem_positions[i] != SEM_FAILED) 
        {
            /*----------------------------------------------------------------*/
            /* CLOSE SEMAPHORE DESCRIPTOR                                     */
            /*----------------------------------------------------------------*/
            
            /* Close semaphore descriptor */
            if (sem_close(sem_positions[i]) == -1) 
            {
                perror("[Semaphores] sem_close failed");
            }

            /*----------------------------------------------------------------*/
            /* UNLINK NAMED SEMAPHORE FROM SYSTEM                            */
            /*----------------------------------------------------------------*/
            
            /* Generate semaphore name for unlinking */
            snprintf(sem_name, sizeof(sem_name), "/drone_sem_%d", i);
            
            /* Unlink named semaphore from the system
             * This removes the semaphore from the system namespace */
            if (sem_unlink(sem_name) == -1) 
            {
                perror("[Semaphores] sem_unlink failed");
            }

            /* Set semaphore pointer to NULL to prevent accidental access */
            sem_positions[i] = NULL;
        }
    }
    
    printf("[Semaphores] Semaphore cleanup completed\n");
}