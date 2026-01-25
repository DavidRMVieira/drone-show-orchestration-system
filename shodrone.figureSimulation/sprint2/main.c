/*
    Dar load na figura;
    Fazer fork para cada drone
    Dar setup a pipes para fazer a comunicação entre cada processo
    Dar track nas posições com uma data struct
    Detetar colisões (na matriz, se cada drone tiver uma posição diferente, não há colisão)

    Cada drone tem uma posição (x, y)
    Cada figura terá um script para cada drone envolvido::::  a program routine run by the drone to implement that figure/sequence

    Finaly, there is also the need to test/validate figures and complete shows:
    Simulation of the code of the figure, which means simulation the operation of all drones
    involved and check if there are no collisions. A collision is when two drone are in the same
    place at the same time. If you create a 3D matrix (e.g., 1 cell = 1 cubic meter) and use timebased simulation, it is possible to verify approximately that no collisions will occur.
    Finally, there is also the need to test a full show, which is a composition of multiple
    figures/sequences. In this one, the simulation involves the coordination with a central
    orchestrator/maestro server, that gives the order to initiate a figure and receives feedback
    from each drone regarding the completion of a figure. 

    Step 1: List available wind data for the user to choose from
    
    Step 2: Prompt user for the name of the wind data to load
    
    Step 3: Load the wind data
    
    Step 4: List available figures for the user to choose from

    Step 5: Prompt user for the name of the figure to load

    Step 6: Load the figure and its drones

    Step 7: Setup for the signal handlers used by the Parent process
    
    Step 8: Create a process for each drone & Step 5: Execute the drone's routine

    Step 9: Wait for user input to control simulation

    Step 10: Monitor for collisions

    Step 11: Wait for all child processes (drones) to finish
	
	Step 12: Get the report file
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
#include <signal.h>
#include <stdbool.h>
#include "types.h"
#include "drone.h"

int pipes[MAX_DRONES][2];  // Set up pipes for communication with each drone Parent -> Child
int pos_pipes[MAX_DRONES][2];  // Position pipes Child -> Parent
Drone drones[MAX_DRONES];   // Array to store drone information
int drone_count = 0;       // Number of drones loaded for the current figure
pid_t pids[MAX_DRONES];    // Array to store the PIDs of the drone processes
int drone_still_alive[MAX_DRONES]; // Array to store information on whether the drone died or not
EstadoDrone estados[MAX_DRONES]; // Array to save information to help in the final report
Collision collisions[MAX_COLLISIONS]; // Array to save information about collisions to help in final report
Wind wind;
int offset = 0;

int terminate = 0;
int total_collisions = 0;
bool isOutOfBounds = false;

int running = 0;  // Variable to track if drones are running or paused
int completed_drones= 0; // Variable to track number of completed drones

int matrix[MAX_X][MAX_Y][MAX_Z] = {0};


// ---------- Signal Setup Utility ----------

void setup_sigaction(int signum, void (*handler)(int)) {
    struct sigaction sa;
    sa.sa_handler = handler;    // Set the signal handler function
    sa.sa_flags = 0;    // Set flags for signal handling (no special flags at the moment)
    sigemptyset(&sa.sa_mask);   // Make sure the signal mask is empty 
    if (sigaction(signum, &sa, NULL) == -1) {   // Register the signal handler 
        perror("sigaction");    // Print an error message if it fails
        exit(EXIT_FAILURE);     // Exit if the registering fails
    }
}

// ---------- Signal Handlers ----------

// Handler for SIGUSR1 to indicate drones should start their routines
void handle_sigusr1(int sig) {
    running = 1;  // Set running flag to true when SIGUSR1 is received
}

// Handler for SIGUSR2 to indicate drones should exit after completing their routines (used on the child processes)
void handle_sigusr2_child(int sig) {
    printf("[Child %d] Received SIGUSR2. Exiting...\n", getpid());
    exit(0);
}

// Handler for SIGUSR2 to indicate if all the processes have been completed
void handle_sigusr2_parent(int sig) {
    completed_drones++;
}

// Handler for SIGUSR1 to indicate if a collision has been detected
void handle_sigusr1_collision(int sig) {
    sigset_t block_mask, old_mask;
    
    sigfillset(&block_mask);  // Inicializa block_mask para bloquear todos os sinais
    sigprocmask(SIG_BLOCK, &block_mask, &old_mask);  // Bloqueia todos os sinais
    
    printf("[Child %d] Collision detected!\n", getpid());
    
    sigprocmask(SIG_SETMASK, &old_mask, NULL);  // Restaura a máscara de sinais anterior
}

// Handler fro clean termination
void handle_sigterm(int sig) {
    terminate = 1;
}

// ---------- Wind, Figure and Drone Utilities ----------

// Function to list available figures in the "figureData" directory
void list_figures(const char *folder) {
    DIR *dir = opendir(folder);
    if (!dir) {
        perror("Failed to open data folder");
        exit(1);
    }

    printf("Available figures:\n");
    struct dirent *entry;
    while ((entry = readdir(dir))) {
        if (entry->d_type == DT_DIR && strcmp(entry->d_name, ".") && strcmp(entry->d_name, "..")) {
            printf("- %s\n", entry->d_name);  // Print the name of each available figure
        }
    }

    closedir(dir);
}

// Function to list available wind data in the "wind" directory
void list_wind(const char *folder) {
    DIR *dir = opendir(folder);
    if (!dir) {
        perror("Failed to open data folder");
        exit(1);
    }

    printf("Available wind data:\n");
    struct dirent *entry;
    while ((entry = readdir(dir))) {
        if (entry->d_type == DT_DIR && strcmp(entry->d_name, ".") && strcmp(entry->d_name, "..")) {
            printf("- %s\n", entry->d_name);  // Print the name of each available figure
        }
    }

    closedir(dir);
}

// Function to load all drones from the selected figure
int load_figure(const char *figure_path, Drone *drones, int *drone_count) {
    DIR *dir = opendir(figure_path);
    if (!dir) {
        perror("Failed to open figure folder");
        return -1;  // Return error if folder can't be opened
    }

    struct dirent *entry;
    int count = 0;

    // Load each drone's routine from text files in the selected figure's directory
    while ((entry = readdir(dir))) {
        if (strstr(entry->d_name, ".txt")) {
            char filepath[512];
            snprintf(filepath, sizeof(filepath), "%s/%s", figure_path, entry->d_name);

            drones[count].id = count;  // Assign ID to each drone
            strncpy(drones[count].name, entry->d_name, 50);

            if (load_drone_routine(filepath, &drones[count]) == 0) {
                printf("Loaded drone %s (%d steps/time)\n", drones[count].name, drones[count].duration);
                count++;  // Increment count if drone routine loaded successfully
            } else {
                fprintf(stderr, "Error loading %s\n", entry->d_name);  // Error loading drone routine
            }
        }
    }

    *drone_count = count;  // Update the number of loaded drones
    closedir(dir);
    return 0;
}

// Function to load the wind data from the selected one
int load_wind(const char *wind_path) {
	DIR *dir = opendir(wind_path);
    if (!dir) {
        perror("Failed to open wind folder");
        return -1;  // Return error if folder can't be opened
    }
    
    struct dirent *entry;
    char filepath[528];
    int loaded = 0;

    while ((entry = readdir(dir))) {
        if (strstr(entry->d_name, ".txt")) {
            snprintf(filepath, sizeof(filepath), "%s/%s", wind_path, entry->d_name);

            FILE *file = fopen(filepath, "r");
            if (!file) {
                perror("Failed to open wind file");
                continue;
            }

            // Ex: conteúdo do ficheiro: "12 N"
            int velocity;
            char direction;

            if (fscanf(file, "%d %c", &velocity, &direction) == 2) {
                wind.velocity = velocity;
                wind.direction = direction;
                loaded = 1;
                printf("Wind data loaded: %d km/h, direction %c\n", velocity, direction);
            } else {
                fprintf(stderr, "Invalid format in wind file: %s\n", filepath);
            }

            fclose(file);
            break;  // Lê apenas o primeiro ficheiro .txt encontrado
        }
    }

    closedir(dir);

    if (!loaded) {
        fprintf(stderr, "No valid wind data found in folder: %s\n", wind_path);
        return -1;
    }

    return 0;
}

// Function to get the final report
void get_report(const char *filename) {
	
    FILE *f = fopen(filename, "w");
    if (f == NULL) {
        perror("Error trying to open the file!");
        return;
    }

    fprintf(f, "Number of Drones: %d\n\n", drone_count);
	
    fprintf(f, "Drone information:\n\n");
    for (int j = 0; j < drone_count; j++) {
        fprintf(f, "Drone ID: %d\n", drones[j].id);
        fprintf(f, "Drone Name: %s\n", drones[j].name);
        fprintf(f, "Routine (Duration: %d steps/time):\n", drones[j].duration);
        for (int i = 0; i < drones[j].duration; i++) {
			
			switch (wind.direction) {
				case 'N': drones[j].movements[i].y -= offset; break;
				case 'S': drones[j].movements[i].y += offset; break;
				case 'E': drones[j].movements[i].x += offset; break;
				case 'W': drones[j].movements[i].x -= offset; break;
				default: break;
			}
			
            fprintf(f, "  Step/Time %d -> (x: %d, y: %d, z: %d)\n", i,
                drones[j].movements[i].x,
                drones[j].movements[i].y,
                drones[j].movements[i].z);
        }
        fprintf(f, "\n");
    }
    
    fprintf(f, "--------------------------------------\n");
    
    fprintf(f, "Collisions Information:\n\n");
    if (total_collisions == 0) {
        fprintf(f, "There has been 0 collisions!\n");
    } else {
        for (int i = 0; i < total_collisions; i++) {
            fprintf(f, "Step/Time %d -> (x: %d, y: %d, z: %d) involving %s and %s\n",
                collisions[i].time,
                collisions[i].position.x,
                collisions[i].position.y,
                collisions[i].position.z,
                collisions[i].dronesUsed[0],
                collisions[i].dronesUsed[1]);
        }
    }

    fprintf(f, "\n--------------------------------------\n");
    
    fprintf(f, "Figure Validation:\n\n");
    if (isOutOfBounds || total_collisions > 0) {
        fprintf(f, "Figure did not pass!\n");
    } else {
        fprintf(f, "Figure passed!\n");
    }

    fclose(f);
}

// ---------- Prompt user to enter the wind data and figure name ----------

void ask_user_for_figure(char *selected_figure, size_t size) {
    printf("\nEnter the name of the figure to load (e.g., figure1): ");
    scanf("%s", selected_figure);
    getchar(); // consume newline left by scanf
}

void ask_user_for_wind_data(char *selected_wind, size_t size) {
    printf("\nEnter the name of the wind data to load (e.g., wind1): ");
    scanf("%s", selected_wind);
    getchar(); // consume newline left by scanf
}

// ---------- Load wind, figure and initialize drones ----------


int load_figure_and_drones(const char *figure_path) {
    if (load_figure(figure_path, drones, &drone_count) != 0 || drone_count == 0) {
        printf("Failed to load the figure or no drones found.\n");
        return 1;  // Return error if figure loading fails or no drones are found
    } else
        return 0;
}

int load_wind_data(const char *wind_path) {
	if (load_wind(wind_path) != 0) {
		printf("Failed to load the wind data.\n");
		return 1;
	} else
		return 0;
}

// ---------- Set up parent signal handlers ----------

void setup_parent_signal_handlers() {
    setup_sigaction(SIGUSR2, handle_sigusr2_parent);  // Drone completion
    setup_sigaction(SIGUSR1, handle_sigusr1_collision);  // For collision information
    setup_sigaction(SIGTERM, handle_sigterm);  // For clean termination
}

// ---------- Create drone child processes and set up pipes ----------

void create_drones_processes() {

for (int j = 0; j < drone_count; j++) {
        if (pipe(pipes[j]) == -1 || pipe(pos_pipes[j]) == -1) {
            perror("pipe failed");
            exit(1);    // Return error if pipe creation fails
        }

        pid_t pid = fork();  // Create a new process for each drone

        if (pid < 0) {
            perror("fork failed");
            exit(1);  // Return error if fork fails
        } else if (pid == 0) {  // Child process (drone process)
        
            // Set up signal handlers for communication between parent and child

            setup_sigaction(SIGUSR1, handle_sigusr1);
            setup_sigaction(SIGUSR2, handle_sigusr2_child);
            setup_sigaction(SIGTERM, handle_sigterm);

            close(pipes[j][1]);  // Close the write end of the pipe in the child process
            close(pos_pipes[j][0]); // Close the read end of the position pipe in the child process

            Drone this_drone;
            read(pipes[j][0], &this_drone, sizeof(Drone));  // Read drone data from the pipe
            close(pipes[j][0]);

            // Wait until the parent sends SIGUSR1 to start the drone's routine
            while (!running) {
                pause();  // Pause execution until SIGUSR1 is received
            }

            if (terminate) {
                printf("[Child %d] Termination signal received. Cleaning up...\n", getpid());
                exit(0);
            }

            // Step 5: Execute the drone's routine
            for (int i = 0; i < this_drone.duration; i++) {
                
                if (terminate) {
                    printf("[Child %d] Termination signal received. Cleaning up...\n", getpid());
                    exit(0);
                }

                printf("[Child %d] t=%d -> (%d, %d, %d)\n", getpid(), i,
                    this_drone.movements[i].x,
                    this_drone.movements[i].y,
                    this_drone.movements[i].z);
                write(pos_pipes[j][1], &this_drone.movements[i], sizeof(Position));
                pause();
                fflush(stdout);
                usleep(50000);
                
                if(terminate) {
					exit(0);
				}
            }

            close(pos_pipes[j][1]); // Close the write end of the pos pipe
            kill(getppid(), SIGUSR2);  // Notify parent process that the drone is finished
            exit(0);  // Exit the child process
        } else {  // Parent process
            pids[j] = pid;  // Store the PID of the drone process
            close(pipes[j][0]);  // Close the read end of the pipe in the parent process
            close(pos_pipes[j][1]); // Close the wwrite end of the position pipe in the parent process
            write(pipes[j][1], &drones[j], sizeof(Drone));  // Send drone data to the child process
            close(pipes[j][1]);  // Close the write end of the pipe after sending data
        }
    }
}

void create_drone_processes_with_wind() {
    // If velocity is lower then FIRS_LEVEL_WIND (10), it calls the original function
    if (wind.velocity < FIRST_LEVEL_WIND) {
        create_drones_processes(); // Uses the original function
        return;
    }

    if (wind.velocity >= FIRST_LEVEL_WIND && wind.velocity < SECOND_LEVEL_WIND) {
        offset = 1;
    } else if (wind.velocity >= SECOND_LEVEL_WIND) {
        offset = 2;
    }

    for (int j = 0; j< drone_count; j++) {
        // pipes's creation
        if (pipe(pipes[j]) == -1 || pipe(pos_pipes[j]) == -1) {
            perror("pipe failed");
            exit(1);
        }
		
		pid_t pid = fork();  // Create a new process for each drone
		
        if (pid < 0) {
            perror("fork failed");
            exit(1);  // Return error if fork fails
        } else if (pid == 0) {  // Child process (drone process)
        
            // Set up signal handlers for communication between parent and child

            setup_sigaction(SIGUSR1, handle_sigusr1);
            setup_sigaction(SIGUSR2, handle_sigusr2_child);
            setup_sigaction(SIGTERM, handle_sigterm);

            close(pipes[j][1]);  // Close the write end of the pipe in the child process
            close(pos_pipes[j][0]); // Close the read end of the position pipe in the child process

            // Wait until the parent sends SIGUSR1 to start the drone's routine
            while (!running) {
                pause();  // Pause execution until SIGUSR1 is received
            }

            if (terminate) {
                printf("[Child %d] Termination signal received. Cleaning up...\n", getpid());
                exit(0);
            }

            // Execute the drone's routine
            for (int i = 0; i < drones[j].duration; i++) {
                
                if (terminate) {
                    printf("[Child %d] Termination signal received. Cleaning up...\n", getpid());
                    exit(0);
                }
                
                switch (wind.direction) {
                    case 'N': drones[j].movements[i].y -= offset; break;
                    case 'S': drones[j].movements[i].y += offset; break;
                    case 'E': drones[j].movements[i].x += offset; break;
                    case 'W': drones[j].movements[i].x -= offset; break;
                    default: break;
                }

                printf("[Childs %d] t=%d -> (%d, %d, %d)\n", getpid(), i,
                    drones[j].movements[i].x,
                    drones[j].movements[i].y,
                    drones[j].movements[i].z);
                write(pos_pipes[j][1], &drones[j].movements[i], sizeof(Position));
                pause();
				usleep(50000);
				
                if(terminate) {
					exit(0);
				}
            }

            close(pos_pipes[j][1]); // Close the write end of the pos pipe
            kill(getppid(), SIGUSR2);  // Notify parent process that the drone is finished
            exit(0);  // Exit the child process
        } else {  // Parent process
            pids[j] = pid;  // Store the PID of the drone process
            close(pipes[j][0]);  // Close the read end of the pipe in the parent process
            close(pos_pipes[j][1]); // Close the wwrite end of the position pipe in the parent process
            write(pipes[j][1], &drones[j], sizeof(Drone));  // Send drone data to the child process
            close(pipes[j][1]);  // Close the write end of the pipe after sending data
        }
    }
}

// ---------- Wait for user to start or abort simulation ----------

void wait_for_user_input() {
    char cmd = 0;
    
    printf("[Parent] Press ENTER to start drones, or 'q' to abort: ");

    while (cmd != '\n' && cmd != 'q')
    {
        cmd = getchar(); // Read the user input 

        // Clear the buffer 
        if (cmd != '\n') {
            // Clear the rest of the line only if the user typed more than just Enter
            while (getchar() != '\n');
        }

        if (cmd == '\n') {
            // Start all drone processes by sending SIGUSR1 to each child
            for (int j = 0; j < drone_count; j++) {
                kill(pids[j], SIGUSR1);
            }

        } else if (cmd == 'q') {
            // Abort simulation by sending SIGUSR2 to each drone
            for (int j = 0; j < drone_count; j++) {
                kill(pids[j], SIGUSR2);
            }

        } else {
            // Invalid input, prompt the user again
            printf("[Parent] Invalid input. Please enter either ENTER to start drones or 'q' to abort.\n");
        }
    }
}

// ---------- Monitor drone positions and detect collisions ----------

void detect_collisions() {
	
	printf("[Parent] Initializing drones as alive\n");
    for (int i = 0; i < drone_count; i++) {
        drone_still_alive[i] = 1;
    }

    for (int t = 0; t < MAX_TIME && !terminate; t++) {
		
		printf("[Parent] Checking collisions in t=%d\n", t);
        
        // Clear the movements matrix at each time step
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                for (int z = 0; z < MAX_Z; z++) {
                    matrix[x][y][z] = 0;
                }
            }
        }

        Position positions[MAX_DRONES] = {0}; // Store all drone positions for this time step
		
		// process the data of each drone
        for (int j = 0; j < drone_count; j++) {
			if (!drone_still_alive[j]) continue; // ignores already finished drones
			
            if (read(pos_pipes[j][0], &positions[j], sizeof(Position)) == sizeof(Position)) {
                Position pos = positions[j];

                // Bounds check — if any coordinate is outside allowed space, terminate
                // Verifica limites
                if (pos.x >= MAX_X || pos.y >= MAX_Y || pos.z >= MAX_Z) {
                    printf("[Parent] Drone %s out of bounds: (%d,%d,%d)\n", drones[j].name, pos.x, pos.y, pos.z);
                    isOutOfBounds = true;
                    kill(pids[j], SIGTERM);
                    drone_still_alive[j] = 0;
                    break;
                }

                // Mark position as occupied
                matrix[pos.x][pos.y][pos.z]++;

                // Check for collision at this position
                if (matrix[pos.x][pos.y][pos.z] > 1) {
					
					collisions[total_collisions].time = t;
					collisions[total_collisions].position.x = pos.x;
					collisions[total_collisions].position.y = pos.y;
					collisions[total_collisions].position.z = pos.z;
					collisions[total_collisions].dronesUsed[0] = drones[j - 1].name;
					collisions[total_collisions].dronesUsed[1] = drones[j].name;
					
                    printf("!!!!!! Collision at t=%d -> (%d,%d,%d) involving %s\n",
                        t, pos.x, pos.y, pos.z, drones[j].name);

                    // Notify current drone
                    kill(pids[j], SIGUSR1);

                    // Increment collision count and terminate if needed
                    total_collisions++;
                    if (total_collisions >= MAX_COLLISIONS) {
                        printf("[Parent] Collision threshold reached. Terminating simulation.\n");

                        // Terminate all drones
                        for (int m = 0; m < drone_count; m++) {
                            kill(pids[m], SIGTERM);
                            drone_still_alive[m] = 0;
                        }
                        terminate = 1;
                        break;
                    }
                }
            } else {
				// Drone já terminou ou morreu
				drone_still_alive[j] = 0;
				positions[j].x = positions[j].y = positions[j].z = -1; // ou ignora
			} 	
		}
		
		// Verifica se todos os drones estão mortos
		int all_dead = 1;
		for (int j = 0; j < drone_count; j++) {
			if (drone_still_alive[j]) {
				all_dead = 0;
				break;
			}
		}
		if (all_dead) {
			printf("[Parent] All drones have finished. Ending simulation.\n");
			break;
		}
		
		for (int j = 0; j < drone_count; j++) {
			if (drone_still_alive[j]) {
				kill(pids[j], SIGUSR1);
			}
		}
	}
}

// ---------- Wait for drones to finish and clean up resources ----------

void cleanup_and_wait() {
    // Wait for all child processes to finish (cleanup)
    for (int i = 0; i < drone_count; i++) {
        waitpid(pids[i], NULL, 0);  // Wait for each drone to terminate
    }

    // Close pipes after drones are done
    for (int i = 0; i < drone_count; i++) {
        close(pipes[i][0]);  // Close read ends of pipes in parent
        close(pos_pipes[i][1]);  // Close write ends of position pipes in parent
    }
}

// ---------- Main Program ----------

int main() {
    char selected_figure[100];
    char selected_wind[100];
    char figure_path[256];
    char wind_path[256];
    char report_file[] = "report.txt";
    
    // Step 1: List available wind data for the user to choose from
    list_wind("wind");
    
    // Step 2: Prompt user for the name of the wind data to load
    ask_user_for_wind_data(selected_wind, sizeof(selected_wind));
    snprintf(wind_path, sizeof(wind_path), "wind/%s", selected_wind);
    
    // Step 3: Load the wind data
    if(load_wind_data(wind_path) != 0){
		return 1; // Error
	}
	
	printf("\n");

    // Step 4: List available figures for the user to choose from
    list_figures("figureData");

    // Step 5: Prompt user for the name of the figure to load

    ask_user_for_figure(selected_figure, sizeof(selected_figure));
    snprintf(figure_path, sizeof(figure_path), "figureData/%s", selected_figure);

    // Step 6: Load the figure and its drones

    if (load_figure_and_drones(figure_path) != 0) {
        return 1; // Error
    }

    // Step 7: Setup for the signal handlers used by the Parent process
    setup_parent_signal_handlers();
    
    // Step 8: Create a process for each drone & Step 5: Execute the drone's routine
    create_drone_processes_with_wind();

    // Step 9: Wait for user input to control simulation
    wait_for_user_input();

    // Step 10: Monitor for collisions
    detect_collisions();

    // Step 11: Wait for all child processes (drones) to finish
    cleanup_and_wait();
	
	// Step 12: Get the report file
    get_report(report_file);

    if (total_collisions >= MAX_COLLISIONS) {
        printf("\n--- All drones simulations have stopped due to collisions. ---\n");
    } else {
        printf("\n--- All drone simulations completed. ---\n");
    }

    return 0;
}

