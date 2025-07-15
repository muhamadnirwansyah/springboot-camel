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
@Table(name = "tbl_log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "action", nullable = false)
    private String action;
    @Column(name = "log_id", nullable = false)
    private Long logId;
    @Column(name = "module", nullable = false)
    private String module;
    @Column(name = "log_time", nullable = false)
    private LocalDateTime logTime;
}
