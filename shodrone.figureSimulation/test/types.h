/* types.h
   Type definitions for drone simulation system */

#ifndef TYPES_H
#define TYPES_H

// System constants
#define MAX_DRONES 50
#define MAX_STEPS 1000
#define MAX_TIME 10000
#define MAX_X 100
#define MAX_Y 100
#define MAX_Z 50

// Movement structure
typedef struct {
    int x, y, z;
    int duration_ms;  // Time to spend at this position
} Movement;

// Drone structure
typedef struct {
    int id;
    char name[64];
    Movement movements[MAX_STEPS];
    int duration;     // Number of movement steps
    int current_step;
    int active;
} Drone;

#endif // TYPES_H