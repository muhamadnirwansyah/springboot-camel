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
@Table(name = "tbl_subdistrict") //kecamatan
public class SubDistrict {

    @Id
    private String id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "alt_name")
    private String altName;
    @Column(name = "city_id", nullable = false)
    private String cityId;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
    @Column(name = "updated_time", updatable = true)
    private LocalDateTime updatedTime;

}
