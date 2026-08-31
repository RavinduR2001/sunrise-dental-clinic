package com.sunrisedental.models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int dentistId;
    private Integer treatmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private int durationMinutes;
    private String status;
    private String notes;
    private int createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Appointment() {}

    // Constructor with required fields
    public Appointment(int patientId, int dentistId, LocalDate appointmentDate,
                       LocalTime appointmentTime, int createdBy) {
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.createdBy = createdBy;
        this.durationMinutes = 30;
        this.status = "Scheduled";
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDentistId() { return dentistId; }
    public void setDentistId(int dentistId) { this.dentistId = dentistId; }

    public Integer getTreatmentId() { return treatmentId; }
    public void setTreatmentId(Integer treatmentId) { this.treatmentId = treatmentId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}