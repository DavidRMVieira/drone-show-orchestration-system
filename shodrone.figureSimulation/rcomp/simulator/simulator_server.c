#include "simulator_server.h"
#include "simulator_protocol.h"
#include "simulator_state.h"
#include "common.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <errno.h>

#define BUFFER_SIZE 1024
#define MAX_CLIENTS 10
#define SERVER_PORT 10005

typedef struct {
    int client_sock;
    struct sockaddr_in client_addr;
    pthread_t tid;
} client_handler_args;

ssize_t send_response(int sock, const char* message) {
    ssize_t bytes_sent = send(sock, message, strlen(message), 0);
    if (bytes_sent == -1) {
        perror("");
        LOGGER_DEBUG("Erro ao enviar mensagem para socket %d: %s", sock, strerror(errno));
        return -1;
    }
    ssize_t empty_line_sent = send(sock, "\n", 1, 0);
    if (empty_line_sent == -1) {
        perror("");
        LOGGER_DEBUG("Erro ao enviar linha vazia para socket %d: %s", sock, strerror(errno));
        return -1;
    }
    return bytes_sent + empty_line_sent;
}

void handle_testing_app_client(int client_sock, const char* request_str) {
    TestShowRequest req_data;
    if (!parse_test_show_request(request_str, &req_data)) {
        char error_response[BUFFER_SIZE];
        snprintf(error_response, BUFFER_SIZE, "BAD_REQUEST,Invalid TEST_SHOW format.\n");
        send_response(client_sock, error_response);
        close(client_sock);
        LOGGER_DEBUG("Invalid TEST_SHOW request.");
        return;
    }

    pthread_mutex_lock(&g_simulator_state.mutex);

    if (g_simulator_state.test_show_received) {
        char response[BUFFER_SIZE];
        snprintf(response, BUFFER_SIZE, "SERVER_ERROR,Já existe uma simulação pendente/em curso. Por favor, aguarde.\n");
        send_response(client_sock, response);
        pthread_mutex_unlock(&g_simulator_state.mutex);
        close(client_sock);
        LOGGER_DEBUG("Nova requisição TEST_SHOW recebida enquanto outra estava pendente.");
        return;
    }

    g_simulator_state.current_test_show_request = req_data;
    g_simulator_state.test_show_received = true;
    g_simulator_state.testing_app_response_sock = client_sock;
    g_simulator_state.num_drones_expected = req_data.numberOfDrones;
    g_simulator_state.num_drones_ready = 0;
    g_simulator_state.simulation_active = true;

    char pending_response[BUFFER_SIZE];
    snprintf(pending_response, BUFFER_SIZE, "SIMULATION_PENDING,Waiting for %d Drone Runner(s)...\n", req_data.numberOfDrones);
    send_response(client_sock, pending_response);
    printf("Testing App connected. Waiting for %d Drone Runner(s).\n", req_data.numberOfDrones);

    while (g_simulator_state.simulation_active) {
        pthread_cond_wait(&g_simulator_state.simulation_complete_cond, &g_simulator_state.mutex);
    }

    char final_response[BUFFER_SIZE];
    if (g_simulator_state.num_drones_ready == 0 && !g_simulator_state.test_show_received) {
        snprintf(final_response, BUFFER_SIZE, "SIMULATION_COMPLETE,Duration:%d,Drones:%d,Lat:%.4f,Lon:%.4f,Simulation completed successfully.\n",
                 req_data.duration, req_data.numberOfDrones, req_data.latitude, req_data.longitude);
    } else {
        snprintf(final_response, BUFFER_SIZE, "SERVER_ERROR,Simulation ended unexpectedly.\n");
    }

    send_response(client_sock, final_response);
    printf("Simulation responses sent to Testing App.\n");

    g_simulator_state.test_show_received = false;
    g_simulator_state.testing_app_response_sock = -1;

    pthread_mutex_unlock(&g_simulator_state.mutex);
    close(client_sock);
    LOGGER_DEBUG("Testing App cliente tratado e socket fechado.");
}

void handle_drone_runner_client(int client_sock, const char* request_str, __attribute__((unused)) pthread_t tid) {
    int drone_id = 0;
    char prefix[32];

    while (*request_str == ' ' || *request_str == '\t' || *request_str == '\n' || *request_str == '\r')
        request_str++;

    if (sscanf(request_str, " %31[^,] , %d", prefix, &drone_id) != 2 || strcmp(prefix, "DRONE_INIT") != 0 || drone_id <= 0) {
        char error_response[BUFFER_SIZE];
        snprintf(error_response, BUFFER_SIZE, "BAD_REQUEST,Invalid DRONE_INIT format.\n");
        send_response(client_sock, error_response);
        close(client_sock);
        return;
    }

    char ack_response[BUFFER_SIZE];
    snprintf(ack_response, BUFFER_SIZE, "DRONE_READY,Ready to receive commands for Drone ID %d.\n", drone_id);
    send_response(client_sock, ack_response);

    char filepath[256];
    snprintf(filepath, sizeof(filepath), "drones/drone%d.txt", drone_id);
    FILE *fp = fopen(filepath, "r");
    if (!fp) {
        char error_response[BUFFER_SIZE];
        snprintf(error_response, BUFFER_SIZE, "ERROR,Could not open movement file for Drone %d.\n", drone_id);
        send_response(client_sock, error_response);
        close(client_sock);
        return;
    }
    char line[BUFFER_SIZE];
    while (fgets(line, sizeof(line), fp)) {
        double x, y, z;
        if (sscanf(line, "start initiation: x=%lf y=%lf z=%lf", &x, &y, &z) == 3 ||
            sscanf(line, "move to: x=%lf y=%lf z=%lf", &x, &y, &z) == 3 ||
            sscanf(line, "%lf %lf %lf", &x, &y, &z) == 3) {
            char command[BUFFER_SIZE];
            snprintf(command, BUFFER_SIZE, "COMMAND,MOVE_TO_POS,%.2f,%.2f,%.2f\n", x, y, z);
            send_response(client_sock, command);
        }
    }
    fclose(fp);

    char end_sim_msg[BUFFER_SIZE];
    snprintf(end_sim_msg, BUFFER_SIZE, "END_SIMULATION,Simulation for Drone %d finished.\n", drone_id);
    send_response(client_sock, end_sim_msg);
    close(client_sock);
}

