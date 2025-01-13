package org.svalero.memesconclase.domain.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ErrorResponse {

    private int code; // Código de estado HTTP
    private String message; // Mensaje de error principal
    private Map<String, String> errorMessages; // Detalle de errores específicos

    // Constructor privado para errores generales
    private ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Constructor privado para errores de validación
    private ErrorResponse(Map<String, String> errorMessages) {
        this.code = 400; // Código HTTP por defecto para errores de validación
        this.message = "Bad Request";
        this.errorMessages = errorMessages;
    }

    // Método estático para crear una instancia de error general
    public static ErrorResponse generalError(int code, String message) {
        return new ErrorResponse(code, message);
    }

    // Método estático para crear una instancia de error de validación
    public static ErrorResponse validationError(Map<String, String> errors) {
        return new ErrorResponse(errors);
    }
}
