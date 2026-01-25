#ifndef SIMULATOR_STATE_H
#define SIMULATOR_STATE_H

#include <stdbool.h>
#include <pthread.h>
#include "simulator_protocol.h" // For TestShowRequest

// Maximum number of drone runners the simulator can handle simultaneously for a single test
// This should match NUM_DRONES in drone_runner_main.c
#define MAX_DRONES_PER_SIMULATION 3

typedef struct {
    // Estado para comunicação com a Testing App
    bool test_show_received;
    TestShowRequest current_test_show_request;
    int testing_app_response_sock; // Socket para enviar a resposta final de volta para a Testing App

    // Estado para comunicação com os Drone Runners
    // Nota: Usamos indexação 1-base para IDs de drones, então o tamanho do array é MAX_DRONES_PER_SIMULATION + 1
    bool drone_runner_connected[MAX_DRONES_PER_SIMULATION + 1];
    int drone_runner_socks[MAX_DRONES_PER_SIMULATION + 1];
    int num_drones_expected; // Do pedido TEST_SHOW
    int num_drones_ready; // Contagem de drones conectados para a simulação atual

    // Flag para indicar se a simulação principal está em andamento.
    // Usada para evitar que múltiplos threads de drones tentem iniciar a simulação simultaneamente.
    bool simulation_active;

    // Sincronização
    pthread_mutex_t mutex;
    pthread_cond_t simulation_ready_cond;    // Sinaliza quando TestShow é recebido E todos os drones esperados estão conectados
    pthread_cond_t simulation_complete_cond; // Sinaliza quando a simulação termina (pelo último thread de drone a concluir sua parte)

} SimulatorGlobalState;

extern SimulatorGlobalState g_simulator_state;

void init_simulator_state();
void cleanup_simulator_state();

#endif // SIMULATOR_STATE_H

