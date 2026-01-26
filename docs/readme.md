# Planning and Technical Documentation

## 1. Planning

### 1.1. Project Objectives
The **Shodrone** project aims to develop a back-office support system for Shodrone, a company specialized in creating customized multimedia drone shows.

The system will provide the following functionalities:

- **Customer management**, including registration and data updates.
- **Customer request management**, supporting creation, analysis, validation, acceptance/rejection, and cancellation of requests, as well as handling figure exclusivity periods.
- **Management of the figure/sequence library**, where each figure contains drone-specific scripts described in a DSL (Domain-Specific Language). This DSL code is generated externally and imported into the system.
- **Simulation of individual figures**, checking for potential drone collisions using a 3D time-based simulation grid (e.g., 1m³ per cell with discrete time steps).
- **Simulation of full shows**, composed of multiple figures/sequences, with coordination via a central orchestrator (or "maestro") server. The orchestrator triggers figure execution and collects feedback from drones upon completion.
- **Scalable simulation**, to support large-scale shows with hundreds of drones. The airspace can be partitioned into multiple subareas, each simulated by a dedicated server. The system must handle drone movement between areas seamlessly.

This system aims to prepare Shodrone for large, highly customized drone shows, positioning the company as a leader in the drone-based entertainment industry.

### 1.2 Sprints

| Sprint   | Dates                    |
|----------|--------------------------|
| Sprint 1 | 2025-03-17 to 2025-04-06 |
| Sprint 2 | 2025-04-07 to 2025-05-18 |
| Sprint 3 | 2025-05-19 to 2025-06-15 |

### 1.3 Team

| Number  | Name           |
|---------|----------------|
| 1230487 | David Vieira   |
| 1231046 | Daniel Silva   |
| 1230544 | Rafael Barbosa |
| 1230543 | Igor Coutinho  |
| 1211252 | Rui Vieira     |


## 2. Technical Documentation

### 2.1 Architecture

The application follows a typical layered approach

    UI -> Controller -+-> Domain
                      |     ^
                      |     |
                      +-> Repositories

### 2.2 System Internal Layer Communication

![System Internal Layer Architecture](system_architecture/system-internal-layer-architecture.svg)

### 2.3 Technologies Used
- **Programming Languages**: Java, C and ANTLR
- **Database**: ...
- **Framework**: Eapli Framework
- **Tests**: JUnit

### 2.4 Domain Model

![Domain Model](domain_model/domain-model.svg)


### 2.5 Distributed System Architecture & Protocols

![System Servers-Clients Architecture Diagram](servers-clients/distributed_system_architecture.svg)

#### 2.5.1. Servers and Clients Overview
For a detailed description of the networked components, their roles, and communication flows, see:
**[Servers and Clients Architecture](servers-clients/distributed_system_architecture.md)**

#### 2.5.2. Communication Protocols
- Customer App Server (CAS) protocol: [more](servers-clients/servers/customer_app_server/CAS.md)
- Simulator Server protocol: [more](servers-clients/servers/simulator_server/Simulator.md)

---