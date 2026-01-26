@echo off
REM Build the Simulator Server if not already built
REM (Uncomment the next line if you want to always build before running)
REM make -C shodrone.figureSimulation\rcomp\simulator

REM Run the Simulator Server executable
REM Path to the executable
SET SIM_EXE=shodrone.figureSimulation\rcomp\simulator\simulator_main.exe


IF EXIST "%SIM_EXE%" (
	echo Running Simulator Server...
	"%SIM_EXE%"
) ELSE (
	echo [ERROR] Simulator Server executable not found!
	echo Please build first: make -C shodrone.figureSimulation\rcomp\simulator
)

pause
