package com.app.service_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_postalcode")
public class PostalCode {

    @Id
    private String id;
    @Column(name = "code",nullable = false)
    private String code;
    @Column(name = "rt_id",nullable = false)
    private Long rtId;
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
    @Column(name = "updated_time", updatable = true)
    private LocalDateTime updatedTime;
}
