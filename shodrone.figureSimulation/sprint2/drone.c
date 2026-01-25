#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "types.h"
#include "drone.h"

int load_drone_routine(const char *filepath, Drone *drone) {
    FILE *file = fopen(filepath, "r");
    if (!file) {
        perror("Failed to open movement file");
        return -1;
    }

    // Primeiro, contar quantas linhas (tempo total)
    int count = 0;
    char line[128];
    while (fgets(line, sizeof(line), file)) {
        if (strlen(line) > 1) count++;
    }

    rewind(file);  // Voltar ao início do ficheiro

    // Alocar memória para a rotina
    drone->movements = malloc(sizeof(Position) * count);
    if (!drone->movements) {
        perror("Memory allocation failed");
        fclose(file);
        return -1;
    }

    drone->duration = count;

    // Preencher a rotina
    int i = 0;
    while (fgets(line, sizeof(line), file)) {
        if (sscanf(line, "%d %d %d", &drone->movements[i].x, &drone->movements[i].y, &drone->movements[i].z) == 3) {
            i++;
        }
    }

    fclose(file);
    return 0;
}
