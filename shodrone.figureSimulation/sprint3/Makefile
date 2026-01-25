all: droneFigure

droneFigure: main.o drone.o monitor.o shared_resources.o utils.o
	gcc -Wall -pthread -o droneFigure main.o drone.o monitor.o shared_resources.o utils.o

main.o: main.c types.h drone.h
	gcc -Wall -c main.c

drone.o: drone.c types.h drone.h
	gcc -Wall -c drone.c

monitor.o: monitor.c monitor.h main.h
	gcc -Wall -c monitor.c

shared_resources.o: shared_resources.c shared_resources.h
	gcc -Wall -c shared_resources.c

utils.o: utils.c utils.h
	gcc -Wall -c utils.c

clean:
	rm -f *.o droneFigure
