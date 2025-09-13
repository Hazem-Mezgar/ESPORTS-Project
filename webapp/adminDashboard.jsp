<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Tableau de bord Admin</title>
    <!-- Bootstrap CSS et icônes -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), 
                        url('https://images.unsplash.com/photo-1633545505446-586bf83717f0?q=80&w=2072&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D') no-repeat center center fixed;
            background-size: cover;
            color: #fff;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .card {
            background: rgba(255, 255, 255, 0.45);
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
            padding: 20px;
            margin-top: 20px;
        }

        .table {
            color: #333;
        }

        .table th {
            background-color: #007bff;
            color: white;
        }

        .table tbody tr:hover {
            background-color: #f1f1f1;
        }

        .btn-sm {
            margin-right: 5px;
            transition: transform 0.2s;
        }

        .btn-sm:hover {
            transform: translateY(-2px);
        }

        .btn-primary {
            border-radius: 20px;
            padding: 10px 20px;
            font-weight: bold;
        }

        @media (max-width: 768px) {
            .card {
                padding: 15px;
            }
            .btn-sm {
                margin-bottom: 5px;
            }
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <div class="card">
        <h2 class="mb-4"><i class="bi bi-person-check"></i> Demandes d'inscription en attente</h2>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Joueur</th>
                    <th>Tournoi</th>
                    <th>Date demande</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${inscriptions}" var="inscription">
                    <tr>
                        <td>${inscription.joueur.pseudo}</td>
                        <td>${inscription.tournoi.nom}</td>
                        <td>${inscription.dateInscription}</td>
                        <td>
                            <a href="validerInscription?id=${inscription.id}" class="btn btn-success btn-sm">
                                <i class="bi bi-check-circle"></i> Valider
                            </a>
                            <a href="refuserInscription?id=${inscription.id}" class="btn btn-danger btn-sm">
                                <i class="bi bi-x-circle"></i> Refuser
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="card mt-4">
        <h2 class="mb-4"><i class="bi bi-controller"></i> Tournois</h2>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Jeu</th>
                    <th>Statut</th>
                    <th>Joueurs Validés</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${tournois}" var="tournoi">
                    <tr>
                        <td>${tournoi.nom}</td>
                        <td>${tournoi.jeu}</td>
                        <td>${tournoi.status}</td>
                        <td>${joueursValidesParTournoi[tournoi.id]}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <a href="tournoi" class="btn btn-primary mt-3">
            <i class="bi bi-plus-circle"></i> Créer un nouveau tournoi
        </a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
