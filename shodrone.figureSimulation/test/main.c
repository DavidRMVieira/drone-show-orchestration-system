/* drone_simulation_enhanced.c
   Enhanced hybrid drone simulation system with multi-threaded parent process */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <dirent.h>
#include <pthread.h>
#include <semaphore.h>
#include <fcntl.h>
#include <signal.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <time.h>
#include "types.h"
#include "drone.h"

#define FIGURE_FOLDER "./figureData"
#define REPORT_FILE "simulation_report.log"

// Global variables
Drone drones[MAX_DRONES];
int drone_count = 0;
int *shared_x, *shared_y, *shared_z;
int *shared_active;  // Track active drones
int shm_fd;
sem_t *sem_positions;
pid_t pids[MAX_DRONES];

// Thread handles
pthread_t collision_thread;
pthread_t report_thread;
pthread_t monitor_thread;

// Synchronization primitives for parent threads
pthread_mutex_t collision_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t report_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t collision_detected = PTHREAD_COND_INITIALIZER;

// Collision event data structure
typedef struct {
    int time_step;
    int x, y, z;
    int drone_ids[MAX_DRONES];
    int num_drones;
    time_t timestamp;
} CollisionEvent;

// Shared collision event buffer
CollisionEvent collision_buffer[100];
int collision_count = 0;
int new_collision_flag = 0;
int simulation_running = 1;

// --------------------- Helper Functions -----------------------
void list_figures(const char *folder) {
    DIR *dir = opendir(folder);
    if (!dir) {
        perror("Failed to open data folder");
        exit(EXIT_FAILURE);
    }
    printf("Available figures:\n");
    struct dirent *entry;
    while ((entry = readdir(dir))) {
        if (entry->d_type == DT_DIR && strcmp(entry->d_name, ".") && strcmp(entry->d_name, "..")) {
            printf("- %s\n", entry->d_name);
        }
    }
    closedir(dir);
}

void ask_user_for_figure(char *selected_figure, size_t size) {
    printf("\nEnter the name of the figure to load (e.g., figure1): ");
    if (fgets(selected_figure, size, stdin)) {
        // Remove newline character
        selected_figure[strcspn(selected_figure, "\n")] = 0;
    }
}

int load_figure(const char *figure_path, Drone *drones, int *drone_count) {
    DIR *dir = opendir(figure_path);
    if (!dir) {
        perror("Failed to open figure folder");
        return -1;
    }

    struct dirent *entry;
    int count = 0;

    while ((entry = readdir(dir))) {
        if (strstr(entry->d_name, ".txt")) {
            char filepath[512];
            snprintf(filepath, sizeof(filepath), "%s/%s", figure_path, entry->d_name);

            drones[count].id = count;
            strncpy(drones[count].name, entry->d_name, 50);

            if (load_drone_routine(filepath, &drones[count]) == 0) {
                printf("Loaded %s (%d steps)\n", drones[count].name, drones[count].duration);
                count++;
            }
        }
    }
    closedir(dir);
    *drone_count = count;
    return 0;
}

void wait_for_user_input() {
    char cmd[10];
    printf("[Parent] Press ENTER to start simulation or 'q' to cancel: ");
    if (fgets(cmd, sizeof(cmd), stdin)) {
        if (cmd[0] == 'q' || cmd[0] == 'Q') {
            exit(0);
        }
    }
}

void cleanup_resources() {
    printf("[Cleanup] Shutting down simulation...\n");
    simulation_running = 0;
    
    // Signal all waiting threads
    pthread_cond_broadcast(&collision_detected);
    
    // Clean up shared memory
    if (shared_x) {
        munmap(shared_x, sizeof(int) * drone_count * 4); // x, y, z, active
    }
    if (shm_fd != -1) {
        close(shm_fd);
        shm_unlink("/drone_shm");
    }
    
    // Clean up semaphores
    if (sem_positions) {
        for (int i = 0; i < drone_count; i++) {
            sem_destroy(&sem_positions[i]);
        }
        munmap(sem_positions, sizeof(sem_t) * drone_count);
    }
}

