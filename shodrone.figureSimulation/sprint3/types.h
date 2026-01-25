/**
 * @file types.h
 * @brief Core data structure definitions and constants for drone simulation system
 * 
 * This header file contains all fundamental data types, structures, and constants
 * used throughout the multi-process drone simulation system. It defines the
 * 3D coordinate system, drone representation, collision detection structures,
 * environmental factors, and shared memory layout used for inter-process
 * communication.
 * 
 * The type system supports:
 * 1. 3D Spatial Representation: Position structures and coordinate bounds
 * 2. Drone Modeling: Complete drone state and movement trajectory definitions
 * 3. Collision Detection: Event structures for real-time collision monitoring
 * 4. Environmental Simulation: Wind effects and atmospheric conditions
 * 5. Inter-Process Communication: Shared memory structures for process coordination
 * 
 * @note Design Philosophy:
 *       All structures are designed for efficient memory layout and fast access
 *       patterns suitable for real-time simulation requirements. Fixed-size
 *       arrays are used where possible to avoid dynamic memory allocation
 *       in time-critical code paths.
 * 
 * @note Dependencies:
 *       - time.h: Required for timestamp functionality in collision events
 *       - Standard C types: int, char, and other primitive types
 * 
 * @note Coordinate System:
 *       The simulation uses a right-handed 3D coordinate system:
 *       - X-axis
 *       - Y-axis 
 *       - Z-axis
 * 
 * @note Thread Safety:
 *       All data structures are designed for safe concurrent access when
 *       used with appropriate synchronization primitives (semaphores, mutexes).
 *       No structure contains internal synchronization mechanisms.
 */

/*============================================================================*/
/*                              HEADER GUARD                                 */
/*============================================================================*/

#ifndef TYPES_H
#define TYPES_H

/*============================================================================*/
/*                                INCLUDES                                   */
/*============================================================================*/

/* Include time header for timestamp functionality in collision events
 * Required for time_t type definition used in CollisionEvent structure */
#include <time.h>

/*============================================================================*/
/*                          SIMULATION CONSTANTS                             */
/*============================================================================*/

/**
 * @brief Maximum simulation time in seconds
 * 
 * Defines the upper limit for simulation duration. Used for array sizing
 * and loop bounds in time-dependent calculations.
 * 
 * @note This constant affects:
 *       - Maximum drone routine duration validation
 *       - Simulation progress tracking upper bounds
 *       - Time-based array allocations in some contexts
 */
#define MAX_TIME 1000

/**
 * @brief Maximum X-coordinate boundary
 * 
 * Defines the X boundary of the simulation space. Valid X coordinates
 * range from 0 to (MAX_X - 1), creating a 100-unit wide simulation area.
 * 
 * @note Coordinate Validation:
 *       Any drone position with x >= MAX_X is considered out of bounds
 *       and will trigger boundary violation detection.
 */
#define MAX_X 100

/**
 * @brief Maximum Y-coordinate boundary (North-South extent)
 * 
 * Defines the Y boundary of the simulation space. Valid Y coordinates
 * range from 0 to (MAX_Y - 1), creating a 100-unit deep simulation area.
 * 
 * @note Coordinate Validation:
 *       Any drone position with y >= MAX_Y is considered out of bounds
 *       and will trigger boundary violation detection.
 */
#define MAX_Y 100

/**
 * @brief Maximum Z-coordinate boundary (Altitude limit)
 * 
 * Defines the maximum Z for drone operations. Valid Z coordinates
 * range from 0 to (MAX_Z - 1), where 0 represents ground level and
 * MAX_Z-1 represents the maximum safe operating altitude.
 * 
 * @note Coordinate Validation:
 *       Any drone position with z >= MAX_Z is considered out of bounds
 *       and will trigger boundary violation detection.
 */
#define MAX_Z 100

/**
 * @brief Sentinel value indicating empty or uninitialized positions
 * 
 * Used throughout the system to mark unoccupied positions in spatial
 * data structures and indicate uninitialized coordinate values.
 * 
 * @note Usage Patterns:
 *       - Spatial grid initialization: All cells set to EMPTY initially
 *       - Position validation: Check against EMPTY before processing
 *       - Collision detection: Skip cells marked as EMPTY
 */
#define EMPTY -1

