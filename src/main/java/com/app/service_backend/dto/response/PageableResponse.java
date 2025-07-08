package com.app.service_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageableResponse {
    private Page page;
}
