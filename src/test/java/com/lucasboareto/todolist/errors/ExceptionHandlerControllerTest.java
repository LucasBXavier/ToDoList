package com.lucasboareto.todolist.errors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerControllerTest {

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;

    @Test
    void handleHttpMessageNotReadableReturnsBadRequestWithSpecificErrorMessage() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON format");

        ResponseEntity<String> response = exceptionHandlerController.handleHttpMessageNotReadable(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao processar a requisição: Invalid JSON format", response.getBody());
    }

    @Test
    void handleHttpMessageNotReadableHandlesNullCauseGracefully() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON format", (HttpInputMessage) null);

        ResponseEntity<String> response = exceptionHandlerController.handleHttpMessageNotReadable(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao processar a requisição: Invalid JSON format", response.getBody());
    }
}