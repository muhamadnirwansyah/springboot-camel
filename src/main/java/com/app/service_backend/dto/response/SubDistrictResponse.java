package com.app.service_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubDistrictResponse {

    private String id;
    private String cityId;
    private String name;
    private String altName;
    private Double longitude;
    private Double latitude;

}
