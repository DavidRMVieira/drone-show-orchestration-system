#!/bin/bash

# Build the Simulator Server if not already built
# (Uncomment the next line if you want to always build before running)
# make -C shodrone.figureSimulation/rcomp/simulator

# Run the Simulator Server executable
shodrone.figureSimulation/rcomp/simulator/simulator_main

# Pause for user input (optional)
read -p "Press any key to continue... " -n1 -s
