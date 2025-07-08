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
public class ConsumerVillageDTO {

    private String id;
    @JsonProperty("district_id")
    private String districtId;
    private String name;
    private String latitude;
    private String longitude;
}
