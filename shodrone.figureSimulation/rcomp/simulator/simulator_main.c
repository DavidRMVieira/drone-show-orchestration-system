#include "simulator_server.h"
#include <stdio.h>
#include <stdlib.h>

#define DEFAULT_SIMULATOR_PORT 10005 // A porta padrão para o Simulator

/**
 * @brief Função principal para iniciar o servidor do simulador.
 *
 * Este programa inicia o servidor TCP que escuta por requisições da Testing App
 * e conexões dos Drone Runners.
 *
 * @param argc O número de argumentos da linha de comando.
 * @param argv Os argumentos da linha de comando (opcionalmente, a porta).
 * @return EXIT_SUCCESS em caso de sucesso, EXIT_FAILURE em caso de erro.
 */
int main(int argc, char *argv[]) {
    int port = DEFAULT_SIMULATOR_PORT;

    // Permitir que a porta seja especificada como um argumento da linha de comando
    if (argc > 1) {
        port = atoi(argv[1]);
        if (port <= 0 || port > 65535) {
            fprintf(stderr, "Usage: %s [port]\n", argv[0]);
            fprintf(stderr, "Port must be a number between 1 and 65535.\n");
            return EXIT_FAILURE;
        }
    }

    printf("Starting Simulator server on port %d...\n", port);
    start_simulator_server(port);

    return EXIT_SUCCESS;
}
