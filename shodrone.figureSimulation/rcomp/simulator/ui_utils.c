// ui_utils.c
#include "ui_utils.h"
#include "common.h" // For LOGGER_DEBUG
#include <string.h> // For strchr, fgets

/**
 * @brief Reads a line from standard input (console).
 *
 * This function reads characters from stdin until a newline character is encountered
 * or the buffer is full. It ensures the string is null-terminated and removes
 * the trailing newline if present.
 *
 * @param buffer Pointer to the character array where the line will be stored.
 * @param buffer_size The maximum size of the buffer, including the null terminator.
 * @return A pointer to the buffer on success, or NULL on error (e.g., EOF, buffer too small).
 */
char* console_read_line(char* buffer, size_t buffer_size) {
    if (buffer == NULL || buffer_size == 0) {
        LOGGER_DEBUG("Erro: buffer nulo ou tamanho do buffer zero.");
        return NULL;
    }

    // Use fgets to read a line safely
    if (fgets(buffer, (int)buffer_size, stdin) == NULL) {
        LOGGER_DEBUG("Erro ao ler do console ou EOF.");
        return NULL;
    }

    // Remove trailing newline character, if present
    buffer[strcspn(buffer, "\n")] = '\0';

    return buffer;
}

