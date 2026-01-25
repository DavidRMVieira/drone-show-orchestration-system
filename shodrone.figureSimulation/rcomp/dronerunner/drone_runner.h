#ifndef DRONE_RUNNER_H
#define DRONE_RUNNER_H

#include <stdbool.h>

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
bool main_drone_ui(int drone_id);

#endif // DRONE_RUNNER_H
