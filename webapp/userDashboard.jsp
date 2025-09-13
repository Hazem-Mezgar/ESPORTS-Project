<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de bord Joueur</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
                        url('https://images.pexels.com/photos/7849510/pexels-photo-7849510.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2') no-repeat center center fixed;
            background-size: cover;
            color: #fff;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .table-container, .card {
            background: rgba(0, 0, 0, 0.4);
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
            padding: 30px;
            margin-top: 40px;
        }

        .alert {
            border-radius: 10px;
            font-weight: bold;
            text-align: center;
        }

        .table th, .table td {
            color: #000;
        }

        .btn {
            border-radius: 20px;
            font-weight: bold;
            padding: 6px 15px;
        }

        @media (max-width: 768px) {
            .table-container, .card {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container mt-4">

    <!-- Messages -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <!-- Tournois disponibles -->
    <div class="table-container">
        <h3><i class="bi bi-controller"></i> Tournois disponibles</h3>
        <table class="table table-striped table-bordered bg-white rounded">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Jeu</th>
                    <th>Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${tournois}" var="tournoi">
                    <tr>
                        <td>${tournoi.nom}</td>
                        <td>${tournoi.jeu}</td>
                        <td>${tournoi.date}</td>
                        <td>
                            <a href="addInscription?tournoiId=${tournoi.id}" class="btn btn-primary btn-sm">
                                <i class="bi bi-pencil-square"></i> S'inscrire
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Mes inscriptions -->
    <div class="table-container">
        <h3 class="mt-5"><i class="bi bi-card-checklist"></i> Mes inscriptions</h3>
        <table class="table table-striped table-bordered bg-white rounded">
            <thead>
                <tr>
                    <th>Tournoi</th>
                    <th>Date demande</th>
                    <th>Statut</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${mesInscriptions}" var="inscription">
                    <tr>
                        <td>${inscription.tournoi.nom}</td>
                        <td>${inscription.dateInscription}</td>
                        <td>
                            <span class="badge 
                                ${inscription.status == 'VALIDEE' ? 'bg-success' : 
                                  (inscription.status == 'REFUSEE' ? 'bg-danger' : 'bg-warning')}">
                                ${inscription.status}
                            </span>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Bouton retour -->
    <div class="text-center mt-4">
        <a href="accueil" class="btn btn-secondary">
            <i class="bi bi-arrow-left-circle"></i> Retour
        </a>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
