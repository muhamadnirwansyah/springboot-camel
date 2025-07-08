package com.app.service_backend.dto.response;


import com.app.service_backend.dto.request.SessionUserInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProvinceResponse extends SessionUserInfoRequest {

    private String id;
    private Double latitude;
    private Double longitude;
    private String name;
    private String altName;

}
