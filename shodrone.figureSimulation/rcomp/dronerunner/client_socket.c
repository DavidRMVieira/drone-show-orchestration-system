#include "client_socket.h"
#include "common.h" // Para LOGGER_DEBUG
#include <stdio.h>    // Para printf, snprintf
#include <stdlib.h>   // Para malloc, free
#include <string.h>   // Para strlen, strncpy
#include <unistd.h>   // Para close
#include <errno.h>    // Para errno
#include <netdb.h>    // Para getaddrinfo

// Flag para controlar se a mensagem de conexão já foi mostrada
static bool server_ip_connect_is_show = false;


/**
 * @brief Inicializa uma nova instância de ClientSocket.
 * @return Um ponteiro para a ClientSocket alocada, ou NULL em caso de falha.
 */
ClientSocket* client_socket_create() {
    ClientSocket* cs = (ClientSocket*)malloc(sizeof(ClientSocket));
    if (cs == NULL) {
        LOGGER_DEBUG("Erro ao alocar ClientSocket.");
        return NULL;
    }
    cs->sock_fd = -1;
    cs->connected = false;
    return cs;
}

/**
 * @brief Conecta-se ao servidor no endereço e porta especificados.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 * @param address Endereço IP ou hostname do servidor.
 * @param port Número da porta para conectar.
 * @return 0 em caso de sucesso, -1 em caso de erro.
 */
int client_socket_connect(ClientSocket* client_socket, const char* address, int port) {
    if (client_socket == NULL || address == NULL) {
        LOGGER_DEBUG("Erro: client_socket ou address é NULL.");
        return -1;
    }

    client_socket->sock_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (client_socket->sock_fd < 0) {
        perror("Erro ao criar socket");
        LOGGER_DEBUG("Erro ao criar socket: %s", strerror(errno));
        return -1;
    }

    struct addrinfo hints, *res = NULL;
    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;

    char port_str[16];
    snprintf(port_str, sizeof(port_str), "%d", port);

    int gai_ret = getaddrinfo(address, port_str, &hints, &res);
    if (gai_ret != 0) {
        fprintf(stderr, "getaddrinfo failed: %s\n", gai_strerror(gai_ret));
        LOGGER_DEBUG("getaddrinfo falhou para o endereço: %s. Erro: %s", address, gai_strerror(gai_ret));
        close(client_socket->sock_fd);
        client_socket->sock_fd = -1;
        return -1;
    }

    LOGGER_DEBUG("Tentando conectar a %s:%d...", address, port);
    if (connect(client_socket->sock_fd, res->ai_addr, res->ai_addrlen) < 0) {
        perror("Erro ao conectar");
        LOGGER_DEBUG("Erro ao conectar: %s", strerror(errno));
        freeaddrinfo(res);
        close(client_socket->sock_fd);
        client_socket->sock_fd = -1;
        return -1;
    }

    freeaddrinfo(res);
    client_socket->connected = true;
    if (!server_ip_connect_is_show) {
        server_ip_connect_is_show = true;
        LOGGER_DEBUG("Conectado a %s", address);
    }
    return 0; // Sucesso
}

/**
 * @brief Envia uma requisição (string) para o servidor.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 * @param request A string da requisição a ser enviada.
 * @return Número de bytes enviados em caso de sucesso, -1 em caso de erro.
 */
ssize_t client_socket_send(ClientSocket* client_socket, const char* request) {
    if (client_socket == NULL || !client_socket->connected || request == NULL) {
        LOGGER_DEBUG("Erro: Socket não conectado ou requisição NULL.");
        return -1;
    }

    // Adiciona uma nova linha ao final da requisição para sinalizar o fim da linha para o servidor CSV.
    // O servidor Java usa println, que adiciona automaticamente. Em C, precisamos adicionar explicitamente.
    char request_with_newline[CLIENT_BUFFER_SIZE];
    int len = snprintf(request_with_newline, CLIENT_BUFFER_SIZE, "%s\n", request);

    if (len < 0 || (size_t)len >= CLIENT_BUFFER_SIZE) {
        LOGGER_DEBUG("Erro: Requisição muito longa para o buffer ou erro de snprintf.");
        return -1;
    }

    ssize_t bytes_sent = send(client_socket->sock_fd, request_with_newline, len, 0);
    if (bytes_sent < 0) {
        perror("Erro ao enviar dados");
        LOGGER_DEBUG("Erro ao enviar dados: %s", strerror(errno));
    }
    LOGGER_DEBUG("Dados enviados: %s", request_with_newline);
    return bytes_sent;
}