/**
 * @brief Maximum number of concurrent drones in simulation
 * 
 * Defines the upper limit for drone count, used for static array sizing
 * in shared memory structures and collision detection systems.
 * 
 * @note Memory Impact:
 *       This constant directly affects:
 *       - Shared memory segment size calculations
 *       - Semaphore array dimensions
 *       - Collision detection matrix sizing
 *       - Performance characteristics of spatial algorithms
 * 
 * @note Scalability:
 *       Increasing this value requires careful consideration of:
 *       - Available system memory for shared segments
 *       - Semaphore system limits
 *       - Collision detection algorithm performance
 */
#define MAX_DRONES 100

/**
 * @brief Maximum number of simultaneous collisions per time step
 * 
 * Currently set to 1, indicating the system is designed to handle
 * one collision event per simulation time step. This constraint
 * simplifies collision resolution and reporting logic.
 * 
 * @note Design Limitation:
 *       The current value reflects a simplified collision model.
 *       Future versions may increase this limit to handle:
 *       - Multiple simultaneous collision events
 *       - Complex multi-drone collision scenarios
 *       - Chain reaction collision sequences
 * 
 * @note Implementation Impact:
 *       Affects collision event buffer sizing and processing algorithms
 *       in the collision detection subsystem.
 */
#define MAX_COLLISIONS 5

/**
 * @brief Wind velocity
 * 
 * --------
 * 
 * @note Implemented in Sprint 2, not yet used on Sprint 3
 * 
 */
#define FIRST_LEVEL_WIND 10

/**
 * @brief Wind velocity
 * 
 * --------
 * 
 * @note Implemented in Sprint 2, not yet used on Sprint 3
 * 
 */
#define SECOND_LEVEL_WIND 20

#define NUMBER_DIRECTIONS 4

/*============================================================================*/
/*                            CORE DATA STRUCTURES                           */
/*============================================================================*/

/**
 * @brief 3D coordinate position structure
 * 
 * Represents a point in 3D space using integer coordinates. This is the
 * fundamental spatial unit used throughout the simulation system for
 * representing drone positions, collision locations, and movement targets.
 * 
 * @note Coordinate System:
 *       - x: 0 to MAX_X-1
 *       - y: 0 to MAX_Y-1
 *       - z: 0 to MAX_Z-1
 * 
 * @note Memory Layout:
 *       Structure is packed efficiently for cache performance in
 *       position array operations and spatial calculations.
 * 
 * @note Usage Patterns:
 *       - Drone current position tracking
 *       - Movement waypoint definitions
 *       - Collision location recording
 *       - Boundary validation checks
 */
typedef struct {
    int x;  /**< 0 to MAX_X-1 */
    int y;  /**< 0 to MAX_Y-1 */
    int z;  /**< 0 to MAX_Z-1 */
} Position;

/**
 * @brief Complete drone definition and flight plan structure
 * 
 * Contains all information needed to represent a drone including identification,
 * complete movement trajectory, and timing information. Each drone maintains
 * its full flight plan as a sequence of positions over time.
 * 
 * @note Memory Management:
 *       The movements array is dynamically allocated based on duration.
 *       Caller is responsible for proper memory allocation and deallocation.
 * 
 * @note Flight Plan Model:
 *       - movements[0]: Initial position at time step 0
 *       - movements[i]: Position at time step i (1 ≤ i < duration)
 *       - duration: Total number of time steps in flight plan
 * 
 * @note Identification:
 *       - id: Unique numeric identifier for inter-process communication
 *       - name: Human-readable identifier for logging and debugging
 */
typedef struct {
    int id;                     /**< Unique numeric drone identifier */
    char name[50];              /**< Human-readable drone name/identifier */
    Position *movements;        /**< Array of positions per time step */
    int duration;              /**< Total routine duration in time steps */
} Drone;

/**
 * @brief Drone operational status enumeration
 * 
 * Defines the possible states a drone can be in during simulation execution.
 * Used for state tracking, error reporting, and simulation flow control.
 * 
 * @note State Transitions:
 *       - SUCESS: Normal operation, drone following flight plan successfully
 *       - COLLISION: Drone has collided with another drone or obstacle
 *       - OUT_OF_BOUNDS: Drone has moved outside valid simulation boundaries
 * 
 * @note Error Handling:
 *       Non-success states typically trigger:
 *       - Drone deactivation and removal from simulation
 *       - Event logging for post-simulation analysis
 *       - Possible simulation termination depending on severity
 */
