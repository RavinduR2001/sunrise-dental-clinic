package com.sunrisedental.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Treatment {
    private int treatmentId;
    private String treatmentName;
    private String description;
    private String category;
    private int durationMinutes;
    private BigDecimal cost;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}