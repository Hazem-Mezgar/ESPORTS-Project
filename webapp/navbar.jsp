<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/accueil">Gestion eSport</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" 
                data-bs-target="#navbarNav" aria-controls="navbarNav" 
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <c:if test="${not empty sessionScope.username}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/accueil">Accueil</a>
                    </li>
                    <c:choose>
                        <c:when test="${sessionScope.role eq 'ADMIN'}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin">Tableau de bord</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/tournoi">Nouveau Tournoi</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/addJoueur">Gestion Joueurs</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/userDashboard">Mon Espace</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </ul>
            <c:if test="${not empty sessionScope.username}">
                <span class="navbar-text me-3">
                    Connecté en tant que ${sessionScope.username} (${sessionScope.role})
                </span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light">Déconnexion</a>
            </c:if>
        </div>
    </div>
</nav>