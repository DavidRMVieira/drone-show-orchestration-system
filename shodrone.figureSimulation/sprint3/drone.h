/**
 * @file drone.h
 * @brief Interface for drone-related operations and execution
 * 
 * This header defines the interface for all drone-related functionality, including:
 * - Loading and validating drone movement routines from external text files
 * - Verifying drone movement limits and bounds compliance
 * - Executing drone movement routines in forked child processes
 * - Debugging utilities to inspect drone data
 * 
 * Design Considerations:
 * - Paths must remain within simulation limits (MAX_X, MAX_Y, MAX_Z, MAX_TIME)
 * - Drone data is synchronized via semaphores and stored in shared memory
 * 
 * All functions are implemented in `drone.c`
 */

#ifndef DRONE_H
#define DRONE_H

#include "types.h"  // For Position, Drone struct, and simulation constants

/*============================================================================*/
/*                             FUNCTION PROTOTYPES                            */
/*============================================================================*/

/**
 * @brief Loads a drone's movement routine from a file
 * 
 * This function opens the specified `.txt` file and reads the drone's 3D movement
 * path. It performs the following:
 * - Supports space-separated or comma-separated values
 * - Validates that all positions fall within simulation bounds
 * - Rejects the file if any malformed or out-of-bounds lines are encountered
 * - Rejects the file if step count exceeds MAX_TIME
 * 
 * @param filepath Path to the movement file (e.g., "./figureData/figure1/drone0.txt")
 * @param drone Pointer to the Drone structure to populate
 * @return 0 on success, -1 on failure (invalid lines, out of bounds, or no valid steps)
 */
int load_drone_routine(const char *filepath, Drone *drone);

/**
 * @brief Prints debug information about a drone
 * 
 * Displays the drone's internal metadata including:
 * - ID and name (from filename)
 * - Number of movement steps
 * - The first five position steps (or fewer if less than 5)
 * 
 * Intended for debugging after loading routines from disk.
 * 
 * @param drone Pointer to the drone structure to inspect
 */
void print_drone_info(const Drone *drone);

/**
 * @brief Verifies all drone movements are within valid grid bounds
 * 
 * This function checks that each movement step is contained within the simulation space:
 * - X in [0, MAX_X)
 * - Y in [0, MAX_Y)
 * - Z in [0, MAX_Z)
 * 
 * @param drone Pointer to the drone structure to validate
 * @return 0 if all steps are within bounds, -1 if any step is out of range
 */
int validate_drone_path(const Drone *drone);

/**
 * @brief Executes the drone's movement routine in a forked process
 * 
 * The drone writes each movement step to shared memory, synchronized by its semaphore.
 * The drone marks itself as active during execution and inactive when finished.
 * Timing is simulated using a 100ms delay between steps.
 * 
 * @param index Index of the drone in the global arrays (used for shared memory access)
 */
void drone_process(int index);

#endif /* DRONE_H */
