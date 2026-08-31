<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Treatments - Sunrise Dental</title>
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
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        .search-box {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .search-box input, .search-box select {
            padding: 10px;
            margin-right: 10px;
            border: 2px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
        }
        .search-box button {
            padding: 10px 20px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .treatment-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
        }
        .treatment-card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }
        .treatment-card:hover {
            transform: translateY(-5px);
        }
        .treatment-card h3 {
            margin: 0 0 10px 0;
            color: #333;
        }
        .treatment-card .category {
            display: inline-block;
            padding: 5px 10px;
            background: #667eea;
            color: white;
            border-radius: 20px;
            font-size: 12px;
        }
        .treatment-card .price {
            font-size: 24px;
            font-weight: bold;
            color: #667eea;
            margin: 10px 0;
        }
        .treatment-card .description {
            color: #666;
            font-size: 14px;
        }
        .treatment-card .duration {
            color: #888;
            font-size: 12px;
        }
        .btn-back {
            padding: 10px 20px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .btn-back:hover {
            background: #5a67d8;
        }
    </style>
</head>
<body>
<div class="header">
    <h1>🦷 Treatment Management</h1>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">← Back to Dashboard</a>
</div>

<div class="container">
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/treatments" method="get">
            <input type="text" name="keyword" placeholder="Search treatments..." value="${keyword}">

            <select name="category">
                <option value="">All Categories</option>
                <option value="General" ${category == 'General' ? 'selected' : ''}>General</option>
                <option value="Cosmetic" ${category == 'Cosmetic' ? 'selected' : ''}>Cosmetic</option>
                <option value="Orthodontic" ${category == 'Orthodontic' ? 'selected' : ''}>Orthodontic</option>
                <option value="Surgical" ${category == 'Surgical' ? 'selected' : ''}>Surgical</option>
                <option value="Pediatric" ${category == 'Pediatric' ? 'selected' : ''}>Pediatric</option>
            </select>

            <input type="number" name="minPrice" placeholder="Min Price" value="${minPrice}" style="width:100px;">
            <input type="number" name="maxPrice" placeholder="Max Price" value="${maxPrice}" style="width:100px;">

            <button type="submit">Search</button>
        </form>
    </div>

    <div class="treatment-grid">
        <c:forEach var="treatment" items="${treatments}">
            <div class="treatment-card">
                <h3>${treatment.treatmentName}</h3>
                <span class="category">${treatment.category}</span>
                <div class="price">$${treatment.cost}</div>
                <div class="duration">⏱ ${treatment.durationMinutes} minutes</div>
                <p class="description">${treatment.description}</p>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
