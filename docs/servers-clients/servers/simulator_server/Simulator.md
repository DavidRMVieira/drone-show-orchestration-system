
# Simulator Server – Protocol and Operation

## Introduction
The Simulator Server simulates drone show executions, handling connections from two types of clients:
- **Testing App**: Requests the simulation of a drone show.
- **Drone Runner**: Each instance represents a single drone participating in the simulation.

The server is implemented in C and communicates using a custom CSV-based protocol over TCP sockets.

## Communication Protocol
The Simulator Server uses text-based messages, where each message is a line with fields separated by commas. The server distinguishes between Testing App and Drone Runner clients, each with its own set of supported commands.

### General Message Structure
- Each message consists of a command (keyword) followed by required parameters, separated by commas.
- Example: `COMMAND,param1,param2,...`

## Supported Commands

### Commands Received by Simulator Server

#### From Testing App
- `TEST_SHOW,<duration>,<numberOfDrones>,<latitude>,<longitude>`
  - Requests the simulation of a show with the given parameters.

#### From Drone Runner
- `DRONE_INIT,<drone_id>`
  - Registers a drone as ready to participate in the simulation.
- `GET_NUM_DRONES`
  - Requests the number of drones expected in the current simulation.

### Commands and Responses Sent by Simulator Server

#### To Testing App
- `SIMULATION_PENDING,Waiting for <n> Drone Runner(s)...`
  - Indicates the server is waiting for the required number of drones to connect.
- `SIMULATION_COMPLETE,Duration:<d>,Drones:<n>,Lat:<lat>,Lon:<lon>,Simulation completed successfully.`
  - Indicates the simulation has finished.
- `SERVER_ERROR,...` or `BAD_REQUEST,...`
  - Error messages in case of invalid requests or if a simulation is already in progress.

#### To Drone Runner
- `DRONE_READY,Ready to receive commands for Drone ID <id>.`
  - Confirms the drone is registered and ready.
- `COMMAND,MOVE_TO_POS,<x>,<y>,<z>`
  - Commands the drone to move to a specific position (sent according to the drone's movement file).
- `END_SIMULATION,Simulation for Drone <id> finished.`
  - Indicates the end of the simulation for that drone.
- `NUM_DRONES,<n>`
  - Response to `GET_NUM_DRONES` with the number of drones in the simulation.
- `ERROR,...` or `BAD_REQUEST,...`
  - Error messages in case of malformed commands or file reading issues.

## Roles in the System

- **Simulator Server**: Receives and processes only the commands listed above, from Testing App and Drone Runner clients.
- **Drone Runner**: Connects to the server, requests the number of drones, registers with its ID, and executes commands received from the server.
