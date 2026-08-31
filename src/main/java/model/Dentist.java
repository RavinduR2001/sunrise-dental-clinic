package com.sunrisedental.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dentist {
    private int dentistId;
    private String name;
    private String specialization;
    private String licenseNumber;
    private String phone;
    private String email;
    private boolean active;
}