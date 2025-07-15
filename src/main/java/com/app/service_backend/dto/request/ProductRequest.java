package com.app.service_backend.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private String description;
    private String activeDate;
    private String expireDate;

    public List<String> doValidate(){
        List<String> errors = new ArrayList<>();
        if (Objects.isNull(name) || name.trim().isBlank()){
            errors.add("Name must not be empty or blank !");
        }
        if (Objects.isNull(description) || description.trim().isEmpty()){
            errors.add("Description must not be empty or blank !");
        }
        if (!name.matches("^[A-Za-z\\s]+$")){
            errors.add("Name must only contain letters and space !");
        }
        if (Objects.isNull(activeDate) || activeDate.trim().isEmpty()){
            errors.add("Active date cannot be empty !");
        }
        if (Objects.isNull(expireDate) || expireDate.trim().isEmpty()){
            errors.add("Expire date or end active date cannot be empty !");
        }
        return errors;
    }
}
