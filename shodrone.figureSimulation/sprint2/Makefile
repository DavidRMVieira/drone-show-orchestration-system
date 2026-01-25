# Copied from https://en.ittrip.xyz/c-language/c-drone-flight-control; adapt according

all: droneFigure

droneFigure: main.o drone.o
	gcc -Wall -o droneFigure main.o drone.o

main.o: main.c types.h drone.h
	gcc -Wall -c main.c

drone.o: drone.c types.h drone.h
	gcc -Wall -c drone.c

clean:
	rm -f *.o droneFigure