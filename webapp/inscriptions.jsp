<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inscriptions - ${tournoi.nom}</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
                        url('https://images.pexels.com/photos/9072386/pexels-photo-9072386.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2') no-repeat center center fixed;
            background-size: cover;
            color: #fff;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .card, .table-container {
            background: rgba(255, 255, 255, 0.4);
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
            .card, .table-container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container mt-4">

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <!-- Titre -->
    <div class="text-center mb-4">
        <h2><i class="bi bi-calendar-check-fill"></i> Inscriptions pour ${tournoi.nom}</h2>
    </div>

    <!-- Liste des inscriptions -->
    <div class="table-container">
        <h4><i class="bi bi-list-ul"></i> Liste des Inscriptions</h4>
        <table class="table table-striped table-bordered bg-white rounded">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Date</th>
                    <th>Joueur</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${inscriptions}" var="i">
                    <tr>
                        <td>${i.id}</td>
                        <td>${i.dateInscription}</td>
                        <td>${i.joueur.pseudo}</td>
                        <td>
                            <c:if test="${sessionScope.role eq 'ADMIN'}">
                                <a href="deleteInscription?id=${i.id}&tournoiId=${tournoi.id}" class="btn btn-danger btn-sm"
                                   onclick="return confirm('Supprimer cette inscription?')">
                                    <i class="bi bi-trash3-fill"></i> Supprimer
                                </a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Détails d'inscription -->
    <c:if test="${not empty selectedInscription}">
        <div class="card">
            <h4 class="text-center mb-3"><i class="bi bi-info-circle-fill"></i> Détails de l'Inscription</h4>
            <p><strong>ID :</strong> ${selectedInscription.id}</p>
            <p><strong>Joueur :</strong> ${selectedInscription.joueur.pseudo}</p>
            <p><strong>Tournoi :</strong> ${selectedInscription.tournoi.nom}</p>
            <p><strong>Date :</strong> ${selectedInscription.dateInscription}</p>
            <p><strong>Statut :</strong> ${selectedInscription.status}</p>
        </div>
    </c:if>

    <!-- Liste des joueurs inscrits -->
    <c:if test="${not empty joueursInscrits}">
        <div class="table-container">
            <h4><i class="bi bi-people-fill"></i> Joueurs Inscrits</h4>
            <table class="table table-striped table-bordered bg-white rounded">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Pseudo</th>
                        <th>Plateforme</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${joueursInscrits}" var="joueur">
                        <tr>
                            <td>${joueur.id}</td>
                            <td>${joueur.pseudo}</td>
                            <td>${joueur.plateforme}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

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
