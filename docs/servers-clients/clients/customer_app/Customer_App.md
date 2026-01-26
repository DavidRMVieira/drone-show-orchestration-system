# Customer App – Client Documentation

---
## Introduction
The Customer App is a Java client application designed for end customers to submit show proposals, view scheduled shows, and interact with the system. It provides a user-friendly interface for non-technical users to access the main business features of the drone show orchestration system.

---
## Communication

### Communicates With
- **Customer App Server** (CAS_Socket)

### Communication Protocol
- Communicates exclusively with the Customer App Server (CAS) via TCP socket (CAS_Socket).
- Uses a CSV-based, line-oriented protocol for all messages.

### General Message Structure
- Each message is a line of text, with fields separated by commas.
- Example: `COMMAND,param1,param2,...`

---
## Main Supported Commands (Sent to Customer App Server)
- `SUBMIT_PROPOSAL,<proposal_data>`
  - Submits a new show proposal.
- `LIST_SHOWS`
  - Requests a list of scheduled shows.
- `GET_SHOW_INFO,<show_id>`
  - Requests detailed information about a specific show.
- `EVALUATE_PROPOSAL,<proposal_id>,<decision>`
  - Sends a decision (approve/reject) for a proposal.

---
## Server Responses
- Responses follow the CSV format and may include:
  - Lists of shows, proposal status, or error messages (e.g., `SERVER_ERROR,...`, `BAD_REQUEST,...`).

---
## Internal Operation
1. The user interacts with the GUI or console to perform actions (submit proposals, view shows, etc.).
2. The Customer App sends the appropriate command to the Customer App Server.
3. It waits for and displays the server's response.

---
## Business and Architectural Characteristics
- Allows customers to submit proposals and view show information.
- Communicates only with the Customer App Server (CAS_Socket).
- Does not accept incoming connections.
- Does not access the database directly.
- Does not interact with the Simulator Server.
- All business logic and data access are handled by the Customer App Server.

---