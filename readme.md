# Distributed Drone Show Orchestration & Simulation System

---

<br>

```
⚠️ This repository was created as part of the Integrative Project in the 3rd semester of the Bachelor’s Degree in Informatics Engineering at ISEP, and was therefore developed as a group project.

Team Members:
 - David Vieira
 - Daniel Silva
 - Rafael Barbosa
 - Igor Coutinho
 - Rui Vieira
```
<br>

---

## 1. Description of the Project
 
*The "Distributed Drone Show Orchestration & Simulation System" is focused on creating a drone management and control system that integrates various aspects such as navigation, communication, and safety. The goal is to develop a robust platform that allows efficient management and monitoring of drone fleets, ensuring smooth operations and scalability. The project aims to build both a hardware-software solution that can be used in diverse environments like logistics, surveillance, and delivery systems.*

## 2. Planning and Technical Documentation

[Planning and Technical Documentation](docs/readme.md)

## 3. How to Build


Make sure Maven is installed and on the PATH.

**Important:** You must use JDK 17 to build and run this project. Other versions (e.g., JDK 21 or 25) may cause build or runtime errors due to incompatibilities with some dependencies (such as Lombok).

You can use the provided script [`use-jdk17.ps1`](use-jdk17.ps1) (Windows) as an example to quickly set JAVA_HOME and PATH for JDK 17. If your JDK 17 is installed in a different location, edit the script accordingly.

run script:

- Windows:

        .\build-all.bat
- Linux/MacOs:

        ./build-all.sh

## 4. How to Execute Tests

make sure a JRE is installed and on the PATH

run script:

- Windows:

        .\run-tests.bat
- Linux/MacOs:

        ./run-tests.sh

## 5. How to Run

make sure a JRE is installed and on the PATH

run script:

- Windows:

        .\run-backoffice.bat
    or

        .\run-customer-app.bat

    you might want to initialize the database first by bootstraping some demo data

        .\run-bootstrap.bat

- Linux/MacOs:

        ./run-backoffice.sh
    or
        
        ./run-customer-app.sh

  you might want to initialize the database first by bootstraping some demo data

        ./run-bootstrap.sh

## 6. How to Install/Deploy into Another Machine (or Virtual Machine)

*To Do*

## 7. How to Generate PlantUML Diagrams

To generate plantuml diagrams for documentation execute the script (linux/unix/macos):

    ./generate-plantuml-diagrams.sh


