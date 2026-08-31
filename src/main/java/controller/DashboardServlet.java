package com.sunrisedental.controller;

import com.sunrisedental.models.User;
import com.sunrisedental.service.UserService;
import com.sunrisedental.service.AppointmentService;
import com.sunrisedental.dao.TreatmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private UserService userService;
    private AppointmentService appointmentService;
    private TreatmentDAO treatmentDAO;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        appointmentService = new AppointmentService();
        treatmentDAO = new TreatmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Get counts for dashboard
        List<User> users = userService.getAllUsers();
        int userCount = users.size();
        int treatmentCount = treatmentDAO.getAllTreatments().size();
        int appointmentCount = appointmentService.getAppointmentsByPatient(1).size(); // Placeholder

        request.setAttribute("userCount", userCount);
        request.setAttribute("treatmentCount", treatmentCount);
        request.setAttribute("appointmentCount", appointmentCount);
        request.setAttribute("dentistCount", 3); // Placeholder

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}

