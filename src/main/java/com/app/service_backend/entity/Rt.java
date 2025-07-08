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
@Table(name = "tbl_rt") //rukun tetangga
public class Rt {

    @Id
    private String id;
    @Column(name = "number",nullable = false)
    private String number;
    @Column(name = "rw_id",nullable = false)
    private Long rwId;
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
    @Column(name = "updated_time", updatable = true)
    private LocalDateTime updatedTime;
}
