// failed_request_exception.h
#ifndef FAILED_REQUEST_EXCEPTION_H
#define FAILED_REQUEST_EXCEPTION_H

// Enumeração para diferentes tipos de erros de requisição
typedef enum {
    FR_SUCCESS = 0,
    FR_SERVER_ERROR,
    FR_BAD_REQUEST,
    FR_UNKNOWN_REQUEST,
    FR_ERROR_IN_REQUEST,
    FR_GENERIC_ERROR // Para erros não especificados
} FailedRequestException_t;

/**
 * @brief Obtém a mensagem de erro correspondente a um código de exceção.
 * @param error_code O código de erro.
 * @return Uma string constante com a mensagem de erro.
 */
const char* get_failed_request_message(FailedRequestException_t error_code);

#endif // FAILED_REQUEST_EXCEPTION_H
