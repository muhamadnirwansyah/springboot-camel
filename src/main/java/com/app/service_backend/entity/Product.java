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
@Table(name = "tbl_product_saving")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "start_active_date", nullable = false)
    private LocalDateTime startActiveDate;
    @Column(name = "end_active_date", nullable = false)
    private LocalDateTime endActiveDate;
    @Column(name = "deleted", nullable = false)
    private Integer deleted;
}
