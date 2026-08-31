package com.sunrisedental.service;

import com.sunrisedental.dao.AppointmentDAO;
import com.sunrisedental.models.Appointment;
import java.util.List;

public class AppointmentService {
    private AppointmentDAO appointmentDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
    }

    public List<Appointment> getAllAppointments() {
        // This would need a new method in AppointmentDAO
        // For now, just return empty list
        return List.of();
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentDAO.getAppointmentsByPatient(patientId);
    }

    public List<Appointment> getAppointmentsByDentist(int dentistId) {
        return appointmentDAO.getAppointmentsByDentist(dentistId);
    }

    public List<Appointment> getTodayAppointments(int dentistId) {
        return appointmentDAO.getTodayAppointments(dentistId);
    }

    public int createAppointment(Appointment appointment) {
        return appointmentDAO.createAppointment(appointment);
    }

    public boolean updateAppointmentStatus(int appointmentId, String status) {
        return appointmentDAO.updateAppointmentStatus(appointmentId, status);
    }

    public boolean cancelAppointment(int appointmentId) {
        return appointmentDAO.cancelAppointment(appointmentId);
    }
}