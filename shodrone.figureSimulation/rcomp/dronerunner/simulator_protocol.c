#define _POSIX_C_SOURCE 200809L

#include "simulator_protocol.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h> // Adicionado para strdup e strtok_r


/**
 * @brief Analisa uma string de requisição "TEST_SHOW" e preenche a estrutura TestShowRequest.
 *
 * A string de entrada deve estar no formato: "TEST_SHOW, <duration>, <numberOfDrones>, <latitude>, <longitude>"
 *
 * @param request_str A string contendo a requisição.
 * @param request Um ponteiro para a estrutura TestShowRequest a ser preenchida.
 * @return true se a análise for bem-sucedida, false caso contrário.
 */
bool parse_test_show_request(const char *request_str, TestShowRequest *request) {
    if (request_str == NULL || request == NULL) {
        return false;
    }

    char *copy = strdup(request_str); // Criar uma cópia para não modificar a string original
    if (copy == NULL) {
        perror("Erro ao duplicar string");
        return false;
    }

    char *token;
    char *rest = copy;

    // O primeiro token deve ser "TEST_SHOW"
    token = strtok_r(rest, ",", &rest);
    if (token == NULL || strcmp(token, "TEST_SHOW") != 0) {
        free(copy);
        return false;
    }

    // Duração
    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    request->duration = atoi(token);

    // Número de drones
    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    request->numberOfDrones = atoi(token);

    // Latitude
    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    request->latitude = atof(token);

    // Longitude
    token = strtok_r(rest, ",", &rest);
    if (token == NULL) {
        free(copy);
        return false;
    }
    request->longitude = atof(token);

    free(copy); // Liberar a memória da cópia
    return true;
}

/**
 * @brief Parses a "DRONE_INIT" request string.
 * Format: "DRONE_INIT,<drone_id>"
 * @param request_str The full request string.
 * @param drone_id Pointer to an int to store the drone ID.
 * @return true if parsing is successful, false otherwise.
 */
bool parse_drone_init_request(const char *request_str, int *drone_id) {
    if (request_str == NULL || drone_id == NULL) {
        return false;
    }

    char *copy = strdup(request_str);
    if (copy == NULL) {
        perror("Erro ao duplicar string");
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
    *drone_id = atoi(token);

    free(copy);
    return true;
}

// --- Implementações das funções de ServerResponse management ---
// (Estas implementações também serão incluídas em client_socket.c para o lado do cliente)
// A duplicação é intencional para manter a modularidade e permitir compilação independente.

/**
 * @brief Aloca e inicializa uma estrutura ServerResponse.
 * @return Um ponteiro para a nova estrutura ServerResponse, ou NULL em caso de falha.
 */
ServerResponse* create_server_response() {
    ServerResponse* response = (ServerResponse*)malloc(sizeof(ServerResponse));
    if (response == NULL) {
        perror("Erro ao alocar ServerResponse");
        return NULL;
    }
    response->count = 0;
    for (size_t i = 0; i < MAX_MESSAGES_IN_RESPONSE; i++) {
        response->messages[i] = NULL;
    }
    return response;
}

/**
 * @brief Adiciona uma mensagem (string) à estrutura ServerResponse.
 * @param response Ponteiro para a estrutura ServerResponse.
 * @param message A string a ser adicionada.
 * @return 0 em caso de sucesso, -1 em caso de falha de alocação ou se o limite for atingido.
 */
int add_message_to_response(ServerResponse* response, const char* message) {
    if (response == NULL || message == NULL) {
        return -1;
    }
    if (response->count >= MAX_MESSAGES_IN_RESPONSE) {
        return -1; // Limite atingido
    }

    response->messages[response->count] = strdup(message);
    if (response->messages[response->count] == NULL) {
        perror("Erro ao duplicar string para ServerResponse");
        return -1;
    }
    response->count++;
    return 0;
}

/**
 * @brief Libera a memória alocada para uma estrutura ServerResponse.
 * @param response Ponteiro para a estrutura ServerResponse a ser liberada.
 */
void free_server_response(ServerResponse* response) {
    if (response == NULL) {
        return;
    }
    for (size_t i = 0; i < response->count; i++) {
        free(response->messages[i]); // Libera cada string
    }
    free(response); // Libera a própria estrutura
}
