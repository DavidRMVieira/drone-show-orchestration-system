# Customer App Server (CAS) – Protocol Description and Operation

## Introduction
The Customer App Server (CAS) is responsible for managing communication between clients and the show management system, enabling operations such as proposal submission and analysis, querying scheduled shows, among others. CAS uses a custom application protocol based on CSV (Comma-Separated Values) messages.

## Communication Protocol
The CAS protocol is based on the exchange of text messages, where each message represents a request or response, with fields separated by commas. The server interprets each received line, identifies the type of request, and executes the corresponding action.

### General Message Structure
- Each message consists of a command (keyword) followed by the required parameters, separated by commas.
- Generic example: `COMMAND,param1,param2,...`

### Main Supported Commands
Below are some examples of commands supported by CAS:

- **LIST_SHOWS**: Lists all scheduled shows for the client.
  - Example: `LIST_SHOWS,client_id`
- **SHOW_INFO**: Retrieves detailed information about a show.
  - Example: `SHOW_INFO,show_id`
- **LIST_PROPOSALS**: Lists proposals submitted by the client.
  - Example: `LIST_PROPOSALS,client_id`
- **ACCEPT_PROPOSAL**: Accepts a show proposal.
  - Example: `ACCEPT_PROPOSAL,proposal_id`
- **REJECT_PROPOSAL**: Rejects a show proposal.
  - Example: `REJECT_PROPOSAL,proposal_id`

The protocol can be easily extended to support new commands by implementing new request classes.

### Server Responses
Responses also follow the CSV format and may contain requested data (lists, details) or error/success messages.
- Example of a success response: `SUCCESS,Operation completed successfully.`
- Example of an error response: `ERROR,Detailed error message.`

## Internal Operation
1. CAS receives a message from the client.
2. The parser (`CsvCustomerAppProtocolMessageParser`) interprets the message and instantiates the corresponding request.
3. The request is executed, using application controllers to access business logic.
4. The result is returned to the client in the same CSV format.

## Security and Authentication
CAS integrates an authentication service to validate client requests, ensuring that only authorized users can perform operations.

## Extensibility
The protocol is designed to be simple and extensible, facilitating the addition of new commands and functionalities without altering the base structure.

---