/**
 * @brief Recebe uma resposta do servidor.
 *
 * A resposta é lida linha por linha até que uma linha vazia seja recebida,
 * o que indica o fim da mensagem (conforme o protocolo CSV).
 * As linhas são armazenadas em uma estrutura ServerResponse.
 *
 * @param client_socket Ponteiro para a instância de ClientSocket.
 * @return Um ponteiro para uma estrutura ServerResponse contendo as mensagens recebidas,
 * ou NULL em caso de erro de I/O. A memória para ServerResponse deve ser liberada
 * pelo chamador usando free_server_response().
 */
ServerResponse* client_socket_recv(ClientSocket* client_socket) {
    if (client_socket == NULL || !client_socket->connected) {
        LOGGER_DEBUG("Erro: Socket não conectado.");
        return NULL;
    }

    ServerResponse* resp = create_server_response();
    if (resp == NULL) {
        return NULL; // Falha na alocação
    }

    char buffer[CLIENT_BUFFER_SIZE];
    ssize_t bytes_read;
    bool eof_protocol_marker_received = false; // Sinaliza que a linha vazia foi recebida

    while (!eof_protocol_marker_received) {
        char line_buffer[CLIENT_BUFFER_SIZE];
        size_t line_len = 0;
        bool newline_found = false;

        // Leitura byte a byte até encontrar um newline ou encher o buffer
        while (line_len < CLIENT_BUFFER_SIZE - 1) {
            bytes_read = recv(client_socket->sock_fd, buffer, 1, 0); // Lê um byte por vez
            if (bytes_read <= 0) {
                // Erro ou conexão fechada
                if (bytes_read == 0) {
                    LOGGER_DEBUG("Conexão do servidor fechada inesperadamente ou EOF.");
                } else {
                    perror("Erro ao receber dados");
                    LOGGER_DEBUG("Erro ao receber dados: %s", strerror(errno));
                }
                free_server_response(resp); // Libera memória alocada parcialmente
                return NULL;
            }

            if (buffer[0] == '\n') {
                newline_found = true;
                break; // Fim da linha
            }
            line_buffer[line_len++] = buffer[0];
        }
        line_buffer[line_len] = '\0'; // Termina a string

        LOGGER_DEBUG("Linha recebida: '%s'", line_buffer);

        if (newline_found && line_len == 0) {
            // Linha vazia recebida, significa EOF no protocolo CSV
            eof_protocol_marker_received = true;
        } else {
            // Adiciona a linha lida (sem o \n) à lista de respostas
            if (add_message_to_response(resp, line_buffer) != 0) {
                LOGGER_DEBUG("Erro ao adicionar mensagem à resposta.");
                free_server_response(resp);
                return NULL;
            }
        }
    }

    return resp;
}

/**
 * @brief Envia uma requisição e recebe a resposta do servidor.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 * @param request A string da requisição a ser enviada.
 * @return Um ponteiro para uma estrutura ServerResponse contendo as mensagens recebidas,
 * ou NULL em caso de erro de I/O. A memória para ServerResponse deve ser liberada
 * pelo chamador usando free_server_response().
 */
ServerResponse* client_socket_send_and_recv(ClientSocket* client_socket, const char* request) {
    if (client_socket_send(client_socket, request) < 0) {
        return NULL; // Erro ao enviar
    }
    return client_socket_recv(client_socket); // Retorna o resultado do recebimento
}

/**
 * @brief Para o socket cliente, fechando os streams de entrada/saída e o socket.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 */
void client_socket_stop(ClientSocket* client_socket) {
    if (client_socket != NULL && client_socket->sock_fd != -1) {
        LOGGER_DEBUG("Fechando socket %d...", client_socket->sock_fd);
        close(client_socket->sock_fd);
        client_socket->sock_fd = -1;
        client_socket->connected = false;
        LOGGER_DEBUG("Socket fechado.");
    }
}

/**
 * @brief Libera a memória alocada para a instância de ClientSocket.
 * @param client_socket Ponteiro para a instância de ClientSocket a ser liberada.
 */
void client_socket_destroy(ClientSocket* client_socket) {
    if (client_socket != NULL) {
        client_socket_stop(client_socket); // Garante que o socket esteja fechado
        free(client_socket);
        LOGGER_DEBUG("ClientSocket destruído.");
    }
}
