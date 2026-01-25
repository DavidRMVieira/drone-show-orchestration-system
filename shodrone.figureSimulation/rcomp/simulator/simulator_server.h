#ifndef SIMULATOR_SERVER_H
#define SIMULATOR_SERVER_H

#include <pthread.h>

/**
 * @brief Inicia o servidor TCP do simulador.
 * @param port A porta na qual o servidor irá escutar.
 */
void start_simulator_server(int port);

/**
 * @brief Função para lidar com o cliente da Testing App.
 * Esta função bloqueia até que a simulação seja concluída.
 * @param client_sock O socket do cliente da Testing App.
 * @param request_str A string da requisição TEST_SHOW.
 */
void handle_testing_app_client(int client_sock, const char* request_str);

/**
 * @brief Função para lidar com um cliente Drone Runner.
 * Esta função bloqueia até que a simulação comece e então envia comandos para o drone.
 * @param client_sock O socket do cliente Drone Runner.
 * @param request_str A string da requisição DRONE_INIT.
 * @param tid O ID da thread do drone.
 */
void handle_drone_runner_client(int client_sock, const char* request_str, pthread_t tid);

#endif // SIMULATOR_SERVER_H

