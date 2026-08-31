package com.sunrisedental.service;

import com.sunrisedental.dao.TreatmentDAO;
import com.sunrisedental.model.Treatment;
import java.math.BigDecimal;
import java.util.List;

public class TreatmentService {
    private TreatmentDAO treatmentDAO;

    public TreatmentService() {
        this.treatmentDAO = new TreatmentDAO();
    }

    // Get all treatments
    public List<Treatment> getAllTreatments() {
        return treatmentDAO.getAllTreatments();
    }

    // Get treatment by ID
    public Treatment getTreatmentById(int treatmentId) {
        return treatmentDAO.getTreatmentById(treatmentId);
    }

    // Get treatments by category
    public List<Treatment> getTreatmentsByCategory(String category) {
        return treatmentDAO.getTreatmentsByCategory(category);
    }

    // Search treatments by name
    public List<Treatment> searchTreatments(String keyword) {
        return treatmentDAO.searchTreatments(keyword);
    }

    // Get treatments within price range
    public List<Treatment> getTreatmentsByPriceRange(BigDecimal min, BigDecimal max) {
        return treatmentDAO.getTreatmentsByPriceRange(min, max);
    }

    // Create new treatment
    public boolean createTreatment(Treatment treatment) {
        // Validate treatment data
        if (treatment.getTreatmentName() == null || treatment.getTreatmentName().isEmpty()) {
            throw new IllegalArgumentException("Treatment name is required");
        }
        if (treatment.getCost() == null || treatment.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cost must be greater than 0");
        }
        return treatmentDAO.createTreatment(treatment);
    }

    // Update treatment
    public boolean updateTreatment(Treatment treatment) {
        if (treatment.getTreatmentId() <= 0) {
            throw new IllegalArgumentException("Invalid treatment ID");
        }
        return treatmentDAO.updateTreatment(treatment);
    }

    // Delete treatment (soft delete)
    public boolean deleteTreatment(int treatmentId) {
        if (treatmentId <= 0) {
            throw new IllegalArgumentException("Invalid treatment ID");
        }
        return treatmentDAO.deleteTreatment(treatmentId);
    }

    // Get treatment count
    public int getTreatmentCount() {
        return treatmentDAO.getAllTreatments().size();
    }
}