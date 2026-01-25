#ifndef CSV_SIMULATOR_PROTOCOL_PROXY_H
#define CSV_SIMULATOR_PROTOCOL_PROXY_H

#include "failed_request_exception.h"
#include <stdbool.h>

// Endereço IP e porta do simulador
#define SERVERIP "vs903.dei.isep.ipp.pt" // Pode ser "localhost" ou o IP da máquina do simulador
#define SERVERPORT 10005     // A porta que o simulador escuta

/**
 * @brief Gerencia o protocolo de comunicação entre o Drone Runner e o Simulator.
 *
 * Esta função estabelece uma conexão com o servidor do simulador, envia o ID do drone,
 * e então entra em um loop para receber comandos do simulador e imprimir.
 * O loop continua até que o comando "END_SIMULATION" seja recebido.
 *
 * @param drone_id O ID único do drone que está se conectando.
 * @return Um código de erro FailedRequestException_t. Retorna FR_SUCCESS em caso de sucesso
 * na comunicação (mesmo que a simulação termine), ou um código de erro apropriado em caso
 * de falha de I/O, conexão, ou erro de protocolo.
 */
FailedRequestException_t drone_runner_protocol_exchange(int drone_id);

#endif // CSV_SIMULATOR_PROTOCOL_PROXY_H
