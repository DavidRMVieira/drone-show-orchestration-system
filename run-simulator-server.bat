@echo off
REM Build the Simulator Server if not already built
REM (Uncomment the next line if you want to always build before running)
REM make -C shodrone.figureSimulation\rcomp\simulator

REM Run the Simulator Server executable
shodrone.figureSimulation\rcomp\simulator\simulator_main.exe

pause
