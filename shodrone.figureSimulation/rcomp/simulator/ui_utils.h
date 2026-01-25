// ui_utils.h
#ifndef UI_UTILS_H
#define UI_UTILS_H

#include <stddef.h> // For size_t

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
char* console_read_line(char* buffer, size_t buffer_size);

#endif // UI_UTILS_H

