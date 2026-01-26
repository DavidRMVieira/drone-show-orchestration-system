# Distributed System Architecture & Protocols

## Overview

![System Servers-Clients Architecture Diagram](system_architecture_servers-clients.svg)

The diagram above reflects the actual implemented architecture, including all flows and responsibilities of the servers and clients.

---

The system is composed of multiple networked components, each with well-defined responsibilities, enabling the management, simulation, and testing of drone shows. The architecture strictly follows the requirements and implementation described in the project statement and codebase.


## 1. Components and Roles

### A. Customer Services

- **[Customer App](clients/customer_app/Customer_App.md)**
  - Used by end customers to submit proposals, view scheduled shows, and interact with the system.
  - Does not have any direct access to the database.
  - Communicates exclusively via TCP socket with the Customer App Server (CAS).
  - Implemented in Java.

- **[Customer App Server (CAS)](servers/customer_app_server/CAS.md)**
  - Acts as an intermediary between the Customer App and the database/business logic.
  - Receives requests from Customer App via TCP socket (CAS_Socket).
  - Executes operations on the database and returns responses to the client.
  - Supports multiple simultaneous clients (multithreaded server).
  - Implemented in Java.

### B. Simulation & Testing

- **[Testing App](clients/testing_app/Testing_App.md)**
  - Used by technical staff to test and simulate shows.
  - Has direct access to the database only for validation and queries (not for simulation execution).
  - All simulation requests are sent to the Simulator Server.
  - Communicates via TCP socket with the Simulator Server (S_Socket).
  - Is a client application, not a server.
  - Implemented in Java.

- **[Simulator Server](Servers/simulator_server/Simulator.md)**
  - Simulates the behavior of drones and the execution of shows.
  - Accepts connections from:
    - Testing App (for simulation/test requests)
    - Drone Runner (to simulate individual drones)
  - Supports multiple simultaneous clients (Testing Apps and Drone Runners).
  - Uses TCP sockets for communication (S_Socket and R_Socket).
  - Implemented in C.

- **[Drone Runner](clients/drone_runner/Drone_Runner.md)**
  - Each instance simulates a single drone.
  - Communicates via TCP socket only with the Simulator Server (R_Socket).
  - Does not have any access to the database.
  - Implemented in C.

---

## 2. Network Connections (Sockets)

- **CAS_Socket:** Customer App <-> Customer App Server
- **S_Socket:** Testing App <-> Simulator Server
- **R_Socket:** Drone Runner <-> Simulator Server

---

## 3. Key Observations

- Only the Customer App Server and the Simulator Server are true servers (accept multiple incoming connections).
- Testing App and Drone Runner are clients (they initiate connections, do not accept them).
- All protocols are based on CSV-formatted messages over TCP/IP sockets.
- Database access is strictly controlled according to each component's role. Customer App and Drone Runner never access the database directly.
- The architecture is designed for clear separation of concerns, concurrency, and extensibility.

---
