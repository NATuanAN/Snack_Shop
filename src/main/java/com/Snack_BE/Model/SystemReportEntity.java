package com.Snack_BE.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "systemreport")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SystemReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportid")
    private Long reportId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;

    @ManyToOne
    @JoinColumn(name = "reportedbyuserid", nullable = false)
    private UserEntity reportedBy;
}
