// client_socket.h
#ifndef CLIENT_SOCKET_H
#define CLIENT_SOCKET_H

#include <sys/socket.h> // Para socket, connect, send, recv
#include <netinet/in.h> // Para sockaddr_in, htons, INADDR_ANY
#include <arpa/inet.h>  // Para inet_pton, inet_ntoa
#include <stdbool.h>    // Para bool
#include <stddef.h>     // Para ssize_t

#include "simulator_protocol.h" // Inclui a definição de ServerResponse
#include "failed_request_exception.h" // Para FailedRequestException_t

// Tamanho máximo do buffer para mensagens do cliente/servidor
#define CLIENT_BUFFER_SIZE 1024

// Estrutura para representar um socket cliente
typedef struct {
    int sock_fd;
    struct sockaddr_in server_addr;
    bool connected;
} ClientSocket;

/**
 * @brief Inicializa uma nova instância de ClientSocket.
 * @return Um ponteiro para a ClientSocket alocada, ou NULL em caso de falha.
 */
ClientSocket* client_socket_create();

/**
 * @brief Conecta-se ao servidor no endereço e porta especificados.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 * @param address Endereço IP ou hostname do servidor.
 * @param port Número da porta para conectar.
 * @return 0 em caso de sucesso, -1 em caso de erro.
 */
int client_socket_connect(ClientSocket* client_socket, const char* address, int port);

/**
 * @brief Envia uma requisição (string) para o servidor.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 * @param request A string da requisição a ser enviada.
 * @return Número de bytes enviados em caso de sucesso, -1 em caso de erro.
 */
ssize_t client_socket_send(ClientSocket* client_socket, const char* request);

/**
 * @brief Recebe uma resposta do servidor.
 * A resposta é lida linha por linha até que uma linha vazia seja recebida,
 * o que indica o fim da mensagem (conforme o protocolo CSV).
 * As linhas são armazenadas em uma estrutura ServerResponse.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 * @return Um ponteiro para uma estrutura ServerResponse contendo as mensagens recebidas,
 * ou NULL em caso de erro de I/O. A memória para ServerResponse deve ser liberada
 * pelo chamador usando free_server_response().
 */
ServerResponse* client_socket_recv(ClientSocket* client_socket);

/**
 * @brief Envia uma requisição e recebe a resposta do servidor.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 * @param request A string da requisição a ser enviada.
 * @return Um ponteiro para uma estrutura ServerResponse contendo as mensagens recebidas,
 * ou NULL em caso de erro de I/O. A memória para ServerResponse deve ser liberada
 * pelo chamador usando free_server_response().
 */
ServerResponse* client_socket_send_and_recv(ClientSocket* client_socket, const char* request);

/**
 * @brief Para o socket cliente, fechando os streams de entrada/saída e o socket.
 * @param client_socket Ponteiro para a instância de ClientSocket.
 */
void client_socket_stop(ClientSocket* client_socket);

/**
 * @brief Libera a memória alocada para a instância de ClientSocket.
 * @param client_socket Ponteiro para a instância de ClientSocket a ser liberada.
 */
void client_socket_destroy(ClientSocket* client_socket);


#endif // CLIENT_SOCKET_H
