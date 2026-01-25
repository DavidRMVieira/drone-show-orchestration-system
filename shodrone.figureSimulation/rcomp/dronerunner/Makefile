# Makefile para o projeto Drone Runner

# Nome do executável
TARGET = drone_runner

# Compilador C
CC = gcc

# Opções de compilação
CFLAGS = -Wall -Wextra -std=c11 -D_POSIX_C_SOURCE=200809L

# Bibliotecas a serem linkadas (para threads e funções de rede)
LDFLAGS = -pthread

# Ficheiros fonte do Drone Runner
# Inclui todos os ficheiros necessários para o Drone Runner, incluindo os comuns e específicos do cliente
SRCS = drone_runner_main.c drone_runner.c csv_simulator_protocol_proxy.c \
       client_socket.c simulator_protocol.c \
       failed_request_exception.c ui_utils.c # common.c não é necessário pois common.h já é incluído

# Ficheiros objeto do Drone Runner
OBJS = $(SRCS:.c=.o)

.PHONY: all clean

# Regra padrão: compilar o executável do Drone Runner
all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(OBJS) -o $(TARGET) $(LDFLAGS)

# Regra para compilar ficheiros .c em ficheiros .o
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Regra para limpar ficheiros gerados
clean:
	rm -f $(OBJS) $(TARGET)
