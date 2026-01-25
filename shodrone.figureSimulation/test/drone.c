#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "types.h"
#include "drone.h"

// Implementation of load_drone_routine function
int load_drone_routine(const char *filepath, Drone *drone) {
    printf("[Debug] Attempting to load drone from: %s\n", filepath);
    
    FILE *file = fopen(filepath, "r");
    if (!file) {
        printf("[Error] Cannot open file %s\n", filepath);
        perror("fopen");
        return -1;
    }
    
    drone->duration = 0;
    drone->current_step = 0;
    drone->active = 1;
    
    char line[256];
    int line_number = 0;
    int valid_lines = 0;
    
    printf("[Debug] Reading file content...\n");
    while (fgets(line, sizeof(line), file) && drone->duration < MAX_STEPS) {
        line_number++;
        printf("[Debug] Line %d: %s", line_number, line);
        
        // Skip comments and empty lines
        if (line[0] == '#' || line[0] == '\n' || line[0] == '\r' || line[0] == ' ') {
            printf("[Debug] Skipping comment/empty line\n");
            continue;
        }
        
        int x, y, z, duration = 100; // Default duration 100ms
        
        // Try to parse with duration, fallback to without
        int parsed = sscanf(line, "%d,%d,%d,%d", &x, &y, &z, &duration);
        if (parsed < 3) {
            // Try different format (space-separated)
            parsed = sscanf(line, "%d %d %d %d", &x, &y, &z, &duration);
        }
        
        if (parsed >= 3) {
            printf("[Debug] Parsed coordinates: (%d,%d,%d) duration=%d\n", x, y, z, duration);
            
            // Validate coordinates
            if (x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y && z >= 0 && z < MAX_Z) {
                drone->movements[drone->duration].x = x;
                drone->movements[drone->duration].y = y;
                drone->movements[drone->duration].z = z;
                drone->movements[drone->duration].duration_ms = duration;
                drone->duration++;
                valid_lines++;
                printf("[Debug] Added movement step %d\n", drone->duration - 1);
            } else {
                printf("[Warning] Coordinates out of bounds: (%d,%d,%d). Max bounds: (%d,%d,%d)\n", 
                       x, y, z, MAX_X-1, MAX_Y-1, MAX_Z-1);
            }
        } else {
            printf("[Warning] Could not parse line %d: %s", line_number, line);
        }
    }
    
    fclose(file);
    
    printf("[Debug] File processing complete. Total lines: %d, Valid movements: %d\n", 
           line_number, valid_lines);
    
    if (drone->duration > 0) {
        printf("[Debug] Successfully loaded drone with %d movement steps\n", drone->duration);
        return 0;
    } else {
        printf("[Error] No valid movements found in file\n");
        return -1;
    }
}

void print_drone_info(const Drone *drone) {
    printf("Drone %d (%s): %d movements\n", drone->id, drone->name, drone->duration);
    for (int i = 0; i < drone->duration && i < 5; i++) {
        printf("  Step %d: (%d,%d,%d) for %dms\n", i,
               drone->movements[i].x, drone->movements[i].y, drone->movements[i].z,
               drone->movements[i].duration_ms);
    }
    if (drone->duration > 5) {
        printf("  ... and %d more steps\n", drone->duration - 5);
    }
}

int validate_drone_path(const Drone *drone) {
    for (int i = 0; i < drone->duration; i++) {
        Movement *m = &drone->movements[i];
        if (m->x < 0 || m->x >= MAX_X || 
            m->y < 0 || m->y >= MAX_Y || 
            m->z < 0 || m->z >= MAX_Z) {
            printf("[Validation Error] Step %d has invalid coordinates: (%d,%d,%d)\n", 
                   i, m->x, m->y, m->z);
            return -1;
        }
    }
    return 0;
}