typedef enum { 
    SUCESS,           /**< Drone operating normally */
    COLLISION,        /**< Drone involved in collision event */
    OUT_OF_BOUNDS     /**< Drone moved outside simulation boundaries */
} EstadoDrone;

/**
 * @brief Collision event record structure
 * 
 * Captures complete information about a collision event for logging,
 * analysis, and reporting purposes. Contains temporal, spatial, and
 * participant information for comprehensive collision documentation.
 * 
 * @note Event Documentation:
 *       - time: Simulation time step when collision occurred
 *       - position: 3D coordinates of collision location
 *       - dronesUsed: Names/identifiers of drones involved in collision
 *       - MAX_COLLISIONS: The maximum number of collisions that the system will allow
 * 
 * @note Memory Management:
 *       dronesUsed array contains pointers to string data. Ensure proper
 *       string lifetime management to avoid dangling pointer issues.
 */
typedef struct {
    int time;                   /**< Simulation time step of collision */
    Position position;          /**< 3D location where collision occurred */
    char *dronesUsed[2];       /**< Identifiers of colliding drones */
} Collision;

/**
 * @brief Wind condition structure for environmental simulation
 * 
 * --------
 * 
 * @note Implemented in Sprint 2, not yet used on Sprint 3
 * 
 */
typedef struct {
    int velocity;              /**< Wind speed in simulation units/time step */
    char direction;            /**< Wind direction (N/S/E/W or similar encoding) */
} Wind;

/*============================================================================*/
/*                         INTER-PROCESS COMMUNICATION                       */
/*============================================================================*/

/**
 * @brief Shared memory layout for inter-process drone data exchange
 * 
 * Defines the complete structure of shared memory used for communication
 * between the main simulation process and monitoring threads/processes.
 * Contains real-time drone state information accessible to all processes.
 * 
 * @note Synchronization Requirements:
 *       Access to this structure must be synchronized using the semaphore
 *       system managed by shared_resources.h functions. Each drone's data
 *       slot is protected by its corresponding semaphore.
 * 
 * @note Memory Layout:
 *       - positions[i]: Current 3D position of drone with ID i
 *       - active[i]: Activity flag for drone i (1=active, 0=terminated)
 *       - step[i]: Current time step/waypoint index for drone i
 * 
 * @note Index Mapping:
 *       Array indices directly correspond to drone IDs. Drone with ID=5
 *       uses array elements at index 5 across all arrays.
 * 
 * @note Real-Time Requirements:
 *       This structure is updated frequently (potentially every simulation
 *       time step) and must support high-frequency read/write operations
 *       from multiple processes simultaneously.
 */
typedef struct {
    Position positions[MAX_DRONES];   /**< Current position of each drone */
    int active[MAX_DRONES];           /**< Activity flags: 1=active, 0=finished */
    int step[MAX_DRONES];             /**< Current time step of each drone */
    Wind wind;
} SharedMemory;

/**
 * @brief Enhanced collision event structure for monitoring system
 * 
 * Extended collision event structure used by the monitoring subsystem
 * for comprehensive collision detection, logging, and analysis. Supports
 * multi-drone collisions and includes precise timing information.
 * 
 * @note Enhanced Features:
 *       - Supports collisions involving more than 2 drones
 *       - Includes wall-clock timestamp for real-world timing correlation
 *       - Provides complete participant list with flexible drone count
 * 
 * @note Multi-Drone Support:
 *       - drone_ids[]: Array of all drone IDs involved in collision
 *       - num_drones: Actual number of drones in collision (≤ MAX_DRONES)
 * 
 * @note Timing Information:
 *       - time_step: Simulation time when collision occurred
 *       - timestamp: Real-world time when collision was detected
 *       - Enables correlation between simulation time and execution time
 * 
 * @note Usage Context:
 *       Primarily used by collision detection threads and report generation
 *       systems. Provides more detailed information than the basic Collision
 *       structure for comprehensive analysis and debugging.
 * 
 * @note Performance Considerations:
 *       Structure size is proportional to MAX_DRONES. Large drone counts
 *       will increase memory usage for collision event storage and
 *       processing time for collision analysis operations.
 */
typedef struct {
    int time_step;                    /**< Simulation time step of collision */
    int x, y, z;                     /**< Collision coordinates (separate for clarity) */
    int drone_ids[MAX_DRONES];       /**< Array of all participating drone IDs */
    int num_drones;                  /**< Actual number of drones in collision */
    time_t timestamp;                /**< Wall-clock time when collision detected */
} CollisionEvent;

