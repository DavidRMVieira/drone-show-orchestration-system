/**
 * @file utils.c
 * @brief Utility functions for drone simulation setup
 *
 * This module handles the user interaction and figure data loading phase of the
 * simulation. It provides functionality to:
 * - List available drone figure sets stored in subdirectories
 * - Prompt the user to select a figure
 * - Load each drone file from the selected figure into memory
 * - Confirm user intention before starting the simulation
 *
 * These utility functions ensure a flexible and dynamic setup phase where new
 * drone formations can be introduced without code changes—only by adding new
 * `.txt` files to the `figureData/` folder.
 *
 * Functions in this module are called early during the simulation setup and
 * do not directly access shared memory or semaphores.
 */

/*============================================================================*/
/*                                INCLUDES                                   */
/*============================================================================*/

#include <stdio.h>      // For printf, fgets, perror
#include <stdlib.h>     // For exit, EXIT_FAILURE
#include <string.h>     // For strcmp, strncpy, strcspn, strstr
#include <dirent.h>     // For opendir, readdir, closedir
#include <unistd.h>     // For system-level I/O operations

#include "types.h"      // For Drone struct definition
#include "drone.h"      // For load_drone_routine() prototype
#include "utils.h"      // For function declarations in this module

/*============================================================================*/
/*                          FUNCTION IMPLEMENTATIONS                         */
/*============================================================================*/

/**
 * @brief Lists all available drone figure sets
 *
 * This function scans the specified folder (typically "./figureData") and
 * prints all valid subdirectory names to stdout. These represent available
 * figure formations that can be loaded and simulated.
 *
 * @param folder Path to the folder containing figure subdirectories
 *
 * @note A valid figure is identified as a subdirectory (ignoring "." and "..").
 *
 * @warning Exits the program if the folder cannot be opened (e.g., missing path).
 */
void list_figures(const char *folder) {
    // Open the directory containing the figure sets
    DIR *dir = opendir(folder);
    if (!dir) {
        // Print error and exit if folder cannot be opened
        perror("[Utils] Failed to open figure data folder");
        exit(EXIT_FAILURE);
    }

    // Print header for available figures
    printf("Available figures:\n");

    // Create a pointer to iterate over directory entries
    struct dirent *entry;

    // Loop through all entries in the directory
    while ((entry = readdir(dir))) {
        // Check if the entry is a directory and is not '.' or '..'
        if (entry->d_type == DT_DIR &&
            strcmp(entry->d_name, ".") != 0 &&
            strcmp(entry->d_name, "..") != 0) {
            // Print the directory name (valid figure)
            printf("- %s\n", entry->d_name);
        }
    }

    // Close the directory after use
    closedir(dir);
}

/**
 * @brief Prompts the user to input the desired figure name
 *
 * This function requests user input and stores the result in the provided buffer.
 * Input is trimmed to remove trailing newline characters from `fgets`.
 *
 * @param selected_figure Output buffer to hold the chosen figure name
 * @param size Maximum number of characters to store (including null terminator)
 *
 * @note Uses `fgets` for input safety, avoiding buffer overflow.
 * @warning If the user input exceeds the buffer size, input may be truncated.
 */
void ask_user_for_figure(char *selected_figure, size_t size) {
    // Prompt the user for figure input
    printf("\nEnter the name of the figure to load (e.g., figure1): ");

    // Read the input into the provided buffer
    if (fgets(selected_figure, size, stdin)) {
        // Strip the newline character added by fgets
        selected_figure[strcspn(selected_figure, "\n")] = '\0';
    }
}

/**
 * @brief Loads all drone files from the specified figure folder
 *
 * This function iterates over the `.txt` files in the given folder, loading
 * each into a `Drone` structure using `load_drone_routine()`. Each drone is
 * assigned an ID and its name is extracted from the filename.
 *
 * @param figure_path Path to the folder (e.g., "./figureData/figure1")
 * @param drones Array where drone data will be stored
 * @param drone_count Pointer where the number of loaded drones will be stored
 *
 * @return 0 on success, -1 on failure (unable to open directory)
 *
 * @note Ignores non-`.txt` files and silently skips loading failures.
 * @warning If the folder contains too many drones (exceeding array size),
 * this function may cause overflow unless external limits are enforced.
 */
int load_figure(const char *figure_path, Drone *drones, int *drone_count) {
    // Open the directory corresponding to the selected figure
    DIR *dir = opendir(figure_path);
    if (!dir) {
        perror("[Utils] Failed to open figure directory");
        return -1;
    }

    struct dirent *entry;
    int count = 0;

    while ((entry = readdir(dir))) {
        // Check if entry is a .txt file
        if (strstr(entry->d_name, ".txt")) {
            char filepath[512];
            snprintf(filepath, sizeof(filepath), "%s/%s", figure_path, entry->d_name);

            // Assign ID and name
            drones[count].id = count;
            strncpy(drones[count].name, entry->d_name, sizeof(drones[count].name) - 1);
            drones[count].name[sizeof(drones[count].name) - 1] = '\0';

            // Try to load the routine
            if (load_drone_routine(filepath, &drones[count]) == 0) {
                // Validate movements for out-of-bounds coordinates
                if (validate_drone_path(&drones[count]) == 0) {
                    printf("Loaded %s (%d steps)\n", drones[count].name, drones[count].duration);
                    count++;  // Valid drone accepted
                } else {
                    // Free memory if validation fails
                    free(drones[count].movements);
                    drones[count].movements = NULL;
                    drones[count].duration = 0;
                    printf("[Warning] Skipping %s due to invalid path\n", drones[count].name);
                }
            }
        }
        printf("\n");
    }

    closedir(dir);
    *drone_count = count;
    return 0;
}

/**
 * @brief Waits for user confirmation before starting the simulation
 *
 * Prompts the user to press ENTER to continue or type 'q' to cancel.
 * If 'q' or 'Q' is entered, the simulation terminates immediately.
 *
 * @note This pause allows the user to prepare or verify before the simulation
 * launches processes and threads.
 *
 * @warning If the user inputs 'q', this function will call `exit(0)`.
 * Use only in the main flow of execution, not in threads or critical routines.
 */
void wait_for_user_input(void) {
    char cmd[10];  // Small buffer for user input command

    // Prompt the user for input
    printf("[Parent] Press ENTER to start simulation or 'q' to cancel: ");

    // Read the user command
    if (fgets(cmd, sizeof(cmd), stdin)) {
        // If user typed 'q' or 'Q', terminate the program
        if (cmd[0] == 'q' || cmd[0] == 'Q') {
            printf("[Parent] Simulation cancelled by user.\n");
            exit(0);
        }
    }
}
