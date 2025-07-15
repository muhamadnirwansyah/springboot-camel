package com.app.service_backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationMessageException extends RuntimeException{

    private List<String> messages;
    private Integer status;

    public ValidationMessageException(List<String> messages, Integer status){
        super(String.join("\n", messages));
        this.status = status;
        this.messages = messages;
    }
}
