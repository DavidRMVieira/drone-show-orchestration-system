#include "failed_request_exception.h"
#include <string.h>

const char* get_failed_request_message(FailedRequestException_t error_code) {
    switch (error_code) {
        case FR_SUCCESS:
            return "Sucesso";
        case FR_SERVER_ERROR:
            return "SERVER_ERROR: Erro interno do servidor.";
        case FR_BAD_REQUEST:
            return "BAD_REQUEST: Requisição mal formatada.";
        case FR_UNKNOWN_REQUEST:
            return "UNKNOWN_REQUEST: Requisição desconhecida.";
        case FR_ERROR_IN_REQUEST:
            return "ERROR_IN_REQUEST: Erro ao processar a requisição.";
        case FR_GENERIC_ERROR:
        default:
            return "Erro desconhecido na requisição.";
    }
}
