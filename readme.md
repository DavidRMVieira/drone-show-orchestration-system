# 📡 Distributed Drone Show Orchestration & Simulation System

---

<br>

```
⚠️ This repository was created as part of the Integrative Project in the 3rd semester of the Bachelor’s Degree in Informatics Engineering at ISEP, and was therefore developed as a group project.

Team Memberss / Credits:
  - David Vieira
  - Daniel Silva
  - Rafael Barbosa
  - Igor Coutinho
  - Rui Vieira
```

<br>

---

## 1. 📝 System Overview

- *The "Distributed Drone Show Orchestration & Simulation System" is a distributed platform for managing, orchestrating, and simulating multimedia drone shows. It integrates a Java-based backoffice for customer and show management with a high-performance C simulation engine for real-time, scalable collision detection and drone fleet coordination. The system is designed for extensibility, safety, and large-scale entertainment scenarios, supporting robust navigation, communication, and monitoring of drone fleets. Its flexible architecture also enables adaptation to other domains such as logistics, surveillance, and delivery. Persistence options (JPA/Hibernate or In-Memory) and modular configuration allow easy deployment and testing across different environments.*

- *The system can be run using servers created locally or deployed on the DEI (Departamento de Engenharia Informática, ISEP) infrastructure. The DEI servers provide virtualized computing, storage, and networking resources (VS-Cloud), accessible via VPN and supporting remote management, SSH/SMB/CIFS storage, and web hosting. This flexibility allows the platform to be used for both local development and production-like deployments on institutional infrastructure.*

---

## 2. 📚 Planning and Technical Documentation

📄 **Detailed documentation:**  
- [Planning and Technical Documentation](docs/planning_technical_documentation.md)

---

## 3. 🛠️ How to Build

### Prerequisites
- Make sure **Maven** is installed and on the `PATH`.

> ⚠️ **Important:**  
> You **must use JDK 17** to build and run this project. Other versions (e.g., JDK 21 or 25) may cause build or runtime errors due to incompatibilities with some dependencies (such as Lombok).

**Quick Setup (Windows):**  
You can use the provided script [`use-jdk17.ps1`](use-jdk17.ps1) as an example to quickly set `JAVA_HOME` and `PATH` for JDK 17.  
If your JDK 17 is installed in a different location, edit the script accordingly.


### Run Build Script

- **Windows:**
  ```powershell
  .\build-all.bat
  ```
  Or, for a faster build (skips javadoc and copies dependencies):
  ```powershell
  .\quickbuild.bat
  ```
- **Linux/MacOS:**
  ```sh
  ./build-all.sh
  ```

---

## 4. 🧪 How to Execute Tests

Make sure a JRE is installed and on the `PATH`.

- **Windows:**
  ```powershell
  .\run-tests.bat
  ```
- **Linux/MacOS:**
  ```sh
  ./run-tests.sh
  ```

---


## 5. 🌐 Server Applications

The Server Applications can be run either locally (localhost) or on DEI servers.

### How to configure the environment

- The host and port for the servers are read from configuration files in each client app:
  - Customer App: [(click here)](shodrone.app.customer/src/main/resources/customerapp.properties)
  - Testing App: [(click here)](shodrone.app.testing/src/main/resources/testingapp.properties)

- Local environment (default):
  ```properties
  server.host=localhost
  server.port=8080  # or 10005 for the Simulator
  ```

- DEI environment (uncomment the lines in the respective file):
  ```properties
  server.host=vs791.dei.isep.ipp.pt  # Customer App Server
  server.port=8080
  ```
  ```properties
  server.host=vs903.dei.isep.ipp.pt  # Simulator Server
  server.port=10005
  ```

### How to run the locally servers (default):

**Customer App Server**
  - Windows:
    ```bat
    .\run-customerapp-server.bat
    ```
  - Linux/MacOS:
    ```sh
    ./run-customerapp-server.sh
    ```

**Simulator Server**
  - Windows:
    ```bat
    .\run-simulator-server.bat
    ```
  - Linux/MacOS:
    ```sh
    ./run-simulator-server.sh
    ```

> ⚠️ To change the environment, simply edit the module's configuration file. Recompiling the code is not necessary.

---

## 6. 🗄️ Persistence Options: JPA/Hibernate vs In-Memory

You can choose between two persistence strategies for running the applications:

- **JPA/Hibernate (default):** Uses a database for persistent storage (recommended for production or realistic testing).
- **In-Memory:** Stores all data in memory (no database required, resets on restart; useful for demos or quick tests).

### How to select the persistence mode

Each application has a configuration file (e.g.,
[`shodrone.app.testing/src/main/resources/application.properties`](shodrone.app.testing/src/main/resources/application.properties))
with a property to select the persistence mode:

```properties
# For JPA/Hibernate (default):
persistence.repositoryFactory=lapr4.JpaRepositoryFactory

# For In-Memory:
#persistence.repositoryFactory=lapr4.InMemoryRepositoryFactory
```

**To switch modes:**
- Uncomment the desired line and comment out the other.
- No need to recompile the project after changing this setting.

---

## 7. 🚀 How to Run


### Backoffice Application

- **Windows:**
  ```powershell
  .\run-backoffice.bat
  ```
- **Linux/MacOS:**
  ```sh
  ./run-backoffice.sh
  ```


### Server Clients Applications

#### — Customer Application

- **Windows:**
  ```powershell
  .\run-customer-app.bat
  ```
- **Linux/MacOS:**
  ```sh
  ./run-customer-app.sh
  ```

> ⚠️ Make sure the Customer App Server is running before starting the Customer Application.

#### — Drone Runner

- **Windows:**
  ```powershell
  .\run-drone-runner.bat
  ```
- **Linux/MacOS:**
  ```sh
  ./run-drone-runner.sh
  ```

> ⚠️ Make sure the Simulator Server is running before starting the Drone Runner.

#### — Testing Application

- **Windows:**
  ```powershell
  .\run-testing-app.bat
  ```
- **Linux/MacOS:**
  ```sh
  ./run-testing-app.sh
  ```

> ⚠️ Make sure the Simulator Server and Drone Runner are running before starting the Testing Application.

---
