# 📡 Distributed Drone Show Orchestration & Simulation System

---

> This repository was created as part of the **Integrative Project** in the 3rd semester of the Bachelor’s Degree in Informatics Engineering at **ISEP**, and was therefore developed as a group project.

---

## 👥 Team Members
- David Vieira
- Daniel Silva
- Rafael Barbosa
- Igor Coutinho
- Rui Vieira

---

## 1. 📝 Project Description

> *The "Distributed Drone Show Orchestration & Simulation System" is focused on creating a drone management and control system that integrates various aspects such as navigation, communication, and safety. The goal is to develop a robust platform that allows efficient management and monitoring of drone fleets, ensuring smooth operations and scalability. The project aims to build both a hardware-software solution that can be used in diverse environments like logistics, surveillance, and delivery systems.*

---

## 2. 📚 Planning and Technical Documentation

📄 **Detailed documentation:**  
[Planning and Technical Documentation](docs/readme.md)

---

## 3. 🛠️ How to Build

### Prerequisites
- Make sure **Maven** is installed and on the `PATH`.

> ⚠️ **Important:**  
> You **must use JDK 17** to build and run this project. Other versions (e.g., JDK 21 or 25) may cause build or runtime errors due to incompatibilities with some dependencies (such as Lombok).

**Quick Setup (Windows):**  
You can use the provided script [`use-jdk17.ps1`](use-jdk17.ps1) as an example to quickly set `JAVA_HOME` and `PATH` for JDK 17.  
_If your JDK 17 is installed in a different location, edit the script accordingly._


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

## 5. 🚀 How to Run

Make sure a JRE is installed and on the `PATH`.

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

- Make sure the Customer App Server is running before starting the Customer Application.

#### — Drone Runner

- **Windows:**
  ```powershell
  .\run-drone-runner.bat
  ```
- **Linux/MacOS:**
  ```sh
  ./run-drone-runner.sh
  ```

- Make sure the Simulator Server is running before starting the Drone Runner.

#### — Testing Application

- **Windows:**
  ```powershell
  .\run-testing-app.bat
  ```
- **Linux/MacOS:**
  ```sh
  ./run-testing-app.sh
  ```

- Make sure the Simulator Server and Drone Runner are running before starting the Testing Application.

---