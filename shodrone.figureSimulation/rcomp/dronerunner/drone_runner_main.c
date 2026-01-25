#include "ui_utils.h" // Para console_read_line
#include "drone_runner.h"
#include "common.h" // Para LOGGER_DEBUG
#include "client_socket.h"

#include <stdio.h>
#include <stdlib.h> // Para exit, system
#include <string.h> // Para strcmp

#define SIMULATOR_HOST "vs903.dei.isep.ipp.pt"
#define SIMULATOR_PORT 10005

int main() {
    printf("=== Drone Runner ===\n");

    // Step 1: Get number of drones from simulator
    ClientSocket* num_socket = client_socket_create();
    if (!num_socket || client_socket_connect(num_socket, SIMULATOR_HOST, SIMULATOR_PORT) != 0) {
        fprintf(stderr, "Failed to connect to simulator for drone count.\n");
        return EXIT_FAILURE;
    }
    ServerResponse* num_resp = client_socket_send_and_recv(num_socket, "GET_NUM_DRONES");
    int num_drones = 0;
    if (num_resp && num_resp->count > 0) {
        // Expecting: NUM_DRONES,<n>
        char* msg = num_resp->messages[0];
        if (strncmp(msg, "NUM_DRONES,", 11) == 0) {
            num_drones = atoi(msg + 11);
        }
    }
    free_server_response(num_resp);
    client_socket_destroy(num_socket);
    if (num_drones <= 0) {
        fprintf(stderr, "Could not retrieve number of drones from simulator.\n");
        return EXIT_FAILURE;
    }

    printf("\nWelcome to Drone Runner!\n");
    printf("Please select the ID of the drone you want to simulate:\n");
    for (int i = 1; i <= num_drones; i++) {
        printf("  %d. Drone %d\n", i, i);
    }
    printf("\nEnter the drone ID (1-%d): ", num_drones);

    char input_buffer[10];
    if (console_read_line(input_buffer, sizeof(input_buffer)) == NULL) {
        fprintf(stderr, "Error reading user input.\n");
        return EXIT_FAILURE;
    }
    int selected_drone_id = atoi(input_buffer);
    if (selected_drone_id < 1 || selected_drone_id > num_drones) {
        printf("Invalid drone ID. Please enter a number between 1 and %d.\n", num_drones);
        return EXIT_FAILURE;
    }

    printf("\nStarting Drone Runner for Drone %d...\n", selected_drone_id);
    printf("Connecting to Simulator...\n");

    if (!main_drone_ui(selected_drone_id)) {
        fprintf(stderr, "Unexpected errors occurred during drone simulation.\n");
        return EXIT_FAILURE;
    }
    printf("Drone %d finished its participation in the simulation.\n", selected_drone_id);
    return EXIT_SUCCESS;
}