typedef struct {
	Position positions[MAX_TIME];
} DroneExecutionHistory;



/*============================================================================*/
/*                               USAGE EXAMPLES                              */
/*============================================================================*/

/**
 * @example
 * @brief Basic position and drone initialization example
 * 
 * @code
 * #include "types.h"
 * #include <stdlib.h>
 * #include <string.h>
 * 
 * // Create a simple drone with a basic flight plan
 * Drone* create_simple_drone(int id, const char* name) {
 *     Drone* drone = malloc(sizeof(Drone));
 *     if (!drone) return NULL;
 *     
 *     drone->id = id;
 *     strncpy(drone->name, name, sizeof(drone->name) - 1);
 *     drone->name[sizeof(drone->name) - 1] = '\0';
 *     
 *     // Create a simple 10-step flight plan
 *     drone->duration = 10;
 *     drone->movements = malloc(drone->duration * sizeof(Position));
 *     if (!drone->movements) {
 *         free(drone);
 *         return NULL;
 *     }
 *     
 *     // Initialize flight plan (straight line movement)
 *     for (int i = 0; i < drone->duration; i++) {
 *         drone->movements[i].x = i;     // Move east
 *         drone->movements[i].y = 5;     // Constant north position
 *         drone->movements[i].z = 10;    // Constant altitude
 *     }
 *     
 *     return drone;
 * }
 * 
 * // Clean up drone resources
 * void destroy_drone(Drone* drone) {
 *     if (drone) {
 *         free(drone->movements);
 *         free(drone);
 *     }
 * }
 * @endcode
 */

/**
 * @example
 * @brief Collision event creation and logging example
 * 
 * @code
 * #include "types.h"
 * #include <time.h>
 * #include <stdio.h>
 * 
 * // Create a collision event record
 * CollisionEvent create_collision_event(int step, Position pos, int* drone_ids, int count) {
 *     CollisionEvent event;
 *     
 *     event.time_step = step;
 *     event.x = pos.x;
 *     event.y = pos.y;
 *     event.z = pos.z;
 *     event.num_drones = count;
 *     event.timestamp = time(NULL);
 *     
 *     // Copy drone IDs (up to MAX_DRONES)
 *     int copy_count = (count > MAX_DRONES) ? MAX_DRONES : count;
 *     for (int i = 0; i < copy_count; i++) {
 *         event.drone_ids[i] = drone_ids[i];
 *     }
 *     
 *     return event;
 * }
 * 
 * // Log collision event to console
 * void log_collision_event(const CollisionEvent* event) {
 *     printf("COLLISION at step %d: (%d,%d,%d)\n", 
 *            event->time_step, event->x, event->y, event->z);
 *     printf("Drones involved: ");
 *     for (int i = 0; i < event->num_drones; i++) {
 *         printf("%d ", event->drone_ids[i]);
 *     }
 *     printf("\nTimestamp: %s", ctime(&event->timestamp));
 * }
 * @endcode
 */

/**
 * @example
 * @brief Shared memory access pattern example
 * 
 * @code
 * #include "types.h"
 * #include <semaphore.h>
 * 
 * // Update drone position in shared memory (with synchronization)
 * void update_drone_position(SharedMemory* shared_mem, sem_t* drone_sem, 
 *                           int drone_id, Position new_pos, int new_step) {
 *     // Acquire exclusive access to this drone's data
 *     sem_wait(&drone_sem[drone_id]);
 *     
 *     // Update drone state atomically
 *     shared_mem->positions[drone_id] = new_pos;
 *     shared_mem->step[drone_id] = new_step;
 *     shared_mem->active[drone_id] = 1;  // Mark as active
 *     
 *     // Release access
 *     sem_post(&drone_sem[drone_id]);
 * }
 * 
 * // Read drone position from shared memory (with synchronization)
 * Position read_drone_position(SharedMemory* shared_mem, sem_t* drone_sem, int drone_id) {
 *     Position pos;
 *     
 *     // Acquire read access
 *     sem_wait(&drone_sem[drone_id]);
 *     
 *     // Read position atomically
 *     pos = shared_mem->positions[drone_id];
 *     
 *     // Release access
 *     sem_post(&drone_sem[drone_id]);
 *     
 *     return pos;
 * }
 * @endcode
 */

/*============================================================================*/
/*                           END OF HEADER GUARD                             */
/*============================================================================*/

#endif
