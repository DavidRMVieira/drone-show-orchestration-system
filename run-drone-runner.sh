#!/bin/bash

# Build the Drone Runner if not already built
# (Uncomment the next line if you want to always build before running)
# make -C shodrone.figureSimulation/rcomp/dronerunner

# Run the Drone Runner executable
shodrone.figureSimulation/rcomp/dronerunner/drone_runner

# Pause for user input (optional)
read -p "Press any key to continue... " -n1 -s
