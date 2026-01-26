# Testing App – Client Documentation

---
## Introduction
The Testing App is a Java client application used by technical staff to test and simulate drone shows. It allows users to initiate show simulations, validate show parameters, and interact with the Simulator Server.

---
## Communication

### Communicates With
- **Simulator Server** (S_Socket)

### Communication Protocol
- Communicates with the Simulator Server (C) via TCP socket (S_Socket).
- Uses a CSV-based, line-oriented protocol for all messages.

### General Message Structure
- Each message is a line of text, with fields separated by commas.
- Example: `COMMAND,param1,param2,...`

---
## Main Supported Commands (Sent to Simulator Server)
- `TEST_SHOW,<duration>,<numberOfDrones>,<latitude>,<longitude>`
  - Requests the simulation of a show with the specified parameters.
- `GET_NUM_DRONES`
  - Requests the number of drones expected in the current simulation (rarely used directly by Testing App, but supported by the server).

---
## Server Responses
- `SIMULATION_PENDING,Waiting for <n> Drone Runner(s)...`
  - Indicates the server is waiting for the required number of drones to connect.
- `SIMULATION_COMPLETE,Duration:<d>,Drones:<n>,Lat:<lat>,Lon:<lon>,Simulation completed successfully.`
  - Indicates the simulation has finished.
- `SERVER_ERROR,...` or `BAD_REQUEST,...`
  - Error messages in case of invalid requests or if a simulation is already in progress.

---
## Internal Operation
1. The user provides show parameters and initiates a simulation.
2. The Testing App sends a `TEST_SHOW` command to the Simulator Server.
3. It waits for responses and displays simulation status and results to the user.
4. The Testing App may be closed or used to initiate further simulations.

---
## Business and Architectural Characteristics
- Initiates and monitors drone show simulations.
- Communicates only with the Simulator Server (S_Socket).
- Does not accept incoming connections.
- Does not access the database directly for simulation.
- Does not control individual drones; all simulation requests are handled through the Simulator Server.

---