package com.app.service_backend.dto.response;

import com.app.service_backend.dto.request.SessionUserInfoRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse extends SessionUserInfoRequest {

    private Integer status;
    private String messageSuccess;
    private List<String> messageErrors;
    private Object data;

    public static ApiResponse isOk(Object data, String message, String userInfo, String token){
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .messageSuccess(message)
                .data(data)
                .userInfo(userInfo)
                .token(token)
                .build();
    }

    public static ApiResponse isFailed(Integer status, List<String> messageErrors, String userInfo, String token){
        return ApiResponse.builder()
                .status(status)
                .messageSuccess(null)
                .messageErrors(messageErrors)
                .userInfo(userInfo)
                .token(token)
                .build();
    }
}
