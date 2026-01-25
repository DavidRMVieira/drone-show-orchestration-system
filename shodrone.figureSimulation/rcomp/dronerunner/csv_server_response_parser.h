#ifndef CSV_SERVER_RESPONSE_PARSER_H
#define CSV_SERVER_RESPONSE_PARSER_H

#include "failed_request_exception.h"
#include <stdbool.h>
#include <stddef.h> // Para size_t

// Estrutura para representar a resposta do servidor.
// Em C, uma "lista de strings" pode ser um array de ponteiros para caracteres.
typedef struct {
    char** messages;
    size_t count;
} ServerResponse;

// Função para alocar e inicializar uma estrutura ServerResponse.
ServerResponse* create_server_response();

// Função para adicionar uma mensagem à ServerResponse.
// Retorna 0 em caso de sucesso, -1 em caso de erro (e.g., falha de alocação).
int add_message_to_response(ServerResponse* response, const char* message);

// Função para liberar a memória alocada para uma ServerResponse.
void free_server_response(ServerResponse* response);

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
FailedRequestException_t parse_response_message_test_show(const ServerResponse* response);

#endif // CSV_SERVER_RESPONSE_PARSER_H
