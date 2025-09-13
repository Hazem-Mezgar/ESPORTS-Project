<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des Tournois</title>
    <!-- Inclut Bootstrap 5 CSS pour les styles modernes -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Inclut Bootstrap Icons pour ajouter des icônes aux boutons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        /* Définit un arrière-plan dégradé avec une image semi-transparente */
        body {
            background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), 
                        url('https://images.unsplash.com/photo-1633545505446-586bf83717f0?q=80&w=2072&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D') no-repeat center center fixed;
            background-size: cover;
            color: #fff; /* Texte blanc pour contraste sur l'arrière-plan sombre */
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        /* Style pour le conteneur principal (card) */
        .card {
            background: rgba(255, 255, 255, 0.45); /* Fond blanc semi-transparent */
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
            padding: 20px;
            margin-top: 20px;
        }
        /* Style pour la table */
        .table {
            color: #333; /* Texte sombre pour lisibilité */
        }
        .table th {
            background-color: #007bff; /* En-tête bleu Bootstrap */
            color: white;
        }
        .table tbody tr:hover {
            background-color: #f1f1f1; /* Survol des lignes */
        }
        /* Style pour le formulaire de recherche */
        .search-form .input-group {
            max-width: 500px;
            margin: 0 auto;
        }
        .search-form .form-control {
            border-radius: 20px 0 0 20px;
            border: 1px solid #007bff;
            transition: border-color 0.3s;
        }
        .search-form .form-control:focus {
            border-color: #0056b3;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
        }
        .search-form .btn-primary {
            border-radius: 0 20px 20px 0;
            transition: background-color 0.3s;
        }
        /* Style pour les boutons d'action */
        .btn-sm {
            margin-right: 5px;
            transition: transform 0.2s;
        }
        .btn-sm:hover {
            transform: translateY(-2px); /* Effet de soulèvement au survol */
        }
        /* Style pour le bouton "Ajouter Tournoi" */
        .btn-success {
            border-radius: 20px;
            padding: 10px 20px;
            font-weight: bold;
        }
        /* Media query pour responsive */
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
    <!-- Conteneur principal avec une card Bootstrap -->
    <div class="container mt-5">
        <div class="card">
            <!-- Formulaire de recherche avec style moderne -->
            <form method="get" action="search" class="search-form mb-4">
                <div class="input-group">
                    <input type="text" name="jeu" class="form-control" placeholder="Rechercher par jeu...">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-search"></i> Rechercher
                    </button>
                </div>
            </form>

            <!-- Table des tournois avec style Bootstrap amélioré -->
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Jeu</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${tournois}" var="t">
                        <tr>
                            <td>${t.id}</td>
                            <td>${t.nom}</td>
                            <td>${t.jeu}</td>
                            <td>${t.date}</td>
                            <td>
                                <c:if test="${sessionScope.role eq 'ADMIN'}">
                                    <!-- Boutons pour ADMIN avec icônes -->
                                    <a href="edit?id=${t.id}" class="btn btn-warning btn-sm">
                                        <i class="bi bi-pencil"></i> Modifier
                                    </a>
                                    <a href="delete?id=${t.id}" class="btn btn-danger btn-sm" 
                                       onclick="return confirm('Supprimer ce tournoi?')">
                                        <i class="bi bi-trash"></i> Supprimer
                                    </a>
                                    <a href="inscriptions?tournoiId=${t.id}" class="btn btn-info btn-sm">
                                        <i class="bi bi-list"></i> Inscriptions
                                    </a>
                                </c:if>
                                <c:if test="${sessionScope.role eq 'USER'}">
                                    <!-- Bouton pour USER avec icône -->
                                    <a href="addInscription?tournoiId=${t.id}" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> S'inscrire
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Bouton "Ajouter Tournoi" pour ADMIN -->
            <c:if test="${sessionScope.role eq 'ADMIN'}">
                <a href="tournoi" class="btn btn-success">
                    <i class="bi bi-plus-lg"></i> Ajouter Tournoi
                </a>
            </c:if>
        </div>
    </div>

    <!-- Inclut Bootstrap JS pour les interactions (optionnel, si nécessaire pour d'autres composants) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>