// --------------------- Drone Process -------------------------
void drone_process(int index) {
    printf("[Drone %d] Started process\n", index);
    
    for (int t = 0; t < drones[index].duration && simulation_running; t++) {
        // Wait for permission to update position
        sem_wait(&sem_positions[index]);
        
        // Update shared memory with current position
        shared_x[index] = drones[index].movements[t].x;
        shared_y[index] = drones[index].movements[t].y;
        shared_z[index] = drones[index].movements[t].z;
        shared_active[index] = 1; // Mark as active
        
        // Release the semaphore
        sem_post(&sem_positions[index]);
        
        // Simulate movement time
        usleep(100000); // 100ms per step
    }
    
    // Mark drone as inactive when finished
    sem_wait(&sem_positions[index]);
    shared_active[index] = 0;
    sem_post(&sem_positions[index]);
    
    printf("[Drone %d] Completed routine\n", index);
    exit(0);
}

// -------------------- Collision Detection Thread -----------------------
void* collision_detector(void *arg) {
    printf("[Collision Thread] Started\n");
    int time_step = 0;
    
    while (simulation_running) {
        // 3D space collision matrix
        int collision_matrix[MAX_X][MAX_Y][MAX_Z];
        int position_map[MAX_X][MAX_Y][MAX_Z][MAX_DRONES]; // Track which drones are at each position
        int position_counts[MAX_X][MAX_Y][MAX_Z];
        
        // Initialize matrices
        memset(collision_matrix, 0, sizeof(collision_matrix));
        memset(position_counts, 0, sizeof(position_counts));
        
        // Sample current positions from shared memory
        for (int i = 0; i < drone_count; i++) {
            if (!shared_active[i]) continue; // Skip inactive drones
            
            sem_wait(&sem_positions[i]);
            int x = shared_x[i];
            int y = shared_y[i];
            int z = shared_z[i];
            sem_post(&sem_positions[i]);
            
            // Validate coordinates
            if (x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y && z >= 0 && z < MAX_Z) {
                position_map[x][y][z][position_counts[x][y][z]] = i;
                position_counts[x][y][z]++;
                
                if (position_counts[x][y][z] > 1) {
                    collision_matrix[x][y][z] = 1;
                }
            }
        }
        
        // Check for collisions and notify report thread
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                for (int z = 0; z < MAX_Z; z++) {
                    if (collision_matrix[x][y][z] && position_counts[x][y][z] > 1) {
                        pthread_mutex_lock(&collision_mutex);
                        
                        // Create collision event
                        if (collision_count < 100) {
                            CollisionEvent *event = &collision_buffer[collision_count];
                            event->time_step = time_step;
                            event->x = x;
                            event->y = y;
                            event->z = z;
                            event->num_drones = position_counts[x][y][z];
                            event->timestamp = time(NULL);
                            
                            // Record involved drone IDs
                            for (int d = 0; d < position_counts[x][y][z]; d++) {
                                event->drone_ids[d] = position_map[x][y][z][d];
                            }
                            
                            collision_count++;
                            new_collision_flag = 1;
                            
                            printf("[COLLISION DETECTED] Time %d at (%d,%d,%d) - %d drones involved\n", 
                                   time_step, x, y, z, position_counts[x][y][z]);
                        }
                        
                        // Signal report thread about new collision
                        pthread_cond_signal(&collision_detected);
                        pthread_mutex_unlock(&collision_mutex);
                    }
                }
            }
        }
        
        time_step++;
        usleep(100000); // 100ms detection interval
    }
    
    printf("[Collision Thread] Stopped\n");
    return NULL;
}

