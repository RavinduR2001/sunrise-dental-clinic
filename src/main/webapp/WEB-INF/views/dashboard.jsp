<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Sunrise Dental</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: #f4f4f4;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .header h1 {
            margin: 0;
        }
        .user-info {
            display: flex;
            align-items: center;
            gap: 20px;
        }
        .user-info .logout-btn {
            background: rgba(255,255,255,0.2);
            color: white;
            padding: 8px 16px;
            border: 1px solid white;
            border-radius: 5px;
            text-decoration: none;
        }
        .user-info .logout-btn:hover {
            background: rgba(255,255,255,0.3);
        }
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        .welcome {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }
        .dashboard-card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        .dashboard-card .number {
            font-size: 36px;
            font-weight: bold;
            color: #667eea;
        }
        .dashboard-card .label {
            color: #666;
            margin-top: 10px;
        }
        .dashboard-card .icon {
            font-size: 40px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<%
    // Check if user is logged in
    com.sunrisedental.model.User user = (com.sunrisedental.model.User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<div class="header">
    <h1>🦷 Sunrise Dental</h1>
    <div class="user-info">
        <span>Welcome, <%= user.getFullName() %> (<%= user.getRole() %>)</span>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
    </div>
</div>

<div class="container">
    <div class="welcome">
        <h2>Welcome to Sunrise Dental Clinic!</h2>
        <p>Today is <%= new java.util.Date() %></p>
    </div>

    <div class="dashboard-grid">
        <div class="dashboard-card">
            <div class="icon">👤</div>
            <div class="number">${userCount}</div>
            <div class="label">Total Users</div>
        </div>
        <div class="dashboard-card">
            <div class="icon">📅</div>
            <div class="number">${appointmentCount}</div>
            <div class="label">Today's Appointments</div>
        </div>
        <div class="dashboard-card">
            <div class="icon">👨‍⚕️</div>
            <div class="number">${dentistCount}</div>
            <div class="label">Dentists</div>
        </div>
        <div class="dashboard-card">
            <div class="icon">📋</div>
            <div class="number">${treatmentCount}</div>
            <div class="label">Treatments</div>
        </div>
    </div>
</div>
</body>
</html>