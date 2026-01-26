# Drone Runner – Client Documentation

---
## Introduction
The Drone Runner is a C client application that simulates the behavior of a single drone in a show. Each instance of Drone Runner represents one drone and connects to the Simulator Server to receive movement commands and participate in the simulation.

---
## Communication

### Communicates With
- **Simulator Server** (R_Socket)

### Communication Protocol
- Communicates with the Simulator Server (C) via TCP socket (R_Socket).
- Uses a CSV-based, line-oriented protocol for all messages.

### General Message Structure
- Each message is a line of text, with fields separated by commas.
- Example: `COMMAND,param1,param2,...`


---
## Main Supported Commands (Sent to Simulator Server)
- `DRONE_INIT,<drone_id>`
	- Registers this drone as ready to participate in the simulation.
- `GET_NUM_DRONES`
	- Requests the number of drones expected in the current simulation (usually used at startup to let the user select a valid drone ID).

---
## Server Responses
- `DRONE_READY,Ready to receive commands for Drone ID <id>.`
	- Confirms the drone is registered and ready.
- `COMMAND,MOVE_TO_POS,<x>,<y>,<z>`
	- Commands the drone to move to a specific position.
- `END_SIMULATION,Simulation for Drone <id> finished.`
	- Indicates the end of the simulation for this drone.
- `NUM_DRONES,<n>`
	- Response to `GET_NUM_DRONES` with the number of drones in the simulation.
- `ERROR,...` or `BAD_REQUEST,...`
	- Error messages in case of malformed commands or file reading issues.

---
## Internal Operation
1. At startup, the user requests the number of drones from the Simulator Server and selects a drone ID.
2. The Drone Runner sends a `DRONE_INIT,<drone_id>` command to the Simulator Server.
3. It waits for and processes movement commands (`COMMAND,MOVE_TO_POS,...`) from the server, simulating the drone's actions.
4. When the server sends `END_SIMULATION`, the Drone Runner terminates the simulation and disconnects.

---
## Business and Architectural Characteristics
- Simulates a single drone in a show, executing movement commands from the Simulator Server (R_Socket).
- Does not accept incoming connections.
- Does not access the database.
- Does not interact with the Customer App or Customer App Server.
- Communicates only with the Simulator Server.

---