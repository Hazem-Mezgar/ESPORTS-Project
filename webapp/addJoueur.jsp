<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="navbar.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Gestion des Joueurs</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
                        url('https://images.pexels.com/photos/9072219/pexels-photo-9072219.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2') no-repeat center center fixed;
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

        .form-label, .table th {
            font-weight: bold;
            color: #000;
        }

        .form-control, .form-select {
            border-radius: 10px;
            border: 1px solid #007bff;
        }

        .btn-primary, .btn-info, .btn-danger {
            border-radius: 20px;
            font-weight: bold;
            padding: 6px 15px;
        }

        .alert-danger, .alert-success {
            border-radius: 10px;
        }

        table td {
            color: #000;
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

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger text-center">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success text-center">${successMessage}</div>
    </c:if>

    <!-- Formulaire d'ajout -->
    <div class="card">
        <h3 class="text-center mb-4">
            <i class="bi bi-person-plus-fill"></i> Ajouter un Joueur
        </h3>
        <form method="post" action="addJoueur">
            <div class="mb-3">
                <label class="form-label">Pseudo</label>
                <input type="text" name="pseudo" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Plateforme</label>
                <select name="plateforme" class="form-select" required>
                    <option value="PC">PC</option>
                    <option value="PS5">PlayStation 5</option>
                    <option value="Xbox">Xbox Series X</option>
                </select>
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-plus-circle"></i> Ajouter
                </button>
            </div>
        </form>
    </div>

    <!-- Détails d'un joueur -->
    <c:if test="${not empty joueur}">
        <div class="card mt-4">
            <h4 class="text-center mb-3">
                <i class="bi bi-info-circle"></i> Détails du Joueur
            </h4>
            <p><strong>ID :</strong> ${joueur.id}</p>
            <p><strong>Pseudo :</strong> ${joueur.pseudo}</p>
            <p><strong>Plateforme :</strong> ${joueur.plateforme}</p>
        </div>
    </c:if>

    <!-- Liste des joueurs -->
    <div class="card mt-4">
        <h4 class="text-center mb-3">
            <i class="bi bi-people-fill"></i> Liste des Joueurs
        </h4>
        <form method="get" action="searchJoueur" class="search-form">
            <div class="input-group">
                <input type="text" name="pseudo" class="form-control" placeholder="Rechercher par pseudo..." 
                       value="${searchPseudo != null ? searchPseudo : ''}">
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-search"></i> Rechercher
                </button>
            </div>
        </form>
        <table class="table table-striped table-bordered bg-white rounded">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Pseudo</th>
                    <th>Plateforme</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${joueurs}" var="j">
                    <tr>
                        <td>${j.id}</td>
                        <td>${j.pseudo}</td>
                        <td>${j.plateforme}</td>
                        <td>
                            
                            <a href="deleteJoueur?id=${j.id}" class="btn btn-danger btn-sm"
                               onclick="return confirm('Supprimer ce joueur?')">
                                <i class="bi bi-trash3-fill"></i> Supprimer
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
