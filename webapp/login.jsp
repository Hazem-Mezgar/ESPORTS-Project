<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion</title>
    <!-- Import Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS for the login page -->
    <style>
        body {
            background: url('https://images.pexels.com/photos/13341787/pexels-photo-13341787.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2') no-repeat center center fixed;
            background-size: cover;
            color: #fff;
            font-family: 'Arial', sans-serif;
        }
        
        .login-container {
            background: rgba(0, 0, 0, 0.6);
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
            max-width: 400px;
            margin: 100px auto;
        }
        
        .card-header {
            font-size: 1.8em;
            font-weight: bold;
            text-align: center;
            color: #00000;
        }

        .form-label {
            font-weight: bold;
        }
        
        .form-control {
            border-radius: 10px;
            margin-bottom: 20px;
            padding: 10px;
        }
        
        .btn-primary {
            background-color: #4CAF50;
            color: white;
            border-radius: 20px;
            padding: 12px 20px;
            width: 100%;
            font-size: 1.1em;
        }

        .btn-primary:hover {
            background-color: #45a049;
        }

        .alert-danger {
            color: #721c24;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .text-center {
            margin-top: 20px;
        }
    </style>
</head>
<body>

<div class="login-container">
    <div class="card">
        <div class="card-header">
            Connexion
        </div>
        <div class="card-body">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
            
            <form method="post" action="login">
                <div class="mb-3">
                    <label for="username" class="form-label">Nom d'utilisateur</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Mot de passe</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary">Se connecter</button>
            </form>
        </div>
    </div>

    <div class="text-center">
        <a href="#" class="text-white">Mot de passe oublié ?</a>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
