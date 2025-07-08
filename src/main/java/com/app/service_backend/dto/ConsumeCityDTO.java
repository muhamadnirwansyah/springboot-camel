package com.app.service_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumeCityDTO {

    private String id;
    @JsonProperty("province_id")
    private String provinceId;
    private String name;
    @JsonProperty("alt_name")
    private String altName;
    private String latitude;
    private String longitude;

}
