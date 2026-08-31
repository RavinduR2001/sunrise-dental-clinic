package com.sunrisedental.dao;

import com.sunrisedental.model.Treatment;
import com.sunrisedental.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO {

    // Get all treatments
    public List<Treatment> getAllTreatments() {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM treatments WHERE is_active = 1 ORDER BY treatment_name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                treatments.add(mapResultSetToTreatment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treatments;
    }

    // Get treatment by ID
    public Treatment getTreatmentById(int treatmentId) {
        String sql = "SELECT * FROM treatments WHERE treatment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, treatmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTreatment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get treatments by category
    public List<Treatment> getTreatmentsByCategory(String category) {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM treatments WHERE category = ? AND is_active = 1 ORDER BY treatment_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    treatments.add(mapResultSetToTreatment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treatments;
    }

    // Create new treatment
    public boolean createTreatment(Treatment treatment) {
        String sql = "INSERT INTO treatments (treatment_name, description, category, duration_minutes, cost) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, treatment.getTreatmentName());
            stmt.setString(2, treatment.getDescription());
            stmt.setString(3, treatment.getCategory());
            stmt.setInt(4, treatment.getDurationMinutes());
            stmt.setBigDecimal(5, treatment.getCost());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        treatment.setTreatmentId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update treatment
    public boolean updateTreatment(Treatment treatment) {
        String sql = "UPDATE treatments SET treatment_name = ?, description = ?, category = ?, " +
                "duration_minutes = ?, cost = ?, is_active = ?, updated_at = GETDATE() " +
                "WHERE treatment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, treatment.getTreatmentName());
            stmt.setString(2, treatment.getDescription());
            stmt.setString(3, treatment.getCategory());
            stmt.setInt(4, treatment.getDurationMinutes());
            stmt.setBigDecimal(5, treatment.getCost());
            stmt.setBoolean(6, treatment.isActive());
            stmt.setInt(7, treatment.getTreatmentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete treatment (soft delete)
    public boolean deleteTreatment(int treatmentId) {
        String sql = "UPDATE treatments SET is_active = 0, updated_at = GETDATE() WHERE treatment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, treatmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get treatment cost
    public double getTreatmentCost(int treatmentId) {
        String sql = "SELECT cost FROM treatments WHERE treatment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, treatmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("cost");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Treatment mapResultSetToTreatment(ResultSet rs) throws SQLException {
        Treatment treatment = new Treatment();
        treatment.setTreatmentId(rs.getInt("treatment_id"));
        treatment.setTreatmentName(rs.getString("treatment_name"));
        treatment.setDescription(rs.getString("description"));
        treatment.setCategory(rs.getString("category"));
        treatment.setDurationMinutes(rs.getInt("duration_minutes"));
        treatment.setCost(rs.getBigDecimal("cost"));
        treatment.setActive(rs.getBoolean("is_active"));
        treatment.setCreatedAt(rs.getTimestamp("created_at"));
        treatment.setUpdatedAt(rs.getTimestamp("updated_at"));
        return treatment;
    }
}

// Add these methods to TreatmentDAO.java

// Search treatments by keyword
public List<Treatment> searchTreatments(String keyword) {
    List<Treatment> treatments = new ArrayList<>();
    String sql = "SELECT * FROM treatments WHERE treatment_name LIKE ? OR description LIKE ? AND is_active = 1";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        String searchPattern = "%" + keyword + "%";
        stmt.setString(1, searchPattern);
        stmt.setString(2, searchPattern);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                treatments.add(mapResultSetToTreatment(rs));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return treatments;
}

// Get treatments by price range
public List<Treatment> getTreatmentsByPriceRange(BigDecimal min, BigDecimal max) {
    List<Treatment> treatments = new ArrayList<>();
    String sql = "SELECT * FROM treatments WHERE cost BETWEEN ? AND ? AND is_active = 1 ORDER BY cost";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setBigDecimal(1, min);
        stmt.setBigDecimal(2, max);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                treatments.add(mapResultSetToTreatment(rs));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return treatments;
}