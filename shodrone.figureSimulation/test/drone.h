#ifndef DRONE_H
#define DRONE_H

#include "types.h"

// Function prototypes
int load_drone_routine(const char *filepath, Drone *drone);
void print_drone_info(const Drone *drone);
int validate_drone_path(const Drone *drone);

#endif
