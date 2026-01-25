#include "csv_simulator_protocol_proxy.h"
#include "client_socket.h"
#include "common.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

FailedRequestException_t drone_runner_protocol_exchange(int drone_id) {
    ClientSocket* socket = client_socket_create();
    if (socket == NULL) {
        LOGGER_DEBUG("Falha ao criar o socket do cliente.");
        return FR_SERVER_ERROR;
    }

    FailedRequestException_t result = FR_SUCCESS;

    printf("Attempting to connect to simulator at %s:%d...\n", SERVERIP, SERVERPORT);
    if (client_socket_connect(socket, SERVERIP, SERVERPORT) != 0) {
        LOGGER_DEBUG("Falha ao conectar ao servidor do simulador em %s:%d.", SERVERIP, SERVERPORT);
        result = FR_SERVER_ERROR;
        client_socket_destroy(socket);
        return result;
    }
    printf("Connected to simulator!\n");

    char initial_request[CLIENT_BUFFER_SIZE];
    snprintf(initial_request, CLIENT_BUFFER_SIZE, "DRONE_INIT,%d", drone_id);
    printf("Sending: %s\n", initial_request);
    if (client_socket_send(socket, initial_request) < 0) {
        LOGGER_DEBUG("Falha ao enviar mensagem DRONE_INIT.");
        result = FR_SERVER_ERROR;
        client_socket_destroy(socket);
        return result;
    }

    bool simulation_active = true;
    while (simulation_active) {
        printf("Waiting for commands from simulator...\n");
        ServerResponse* response = client_socket_recv(socket);
        if (response == NULL) {
            LOGGER_DEBUG("Falha ao receber resposta do servidor ou conexão fechada inesperadamente.");
            result = FR_SERVER_ERROR;
            simulation_active = false;
            continue;
        }
        if (response->count == 0) {
            LOGGER_DEBUG("Resposta vazia do servidor. Continuando a aguardar.");
            free_server_response(response);
            continue;
        }

        for (size_t i = 0; i < response->count; i++) {
            char* message_line = response->messages[i];
            double x, y, z;
            if (strncmp(message_line, "COMMAND,MOVE_TO_POS", strlen("COMMAND,MOVE_TO_POS")) == 0) {
                if (sscanf(message_line, "COMMAND,MOVE_TO_POS,%lf,%lf,%lf", &x, &y, &z) == 3) {
                    printf("[DRONE] Received move command to position: x=%.2f, y=%.2f, z=%.2f\n", x, y, z);
                } else {
                    printf("[DRONE] Malformed move command: %s\n", message_line);
                }
            } else {
                printf("Simulator: %s\n", message_line);
            }

            if (strncmp(message_line, "END_SIMULATION", strlen("END_SIMULATION")) == 0) {
                printf("'END_SIMULATION' command received. Ending simulation.\n");
                simulation_active = false;
                break;
            }
        }

        free_server_response(response);
    }

    client_socket_stop(socket);
    client_socket_destroy(socket);

    printf("Disconnected from simulator.\n");
    return result;
}
