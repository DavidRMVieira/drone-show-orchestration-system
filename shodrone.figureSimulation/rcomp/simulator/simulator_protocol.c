#define _POSIX_C_SOURCE 200809L

#include "simulator_protocol.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


bool parse_test_show_request(const char *request_str, TestShowRequest *request) {
    if (request_str == NULL || request == NULL) {
        return false;
    }

    char *copy = strdup(request_str);
    if (copy == NULL) {
        perror("");
        return false;
    }

    char *token;
    char *rest = copy;

    token = strtok_r(rest, ",", &rest);
    if (token == NULL || strcmp(token, "TEST_SHOW") != 0) {
        free(copy);
        return false;
    }

    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    while (*token == ' ') token++;
    if (*token == '\0' || strspn(token, "0123456789") != strlen(token)) {
        free(copy);
        return false;
    }
    request->duration = atoi(token);

    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    while (*token == ' ') token++;
    if (*token == '\0' || strspn(token, "0123456789") != strlen(token)) {
        free(copy);
        return false;
    }
    request->numberOfDrones = atoi(token);

    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    while (*token == ' ') token++;
    if (*token == '\0') {
        free(copy);
        return false;
    }
    request->latitude = atof(token);

    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    while (*token == ' ') token++;
    if (*token == '\0') {
        free(copy);
        return false;
    }
    request->longitude = atof(token);

    free(copy);
    return true;
}

bool parse_drone_init_request(const char *request_str, int *drone_id) {
    if (request_str == NULL || drone_id == NULL) {
        return false;
    }

    char *copy = strdup(request_str);
    if (copy == NULL) {
        perror("");
        return false;
    }

    char *token;
    char *rest = copy;

    token = strtok_r(rest, ",", &rest);
    if (token == NULL || strcmp(token, "DRONE_INIT") != 0) {
        free(copy);
        return false;
    }

    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    while (*token == ' ') token++;
    if (*token == '\0' || strspn(token, "0123456789") != strlen(token)) {
        free(copy);
        return false;
    }
    *drone_id = atoi(token);

    free(copy);
    return true;
}

ServerResponse* create_server_response() {
    ServerResponse* response = (ServerResponse*)malloc(sizeof(ServerResponse));
    if (response == NULL) {
        perror("");
        return NULL;
    }
    response->count = 0;
    for (size_t i = 0; i < MAX_MESSAGES_IN_RESPONSE; i++) {
        response->messages[i] = NULL;
    }
    return response;
}

int add_message_to_response(ServerResponse* response, const char* message) {
    if (response == NULL || message == NULL) {
        return -1;
    }
    if (response->count >= MAX_MESSAGES_IN_RESPONSE) {
        return -1;
    }

    response->messages[response->count] = strdup(message);
    if (response->messages[response->count] == NULL) {
        perror("");
        return -1;
    }
    response->count++;
    return 0;
}

void free_server_response(ServerResponse* response) {
    if (response == NULL) {
        return;
    }
    for (size_t i = 0; i < response->count; i++) {
        free(response->messages[i]);
    }
    free(response);
}
