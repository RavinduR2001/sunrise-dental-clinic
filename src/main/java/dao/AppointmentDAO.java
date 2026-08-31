package com.sunrisedental.dao;

import com.sunrisedental.models.Appointment;
import com.sunrisedental.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC, appointment_time DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Get appointments by patient ID
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_date DESC";

        try (Connection conn = DatabaseConnection.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Get appointments by dentist ID
    public List<Appointment> getAppointmentsByDentist(int dentistId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE dentist_id = ? ORDER BY appointment_date, appointment_time";

        try (Connection conn = DatabaseConnection.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dentistId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Get today's appointments for a dentist
    public List<Appointment> getTodayAppointments(int dentistId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE dentist_id = ? " +
                "AND appointment_date = CAST(GETDATE() AS DATE) " +
                "ORDER BY appointment_time";

        try (Connection conn = DatabaseConnection.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dentistId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Create new appointment
    public int createAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, dentist_id, treatment_id, " +
                "appointment_date, appointment_time, duration_minutes, notes, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDentistId());

            if (appointment.getTreatmentId() != null) {
                stmt.setInt(3, appointment.getTreatmentId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setDate(4, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(5, Time.valueOf(appointment.getAppointmentTime()));
            stmt.setInt(6, appointment.getDurationMinutes());
            stmt.setString(7, appointment.getNotes());
            stmt.setInt(8, appointment.getCreatedBy());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Update appointment status
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ?, updated_at = GETDATE() " +
                "WHERE appointment_id = ?";

        try (Connection conn = DatabaseConnection.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cancel appointment
    public boolean cancelAppointment(int appointmentId) {
        return updateAppointmentStatus(appointmentId, "Cancelled");
    }

    // Helper method to map ResultSet to Appointment object
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDentistId(rs.getInt("dentist_id"));

        int treatmentId = rs.getInt("treatment_id");
        if (!rs.wasNull()) {
            appointment.setTreatmentId(treatmentId);
        }

        appointment.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        appointment.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
        appointment.setDurationMinutes(rs.getInt("duration_minutes"));
        appointment.setStatus(rs.getString("status"));
        appointment.setNotes(rs.getString("notes"));
        appointment.setCreatedBy(rs.getInt("created_by"));
        appointment.setCreatedAt(rs.getTimestamp("created_at"));
        appointment.setUpdatedAt(rs.getTimestamp("updated_at"));
        return appointment;
    }
}