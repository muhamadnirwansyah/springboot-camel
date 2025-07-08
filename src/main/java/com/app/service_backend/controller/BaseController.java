package com.app.service_backend.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public class BaseController {

    public String checkAcceptLanguage(HttpServletRequest request){
        String isAcceptLanguage = request.getHeader("Accept-Language");
        if (Objects.isNull(isAcceptLanguage)){
            return "Success";
        }
        if (!isAcceptLanguage.equalsIgnoreCase("id")){
            return "Success";
        }
        return "Sukses";
    }
}