// -------------------- Report Generation Thread -----------------------
void* report_generator(void *arg) {
    printf("[Report Thread] Started\n");
    FILE *report_file = fopen(REPORT_FILE, "w");
    if (!report_file) {
        perror("Failed to create report file");
        return NULL;
    }
    
    // Write report header
    fprintf(report_file, "=== DRONE SIMULATION REPORT ===\n");
    fprintf(report_file, "Simulation started at: %s\n", ctime(&(time_t){time(NULL)}));
    fprintf(report_file, "Number of drones: %d\n\n", drone_count);
    fflush(report_file);
    
    while (simulation_running) {
        pthread_mutex_lock(&collision_mutex);
        
        // Wait for collision notification
        while (!new_collision_flag && simulation_running) {
            pthread_cond_wait(&collision_detected, &collision_mutex);
        }
        
        if (!simulation_running) {
            pthread_mutex_unlock(&collision_mutex);
            break;
        }
        
        // Process new collisions
        if (new_collision_flag) {
            CollisionEvent *event = &collision_buffer[collision_count - 1];
            
            fprintf(report_file, "COLLISION EVENT #%d\n", collision_count);
            fprintf(report_file, "Time: %d, Position: (%d,%d,%d)\n", 
                    event->time_step, event->x, event->y, event->z);
            fprintf(report_file, "Timestamp: %s", ctime(&event->timestamp));
            fprintf(report_file, "Drones involved (%d): ", event->num_drones);
            
            for (int i = 0; i < event->num_drones; i++) {
                fprintf(report_file, "%s%d", (i > 0) ? ", " : "", event->drone_ids[i]);
            }
            fprintf(report_file, "\n\n");
            fflush(report_file);
            
            new_collision_flag = 0;
        }
        
        pthread_mutex_unlock(&collision_mutex);
    }
    
    // Write report footer
    fprintf(report_file, "=== SIMULATION SUMMARY ===\n");
    fprintf(report_file, "Total collisions detected: %d\n", collision_count);
    fprintf(report_file, "Simulation ended at: %s\n", ctime(&(time_t){time(NULL)}));
    
    fclose(report_file);
    printf("[Report Thread] Generated final report: %s\n", REPORT_FILE);
    return NULL;
}

// -------------------- Monitor Thread -----------------------
void* system_monitor(void *arg) {
    printf("[Monitor Thread] Started\n");
    int last_active_count = drone_count;
    
    while (simulation_running) {
        int active_count = 0;
        
        // Count active drones
        for (int i = 0; i < drone_count; i++) {
            if (shared_active[i]) {
                active_count++;
            }
        }
        
        if (active_count != last_active_count) {
            pthread_mutex_lock(&report_mutex);
            printf("[Monitor] Active drones: %d/%d\n", active_count, drone_count);
            last_active_count = active_count;
            pthread_mutex_unlock(&report_mutex);
        }
        
        // Check if simulation should end
        if (active_count == 0) {
            printf("[Monitor] All drones completed their routines\n");
            simulation_running = 0;
            pthread_cond_broadcast(&collision_detected);
            break;
        }
        
        sleep(1); // Monitor every second
    }
    
    printf("[Monitor Thread] Stopped\n");
    return NULL;
}

