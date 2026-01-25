/**
 * @file utils.h
 * @brief Interface for simulation utility functions (figure listing and loading)
 * 
 * This header defines the interface for utility functions used during the
 * setup phase of the drone simulation. These functions enable dynamic loading
 * of drone formation files, interactive figure selection, and execution
 * control through user confirmation.
 * 
 * -----------------------------------------------------------------------------
 * Scope of This Module:
 * - Lists available drone figures from a directory (e.g., ./figureData/)
 * - Prompts user to select a figure interactively
 * - Loads each drone `.txt` file into a Drone struct array
 * - Waits for user confirmation before starting the simulation
 * -----------------------------------------------------------------------------
 * 
 * Dependencies:
 * - Requires `types.h` for the `Drone` data structure
 * 
 * Usage Context:
 * - Used exclusively by the main simulation controller to configure drones
 *   before launching processes, shared memory, or threads.
 */

#ifndef UTILS_H
#define UTILS_H

/*============================================================================*/
/*                                INCLUDES                                   */
/*============================================================================*/

#include "types.h"  /* Required for Drone struct definition */

/*============================================================================*/
/*                          FUNCTION DECLARATIONS                            */
/*============================================================================*/

/**
 * @brief Lists available drone figure directories
 *
 * Scans the specified base folder and prints the names of valid subdirectories
 * (excluding "." and ".."). Each valid directory is assumed to contain drone
 * definition `.txt` files and represents a drone formation.
 *
 * @param folder Path to the folder containing figure sets (e.g., "./figureData")
 *
 * @note This function is intended for informational display only and
 *       does not modify any state or allocate resources.
 */
void list_figures(const char *folder);

/**
 * @brief Prompts the user to select a figure
 *
 * Requests input from the user, storing the result in the provided buffer.
 * Ensures newline removal and safe storage.
 *
 * @param selected_figure Output buffer to store the entered figure name
 * @param size Maximum number of characters to read (buffer size)
 *
 * @note It is the caller's responsibility to ensure the buffer is large enough
 *       to store typical figure names (recommended minimum size: 64 bytes).
 */
void ask_user_for_figure(char *selected_figure, size_t size);

/**
 * @brief Loads drone definitions from a selected figure folder
 *
 * Opens the specified directory, reads all `.txt` drone files, and loads them
 * into the `drones` array using `load_drone_routine()`. The number of loaded
 * drones is returned through the `drone_count` pointer.
 *
 * @param figure_path Path to the selected figure directory (e.g., "./figureData/figure1")
 * @param drones Pre-allocated array of `Drone` structures to populate
 * @param drone_count Output pointer to store the number of successfully loaded drones
 * @return 0 on success, -1 if the directory could not be opened
 *
 * @warning Assumes enough space in the `drones` array for all valid drone files.
 */
int load_figure(const char *figure_path, Drone *drones, int *drone_count);

/**
 * @brief Waits for user confirmation before simulation start
 *
 * Displays a prompt asking the user to press ENTER to begin, or 'q' to cancel.
 * If the user types 'q', the simulation is immediately terminated using `exit(0)`.
 *
 * @note This is the final interactive checkpoint before launching drone processes.
 */
void wait_for_user_input(void);

#endif /* UTILS_H */