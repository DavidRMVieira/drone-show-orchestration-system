#ifndef SIMULATOR_PROTOCOL_H
#define SIMULATOR_PROTOCOL_H

#include <stdbool.h>
#include <stddef.h> // For size_t

// --- General Protocol Constants ---
#define MAX_LINE_LENGTH 256 // Max length of a single protocol line
#define MAX_MESSAGES_IN_RESPONSE 20 // Max lines in a multi-line response (e.g., simulation commands)

// --- Request DTO for TEST_SHOW from Testing App ---
typedef struct {
    int duration;
    int numberOfDrones;
    double latitude;
    double longitude;
} TestShowRequest;

// --- Server Response Structure (for both Testing App and Drone Runner) ---
// This struct holds multi-line responses from the simulator.
typedef struct {
    char* messages[MAX_MESSAGES_IN_RESPONSE]; // Array of pointers to strings for each line
    size_t count; // Number of messages/lines received
} ServerResponse;


// --- Functions for parsing requests received by Simulator ---
/**
 * @brief Parses a "TEST_SHOW" request string.
 * Format: "TEST_SHOW,<duration>,<numberOfDrones>,<latitude>,<longitude>"
 * @param request_str The full request string.
 * @param request Pointer to the TestShowRequest struct to fill.
 * @return true if parsing is successful, false otherwise.
 */
bool parse_test_show_request(const char *request_str, TestShowRequest *request);

/**
 * @brief Parses a "DRONE_INIT" request string.
 * Format: "DRONE_INIT,<drone_id>"
 * @param request_str The full request string.
 * @param drone_id Pointer to an int to store the drone ID.
 * @return true if parsing is successful, false otherwise.
 */
bool parse_drone_init_request(const char *request_str, int *drone_id);

// --- Functions for ServerResponse management (used by client) ---
ServerResponse* create_server_response();
int add_message_to_response(ServerResponse* response, const char* message);
void free_server_response(ServerResponse* response);


#endif // SIMULATOR_PROTOCOL_H