// -------------------- Shared Memory Setup -----------------------
int setup_shared_memory() {
    // Create shared memory for positions and active status
    size_t shm_size = sizeof(int) * drone_count * 4; // x, y, z, active for each drone
    
    shm_fd = shm_open("/drone_shm", O_CREAT | O_RDWR, 0666);
    if (shm_fd == -1) {
        perror("shm_open failed");
        return -1;
    }
    
    if (ftruncate(shm_fd, shm_size) == -1) {
        perror("ftruncate failed");
        return -1;
    }
    
    void *shm_base = mmap(0, shm_size, PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
    if (shm_base == MAP_FAILED) {
        perror("mmap failed");
        return -1;
    }
    
    // Partition shared memory
    shared_x = (int *)shm_base;
    shared_y = shared_x + drone_count;
    shared_z = shared_y + drone_count;
    shared_active = shared_z + drone_count;
    
    // Initialize shared memory
    memset(shm_base, 0, shm_size);
    
    return 0;
}

int setup_semaphores() {
    // Create semaphores for drone position synchronization
    sem_positions = mmap(NULL, sizeof(sem_t) * drone_count, 
                        PROT_READ | PROT_WRITE, MAP_SHARED | MAP_ANONYMOUS, -1, 0);
    if (sem_positions == MAP_FAILED) {
        perror("mmap semaphores failed");
        return -1;
    }
    
    // Initialize semaphores
    for (int i = 0; i < drone_count; i++) {
        if (sem_init(&sem_positions[i], 1, 1) == -1) {
            perror("sem_init failed");
            return -1;
        }
    }
    
    return 0;
}

// ------------------------ Signal Handler -----------------------
void signal_handler(int sig) {
    printf("\n[Signal] Received signal %d, shutting down...\n", sig);
    simulation_running = 0;
    
    // Terminate all child processes
    for (int i = 0; i < drone_count; i++) {
        if (pids[i] > 0) {
            kill(pids[i], SIGTERM);
        }
    }
    
    cleanup_resources();
    exit(0);
}

// ------------------------ Main ------------------------------
int main() {
    printf("=== Enhanced Drone Simulation System ===\n\n");
    
    // Setup signal handler
    signal(SIGINT, signal_handler);
    signal(SIGTERM, signal_handler);
    
    // Load figure data
    char figure[256];
    list_figures(FIGURE_FOLDER);
    ask_user_for_figure(figure, sizeof(figure));

    char path[512];
    snprintf(path, sizeof(path), "%s/%s", FIGURE_FOLDER, figure);

    if (load_figure(path, drones, &drone_count) != 0 || drone_count == 0) {
        printf("[Error] Could not load figure\n");
        return 1;
    }

    printf("[Main] Loaded %d drones successfully\n", drone_count);

    // Initialize hybrid simulation environment
    printf("[Main] Setting up shared memory and synchronization...\n");
    
    if (setup_shared_memory() != 0) {
        printf("[Error] Failed to setup shared memory\n");
        return 1;
    }
    
    if (setup_semaphores() != 0) {
        printf("[Error] Failed to setup semaphores\n");
        cleanup_resources();
        return 1;
    }

    wait_for_user_input();
    printf("[Main] Starting simulation...\n");

    // Create parent process threads
    printf("[Main] Creating parent threads...\n");
    
    if (pthread_create(&collision_thread, NULL, collision_detector, NULL) != 0) {
        perror("Failed to create collision thread");
        cleanup_resources();
        return 1;
    }
    
    if (pthread_create(&report_thread, NULL, report_generator, NULL) != 0) {
        perror("Failed to create report thread");
        cleanup_resources();
        return 1;
    }
    
    if (pthread_create(&monitor_thread, NULL, system_monitor, NULL) != 0) {
        perror("Failed to create monitor thread");
        cleanup_resources();
        return 1;
    }

    // Launch drone processes
    printf("[Main] Launching drone processes...\n");
    for (int i = 0; i < drone_count; i++) {
        pid_t pid = fork();
        if (pid == 0) {
            // Child process - become a drone
            drone_process(i);
        } else if (pid > 0) {
            // Parent process - store child PID
            pids[i] = pid;
        } else {
            perror("Fork failed");
            cleanup_resources();
            return 1;
        }
    }

    printf("[Main] All processes launched. Monitoring simulation...\n");

    // Wait for all child processes to complete
    for (int i = 0; i < drone_count; i++) {
        int status;
        waitpid(pids[i], &status, 0);
        printf("[Main] Drone process %d completed\n", i);
    }

    // Signal threads to stop and wait for them
    simulation_running = 0;
    pthread_cond_broadcast(&collision_detected);
    
    pthread_join(collision_thread, NULL);
    pthread_join(report_thread, NULL);
    pthread_join(monitor_thread, NULL);

    printf("[Main] Simulation completed successfully.\n");
    printf("[Main] Total collisions detected: %d\n", collision_count);
    printf("[Main] Report saved to: %s\n", REPORT_FILE);

    cleanup_resources();
    return 0;
}