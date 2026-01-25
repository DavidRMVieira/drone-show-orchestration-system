#include "common.h" // Para LOGGER_DEBUG
#include "ui_utils.h"
#include "csv_simulator_protocol_proxy.h"
#include "failed_request_exception.h" // Para get_failed_request_message
#include <stdio.h>
#include <stdlib.h> // Para exit, system
#include <string.h> // Para strcmp, toupper

/**
 * @brief Exibe a interface de usuário principal para o Drone Runner.
 *
 * Esta função inicia a conexão e o protocolo de comunicação com o simulador,
 * atuando como um drone individual.
 *
 * @param drone_id O ID do drone selecionado pelo utilizador.
 * @return true se a operação for concluída com sucesso (simulação terminada sem erros fatais),
 * false em caso de erros que impeçam a comunicação ou conclusão.
 */
bool main_drone_ui(int drone_id) {
    printf("\n=== Drone Runner (ID: %d) ===\n", drone_id);

    // Chama a função que gerencia a comunicação com o simulador, passando o ID do drone
    FailedRequestException_t error = drone_runner_protocol_exchange(drone_id);

    if (error != FR_SUCCESS) {
        printf("Problems communicating with the simulator: %s\n", get_failed_request_message(error));
        LOGGER_DEBUG("Erro ao executar protocolo do drone: %s", get_failed_request_message(error));
        return false;
    } else {
        printf("Simulation for Drone %d completed.\n", drone_id);
        LOGGER_DEBUG("Protocolo do drone executado com sucesso.");
        return true;
    }
}
