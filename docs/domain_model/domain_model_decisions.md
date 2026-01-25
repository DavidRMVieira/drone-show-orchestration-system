# Domain Model Decisions

This section explains the reasoning behind the relationships between entities and value objects, based on the requirements described in the project brief.

## 📚 Overview

The system is organized into **aggregates**, each containing its **root entity**, **associated entities**, and **value objects**.

---

## 📦 Aggregates

### 🔐 Aggregate `User`

| Element        | Type         | Description                             |
|----------------|--------------|-----------------------------------------|
| `ShowOurUser`  | Root Entity  | Represents a system user.               |
| `EmailAddress` | Value Object | User's email.                           |
| `PhoneNumber`  | Value Object | User's phone number.                    |
| `Role`         | Value Object | User's role within the system.          |

- **Explanation:**

  - The user is someone who has access to the system.
  - It includes a valid email that belongs to the Shodrone domain.
  - Besides the name and password (already handled by the framework), it also includes a phone number.
  - Roles are modeled as a collection because a user may have multiple responsibilities in the future.

###
### 🧑‍💼 Aggregate `Customer`

| Element          | Type         | Description                                  |
|------------------|--------------|----------------------------------------------|
| `Customer`       | Root Entity  | Represents a customer.                       |
| `VAT`            | Value Object | European VAT number.                         |
| `Address`        | Value Object | Physical address.                            |
| `CustomerState`  | Value Object | Current state of the customer.               |
| `Representative` | Entity       | Representative associated with the customer. |
| `Position`       | Value Object | Role or title of the representative.         |

- **Explanation:**

  - Customers have a state: Deleted, Infringement, Created, Regular, or VIP.
  - A customer can have multiple representatives.
  - Representatives interact with the system, hence they are linked to ShowOurUser.

###
### 🎭 Aggregate `Show`

| Element         | Type         | Description                            |
|-----------------|--------------|----------------------------------------|
| `Show`          | Root Entity  | Represents a scheduled show.           |
| `Place`         | Value Object | Location of the show.                  |
| `Date`          | Value Object | Date of the show.                      |
| `Duration`      | Value Object | Duration of the show.                  |
| `SimulationLog` | Entity       | Simulation log for the show.           |

- **Explanation:**

  - A show consists of a date, place, and duration.
  - The simulation component belongs to an external system, so only the `SimulationLog` is recorded in the domain, storing the result (success/failure) and the date of the simulation.

###
### 🎭 Aggregate `DroneInShow`

| Element        | Type         | Description                              |
|----------------|--------------|------------------------------------------|
| `DroneInShow`  | Root Entity  | Represents a drone in a specific show.   |

- **Explanation:**

  - This aggregate was introduced to handle the many-to-many relationship between Drone and Show.

###
### 🎭 Aggregate `FigureInShow`

| Element         | Type         | Description                             |
|-----------------|--------------|-----------------------------------------|
| `FigureInShow`  | Root Entity  | Represents a figure in a specific show. |

- **Explanation:**

  - This aggregate was introduced to handle the many-to-many relationship between Figure and Show.

###
### ✨ Aggregate `Figure`

| Element          | Type         | Description                               |
|------------------|--------------|-------------------------------------------|
| `Figure`         | Root Entity  | Represents a drone choreography figure.   |
| `Description`    | Value Object | Textual description of the figure.        |
| `FigureCode`     | Value Object | Unique identifier code.                   |
| `FigureType`     | Value Object | Type of the figure (static or dynamic).   |
| `FigureVersion`  | Value Object | Version of the figure.                    |
| `DSLDescription` | Entity       | Domain-Specific Language (DSL) code.      |
| `DSLId`          | Value Object | Identifier of the DSL.                    |
| `DSLType`        | Value Object | Type of the DSL (geometric or 3D bitmap). |
| `DSLVersion`     | Value Object | Version of the DSL used.                  |

- **Explanation:**

  - A figure includes a code, description, version, and DSL code/description.
  - DSL also includes its own identifier and version.
  - A figure may be exclusive to a single customer (0..1 relationship).
  - DSL is linked to a specific DroneModel since it is designed for a particular type of drone.

###
### 🚁 Aggregate `Drone`

| Element           | Type         | Description                             |
|-------------------|--------------|-----------------------------------------|
| `Drone`           | Root Entity  | Represents a physical drone.            |
| `DroneState`      | Value Object | Current state of the drone.             |
| `SerialNumber`    | Value Object | Unique serial number identifying drone. |
| `AcquisitionDate` | Value Object | Date the drone was acquired.            |

- **Explanation:**

  - Each drone has a state (active, removed, broken).
  - Drones are associated with a specific DroneModel.

###
### 🚁 Aggregate `DroneModel`

| Element        | Type         | Description                       |
|----------------|--------------|-----------------------------------|
| `DroneModel`   | Root Entity  | Represents a model/type of drone. |
| `Name`         | Value Object | Name of the drone model.          |
| `Manufacturer` | Entity       | Manufacturer of the drone model.  |

- **Explanation:**

  - According to the client, "Model" and "Type" of drone are equivalent, so only one entity was used.
  - The model contains a name and a reference to the manufacturer.

###
### 📩 Aggregate `ShowRequest`

| Element        | Type         | Description                            |
|----------------|--------------|----------------------------------------|
| `ShowRequest`  | Root Entity  | Request made by a customer for a show. |
| `Description`  | Value Object | Description of the request.            |
| `NumberDrones` | Primitive    | Estimated number of drones required.   |
| `Place`        | Value Object | Location for the show.                 |
| `Date`         | Value Object | Desired date.                          |
| `Duration`     | Value Object | Desired duration.                      |
| `RequestState` | Value Object | State of the request.                  |

- **Explanation:**

  - A show request includes a description, drone count, location, date, and duration.
  - A state field indicates if the request was accepted.
  - ShowRequest is related to a Customer since their representatives make requests on their behalf.

###
### 📄 Aggregate `ShowProposal`

| Element              | Type         | Description                                                   |
|----------------------|--------------|---------------------------------------------------------------|
| `ShowProposal`       | Root Entity  | Proposal for a show in response to a request.                 |
| `Place`              | Value Object | Location of the proposed show.                                |
| `Date`               | Value Object | Proposed date.                                                |
| `Duration`           | Value Object | Proposed duration.                                            |
| `ProposalValidation` | Value Object | Validation status of the proposal (accepted, rejected, etc).  |

- **Explanation:**

  - A show proposal includes location, date, and duration (which may differ from the original request).
  - ShowProposal is linked to FigureInShow to specify proposed figures.
  - It also has a validation state to indicate whether the proposal was accepted or rejected.
  - Multiple proposals may be linked to a single ShowRequest.

---
