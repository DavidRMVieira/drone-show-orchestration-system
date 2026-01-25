#include "simulator_state.h"
#include <stdio.h>
#include <stdlib.h>

SimulatorGlobalState g_simulator_state;

void init_simulator_state() {
    g_simulator_state.test_show_received = false;
    g_simulator_state.testing_app_response_sock = -1;
    g_simulator_state.num_drones_expected = 0;
    g_simulator_state.num_drones_ready = 0;
    g_simulator_state.simulation_active = false;

    for (int i = 0; i <= MAX_DRONES_PER_SIMULATION; i++) {
        g_simulator_state.drone_runner_connected[i] = false;
        g_simulator_state.drone_runner_socks[i] = -1;
    }

    if (pthread_mutex_init(&g_simulator_state.mutex, NULL) != 0) {
        perror("");
        exit(EXIT_FAILURE);
    }
    if (pthread_cond_init(&g_simulator_state.simulation_ready_cond, NULL) != 0) {
        perror("");
        exit(EXIT_FAILURE);
    }
    if (pthread_cond_init(&g_simulator_state.simulation_complete_cond, NULL) != 0) {
        perror("");
        exit(EXIT_FAILURE);
    }

    printf("Simulator global state initialized.\n");
}

void cleanup_simulator_state() {
    pthread_mutex_destroy(&g_simulator_state.mutex);
    pthread_cond_destroy(&g_simulator_state.simulation_ready_cond);
    pthread_cond_destroy(&g_simulator_state.simulation_complete_cond);

    printf("Simulator global state cleaned up.\n");
}
