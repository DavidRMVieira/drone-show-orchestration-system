# Makefile para o projeto Simulator

# Nome do executável
TARGET = simulator

# Compilador C
CC = gcc

# Opções de compilação
CFLAGS = -Wall -Wextra -std=c11 -D_POSIX_C_SOURCE=200809L

# Bibliotecas a serem linkadas (para threads)
LDFLAGS = -pthread

# Ficheiros fonte do Simulator
# Inclui todos os ficheiros necessários para o Simulator, incluindo os comuns e específicos
SRCS = simulator_main.c simulator_server.c simulator_protocol.c simulator_state.c \
       ui_utils.c # common.c não é necessário pois common.h já é incluído

# Ficheiros objeto do Simulator
OBJS = $(SRCS:.c=.o)

.PHONY: all clean

# Regra padrão: compilar o executável do Simulator
all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(OBJS) -o $(TARGET) $(LDFLAGS)

# Regra para compilar ficheiros .c em ficheiros .o
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Regra para limpar ficheiros gerados
clean:
	rm -f $(OBJS) $(TARGET)
