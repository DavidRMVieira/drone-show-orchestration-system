#define _POSIX_C_SOURCE 200809L
#include "csv_server_response_parser.h"
#include "common.h" // Para LOGGER_DEBUG
#include <stdlib.h> // Para malloc, free, realloc
#include <string.h> // Para strdup, strtok

/**
 * @brief Aloca e inicializa uma estrutura ServerResponse.
 * @return Um ponteiro para a nova estrutura ServerResponse, ou NULL em caso de falha.
 */
ServerResponse* create_server_response() {
    ServerResponse* response = (ServerResponse*)malloc(sizeof(ServerResponse));
    if (response == NULL) {
        LOGGER_DEBUG("Erro ao alocar ServerResponse.");
        return NULL;
    }
    response->messages = NULL;
    response->count = 0;
    return response;
}

/**
 * @brief Adiciona uma mensagem (string) à estrutura ServerResponse.
 * @param response Ponteiro para a estrutura ServerResponse.
 * @param message A string a ser adicionada.
 * @return 0 em caso de sucesso, -1 em caso de falha de alocação.
 */
int add_message_to_response(ServerResponse* response, const char* message) {
    if (response == NULL || message == NULL) {
        LOGGER_DEBUG("Erro: ServerResponse ou message é NULL.");
        return -1;
    }

    // Aumenta o tamanho do array de ponteiros para string
    char** new_messages = (char**)realloc(response->messages, (response->count + 1) * sizeof(char*));
    if (new_messages == NULL) {
        LOGGER_DEBUG("Erro ao realocar mensagens para ServerResponse.");
        return -1;
    }
    response->messages = new_messages;

    // Duplica a string e armazena o ponteiro no array
    response->messages[response->count] = strdup(message);
    if (response->messages[response->count] == NULL) {
        LOGGER_DEBUG("Erro ao duplicar string para ServerResponse.");
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
    free(response->messages); // Libera o array de ponteiros
    free(response); // Libera a própria estrutura
}

/**
 * @brief Verifica se a primeira linha da resposta contém uma mensagem de erro do servidor.
 * @param response Ponteiro para a estrutura ServerResponse.
 * @return Um código de erro FailedRequestException_t. FR_SUCCESS se não houver erro,
 * ou um código de erro específico se um erro for detectado.
 */
static FailedRequestException_t check_for_error_message(const ServerResponse* response) {
    if (response == NULL || response->count == 0 || response->messages[0] == NULL) {
        LOGGER_DEBUG("Resposta do servidor inválida ou vazia.");
        return FR_SERVER_ERROR; // Tratado como erro do servidor se não houver resposta
    }

    // Cria uma cópia da string para poder modificá-la com strtok
    char* first_line_copy = strdup(response->messages[0]);
    if (first_line_copy == NULL) {
        LOGGER_DEBUG("Erro ao duplicar a primeira linha da resposta.");
        return FR_GENERIC_ERROR;
    }

    char* token = strtok(first_line_copy, ",");
    if (token == NULL) {
        free(first_line_copy);
        return FR_SUCCESS; // Não é um formato de erro conhecido
    }

    FailedRequestException_t error_code = FR_SUCCESS;

    if (strcmp(token, "SERVER_ERROR") == 0) {
        error_code = FR_SERVER_ERROR;
    } else if (strcmp(token, "BAD_REQUEST") == 0) {
        error_code = FR_BAD_REQUEST;
    } else if (strcmp(token, "UNKNOWN_REQUEST") == 0) {
        error_code = FR_UNKNOWN_REQUEST;
    } else if (strcmp(token, "ERROR_IN_REQUEST") == 0) {
        error_code = FR_ERROR_IN_REQUEST;
    }

    free(first_line_copy); // Libera a cópia
    return error_code;
}

/**
 * @brief Analisa a mensagem de resposta do servidor para o comando "TEST_SHOW".
 *
 * Esta função recebe uma lista de strings (representando as linhas da resposta do servidor)
 * e verifica se há mensagens de erro de protocolo (SERVER_ERROR, BAD_REQUEST, etc.).
 *
 * @param response Um ponteiro para uma lista de strings recebidas do servidor.
 * @return Um código de erro FailedRequestException_t. FR_SUCCESS se a resposta for válida,
 * ou um código de erro específico se uma mensagem de erro for detectada.
 * Se `response` for NULL, retorna FR_SERVER_ERROR.
 */
FailedRequestException_t parse_response_message_test_show(const ServerResponse* response) {
    if (response == NULL || response->count == 0) {
        LOGGER_DEBUG("Resposta nula ou vazia do servidor.");
        return FR_SERVER_ERROR; // Equivalente a Arrays.asList("SERVER_ERROR: No response from server", "Please try again later")
    }

    // Verifica se a primeira linha da resposta indica um erro de protocolo
    FailedRequestException_t error = check_for_error_message(response);
    if (error != FR_SUCCESS) {
        LOGGER_DEBUG("Erro detectado na resposta do servidor: %s", get_failed_request_message(error));
    }

    return error; // Retorna o código de erro (ou FR_SUCCESS se não houver erro)
}
