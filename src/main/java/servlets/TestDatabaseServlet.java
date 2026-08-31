package com.sunrisedental.servlets;

import com.sunrisedental.util.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/test-db")
public class TestDatabaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Database Test</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 20px; }");
        out.println(".success { color: green; font-weight: bold; }");
        out.println(".error { color: red; font-weight: bold; }");
        out.println("table { border-collapse: collapse; width: 80%; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h1>🔌 Database Connection Test</h1>");

        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                out.println("<p class='success'>✅ Connected to database successfully!</p>");
                out.println("<p><strong>Database:</strong> " + conn.getCatalog() + "</p>");
                out.println("<p><strong>Server:</strong> " + conn.getMetaData().getDatabaseProductName() + "</p>");
                out.println("<p><strong>Version:</strong> " + conn.getMetaData().getDatabaseProductVersion() + "</p>");

                // Test query - get users
                out.println("<h2>Users Table Test</h2>");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT user_id, username, first_name, last_name, role FROM users WHERE is_active = 1")) {

                    if (rs.next()) {
                        out.println("<table>");
                        out.println("<tr><th>ID</th><th>Username</th><th>First Name</th><th>Last Name</th><th>Role</th></tr>");

                        // Reset cursor and loop
                        rs.beforeFirst();
                        while (rs.next()) {
                            out.println("<tr>");
                            out.println("<td>" + rs.getInt("user_id") + "</td>");
                            out.println("<td>" + rs.getString("username") + "</td>");
                            out.println("<td>" + rs.getString("first_name") + "</td>");
                            out.println("<td>" + rs.getString("last_name") + "</td>");
                            out.println("<td>" + rs.getString("role") + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    } else {
                        out.println("<p>No users found in database.</p>");
                    }
                }

                // Test query - get treatments
                out.println("<h2>Treatments Table Test</h2>");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT treatment_id, treatment_name, category, cost FROM treatments WHERE is_active = 1")) {

                    if (rs.next()) {
                        out.println("<table>");
                        out.println("<tr><th>ID</th><th>Treatment Name</th><th>Category</th><th>Cost</th></tr>");

                        rs.beforeFirst();
                        while (rs.next()) {
                            out.println("<tr>");
                            out.println("<td>" + rs.getInt("treatment_id") + "</td>");
                            out.println("<td>" + rs.getString("treatment_name") + "</td>");
                            out.println("<td>" + rs.getString("category") + "</td>");
                            out.println("<td>$" + rs.getBigDecimal("cost") + "</td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    } else {
                        out.println("<p>No treatments found in database.</p>");
                    }
                }

            } else {
                out.println("<p class='error'>❌ Database connection failed - connection is null or closed!</p>");
            }

        } catch (SQLException e) {
            out.println("<p class='error'>❌ Database connection failed!</p>");
            out.println("<p><strong>Error:</strong> " + e.getMessage() + "</p>");
            out.println("<p><strong>SQL State:</strong> " + e.getSQLState() + "</p>");
            out.println("<p><strong>Error Code:</strong> " + e.getErrorCode() + "</p>");
            e.printStackTrace();
        }

        out.println("<br><hr>");
        out.println("<h3>Connection Details Used:</h3>");
        out.println("<pre>");
        out.println("Driver: com.microsoft.sqlserver.jdbc.SQLServerDriver");
        out.println("URL: Check your db.properties file");
        out.println("</pre>");

        out.println("</body></html>");
    }
}