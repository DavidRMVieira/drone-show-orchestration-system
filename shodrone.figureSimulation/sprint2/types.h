#ifndef TYPES_H
#define TYPES_H

#define MAX_TIME 1000
#define MAX_X 100
#define MAX_Y 100
#define MAX_Z 100
#define EMPTY -1
#define MAX_DRONES 100
#define MAX_COLLISIONS 1
#define FIRST_LEVEL_WIND 10
#define SECOND_LEVEL_WIND 20

typedef struct {
    int x;
    int y;
    int z;
} Position;

typedef struct {
    int id;
    char name[50];
    Position *movements;  // Array of positions per second
    int duration;       // Total routine duration
} Drone;

typedef enum { 
	SUCESS, 
	COLLISION, 
	OUT_OF_BOUNDS 
} EstadoDrone;

typedef struct {
	int time;
	Position position;
	char *dronesUsed[2];
} Collision;

typedef struct {
	int velocity;
	char direction;
} Wind;

#endif
