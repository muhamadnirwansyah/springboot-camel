package com.app.service_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProvinceRequest extends SessionUserInfoRequest{

    @NotBlank(message = "{province.id.notblank}")
    private String id;
    @NotNull(message = "{province.latitude.notnull}")
    private Double latitude;
    @NotNull(message = "{provice.longitude.notnull}")
    private Double longitude;
    @NotBlank(message = "{province.name.notblank}")
    private String name;
    @NotBlank(message = "{province.altname.notblank}")
    private String altName;

}
