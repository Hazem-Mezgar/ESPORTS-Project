<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${tournoi != null ? 'Modifier' : 'Ajouter'} Tournoi</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
                        url('https://images.pexels.com/photos/7862413/pexels-photo-7862413.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2') no-repeat center center fixed;
            background-size: cover;
            color: #fff;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .card {
            background: rgba(255, 255, 255, 0.4);
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
            padding: 30px;
            margin-top: 40px;
        }

        .form-label {
            font-weight: bold;
            color: #000;
        }

        .form-control, .form-select {
            border-radius: 10px;
            border: 1px solid #007bff;
        }

        .btn-primary {
            border-radius: 20px;
            font-weight: bold;
            padding: 10px 20px;
        }

        .alert-danger {
            border-radius: 10px;
        }

        @media (max-width: 768px) {
            .card {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <h3 class="text-center mb-4">
            <i class="bi ${tournoi != null ? 'bi-pencil' : 'bi-plus-circle'}"></i>
            ${tournoi != null ? 'Modifier' : 'Ajouter'} un Tournoi
        </h3>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger text-center">${errorMessage}</div>
        </c:if>

        <form method="post" action="tournoi">
            <input type="hidden" name="id" value="${tournoi.id}">

            <div class="mb-3">
                <label class="form-label">Nom</label>
                <input type="text" name="nom" value="${tournoi.nom}" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Jeu</label>
                <input type="text" name="jeu" value="${tournoi.jeu}" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Statut</label>
                <select name="status" class="form-select">
                    <option value="PLANIFIE" ${tournoi.status == 'PLANIFIE' ? 'selected' : ''}>Planifié</option>
                    <option value="EN_COURS" ${tournoi.status == 'EN_COURS' ? 'selected' : ''}>En cours</option>
                    <option value="TERMINE" ${tournoi.status == 'TERMINE' ? 'selected' : ''}>Terminé</option>
                </select>
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-primary">
                    <i class="bi ${tournoi != null ? 'bi-pencil-square' : 'bi-plus-lg'}"></i>
                    ${tournoi != null ? 'Modifier' : 'Ajouter'}
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Bootstrap JS (si nécessaire) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
