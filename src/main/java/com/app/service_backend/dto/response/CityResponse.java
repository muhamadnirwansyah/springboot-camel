package com.app.service_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityResponse {

    private String id;
    private String provinceId;
    private String name;
    private String altName;
    private Double latitude;
    private Double longitude;

}