void *client_handler_thread(void *arg) {
    client_handler_args *args = (client_handler_args *)arg;
    int client_sock = args->client_sock;
    struct sockaddr_in client_addr = args->client_addr;
    char buffer[BUFFER_SIZE];
    ssize_t bytes_read;

    printf("Conexão aceita de %s:%d\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));

    bytes_read = recv(client_sock, buffer, BUFFER_SIZE - 1, 0);
    if (bytes_read <= 0) {
        if (bytes_read == 0) {
            printf("Cliente %s:%d desconectado inesperadamente.\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
        } else {
            perror("");
            LOGGER_DEBUG("Erro ao receber dados: %s", strerror(errno));
        }
        close(client_sock);
        free(args);
        return NULL;
    }
    buffer[bytes_read] = '\0';
    buffer[strcspn(buffer, "\n")] = '\0';

    LOGGER_DEBUG("Mensagem inicial recebida: '%s'", buffer);

    char original_buffer[BUFFER_SIZE];
    strncpy(original_buffer, buffer, BUFFER_SIZE);
    original_buffer[BUFFER_SIZE-1] = '\0';

    char buffer_copy[BUFFER_SIZE];
    strncpy(buffer_copy, buffer, BUFFER_SIZE);
    buffer_copy[BUFFER_SIZE-1] = '\0';

    char *command_type = strtok(buffer_copy, ",");

    if (command_type == NULL) {
        char error_response[BUFFER_SIZE];
        snprintf(error_response, BUFFER_SIZE, "BAD_REQUEST,Invalid initial command.\n");
        send_response(client_sock, error_response);
        close(client_sock);
        LOGGER_DEBUG("Null or invalid initial command.");
    } else if (strcmp(command_type, "TEST_SHOW") == 0) {
        handle_testing_app_client(client_sock, original_buffer);
    } else if (strcmp(command_type, "DRONE_INIT") == 0) {
        handle_drone_runner_client(client_sock, original_buffer, args->tid);
    } else if (strcmp(command_type, "GET_NUM_DRONES") == 0) {
        char response[BUFFER_SIZE];
        pthread_mutex_lock(&g_simulator_state.mutex);
        snprintf(response, BUFFER_SIZE, "NUM_DRONES,%d\n", g_simulator_state.num_drones_expected);
        pthread_mutex_unlock(&g_simulator_state.mutex);
        send_response(client_sock, response);
        close(client_sock);
        LOGGER_DEBUG("GET_NUM_DRONES request handled. Sent: %s", response);
    } else {
        char error_response[BUFFER_SIZE];
        snprintf(error_response, BUFFER_SIZE, "UNKNOWN_REQUEST,Unknown command: %s\n", command_type);
        send_response(client_sock, error_response);
        close(client_sock);
        LOGGER_DEBUG("Unknown initial command: %s", command_type);
    }

    free(args);
    return NULL;
}

void start_simulator_server(int port) {
    int server_sock, client_sock;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_size;
    pthread_t tid;

    init_simulator_state();

    server_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (server_sock < 0) {
        perror("");
        exit(EXIT_FAILURE);
    }
    printf("Socket do servidor criado com sucesso.\n");

    int optval = 1;
    if (setsockopt(server_sock, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval)) < 0) {
        perror("");
    }

    memset(&server_addr, '\0', sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    server_addr.sin_addr.s_addr = INADDR_ANY;

    if (bind(server_sock, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("");
        close(server_sock);
        exit(EXIT_FAILURE);
    }
    printf("Socket ligado na porta %d.\n", port);

    if (listen(server_sock, MAX_CLIENTS) < 0) {
        perror("");
        close(server_sock);
        exit(EXIT_FAILURE);
    }
    printf("Servidor escutando por conexões...\n");

    while (1) {
        addr_size = sizeof(client_addr);
        client_sock = accept(server_sock, (struct sockaddr*)&client_addr, &addr_size);
        if (client_sock < 0) {
            perror("");
            continue;
        }

        client_handler_args *args = (client_handler_args *)malloc(sizeof(client_handler_args));
        if (args == NULL) {
            perror("");
            close(client_sock);
            continue;
        }
        args->client_sock = client_sock;
        args->client_addr = client_addr;

        if (pthread_create(&tid, NULL, client_handler_thread, (void *)args) != 0) {
            perror("");
            close(client_sock);
            free(args);
        }
        pthread_detach(tid);
        args->tid = tid;
    }

    close(server_sock);
    cleanup_simulator_state();
}
