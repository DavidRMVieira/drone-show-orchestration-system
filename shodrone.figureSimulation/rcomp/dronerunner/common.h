// common.h
#ifndef COMMON_H
#define COMMON_H

#include <stdio.h>
#include <stdbool.h>

#define DEBUG_MODE true
#define LOGGER_DEBUG(fmt, ...) \
    do { if (DEBUG_MODE) fprintf(stderr, "[DEBUG] %s:%d:%s(): " fmt "\n", __FILE__, \
                            __LINE__, __func__, ##__VA_ARGS__); } while (0)

#endif // COMMON_H
