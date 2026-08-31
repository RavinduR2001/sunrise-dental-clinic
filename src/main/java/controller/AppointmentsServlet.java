package com.sunrisedental.controller;

import com.sunrisedental.models.Appointment;
import com.sunrisedental.models.User;
import com.sunrisedental.service.AppointmentService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/appointments/*")
public class AppointmentsServlet extends HttpServlet {

    private AppointmentService appointmentService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        appointmentService = new AppointmentService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/sunrise-dental/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (pathInfo == null || "/".equals(pathInfo)) {
            // List appointments
            List<Appointment> appointments;
            if ("admin".equals(user.getRole()) || "receptionist".equals(user.getRole())) {
                appointments = appointmentService.getAllAppointments();
            } else if ("dentist".equals(user.getRole())) {
                // Get dentist ID from user (you'd need to link user to dentist)
                appointments = appointmentService.getAppointmentsByDentist(1); // Placeholder
            } else {
                appointments = appointmentService.getAppointmentsByPatient(user.getUserId());
            }

            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("/WEB-INF/views/appointments.jsp").forward(request, response);

        } else if (pathInfo.equals("/api")) {
            // API endpoint - return JSON
            List<Appointment> appointments = appointmentService.getAllAppointments();
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(appointments));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/sunrise-dental/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            // Get parameters from request
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            int dentistId = Integer.parseInt(request.getParameter("dentistId"));
            int treatmentId = request.getParameter("treatmentId") != null ?
                    Integer.parseInt(request.getParameter("treatmentId")) : 0;

            LocalDate appointmentDate = LocalDate.parse(request.getParameter("appointmentDate"));
            LocalTime appointmentTime = LocalTime.parse(request.getParameter("appointmentTime"));
            String notes = request.getParameter("notes");

            // Create appointment
            Appointment appointment = new Appointment(
                    patientId, dentistId, appointmentDate, appointmentTime, user.getUserId()
            );
            appointment.setTreatmentId(treatmentId > 0 ? treatmentId : null);
            appointment.setNotes(notes);

            int appointmentId = appointmentService.createAppointment(appointment);

            if (appointmentId > 0) {
                response.sendRedirect("/sunrise-dental/appointments?success=created");
            } else {
                response.sendRedirect("/sunrise-dental/appointments?error=creation_failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/sunrise-dental/appointments?error=invalid_data");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            String status = request.getParameter("status");

            boolean updated = appointmentService.updateAppointmentStatus(appointmentId, status);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": " + updated + "}");
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            boolean cancelled = appointmentService.cancelAppointment(appointmentId);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": " + cancelled + "}");
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}