package com.app.service_backend.dto.response;

import com.app.service_backend.dto.request.SessionUserInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageableRequest extends SessionUserInfoRequest {

    private int page = 0;
    private int size = 10;
    private String sort = "id";
    private String order = "desc";
    private String startDate = "";
    private String endDate = "";
    private String textSearch = "";

}
