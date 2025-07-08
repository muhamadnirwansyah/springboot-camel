package com.app.service_backend.controller;

import com.app.service_backend.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex){
        log.error(ex.getMessage());
        List<String> messageErrors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(messageError -> messageErrors.add(messageError.getDefaultMessage()));
        return ResponseEntity.status(ex.getStatusCode().value())
                .body(ApiResponse.isFailed(ex.getStatusCode().value(), messageErrors, null, null));
    }
}
