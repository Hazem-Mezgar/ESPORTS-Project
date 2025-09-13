package web; // Déclare le package du servlet, le plaçant dans l'espace de noms 'web'.

// Importe les classes nécessaires pour l'accès aux données (DAO), les entités et les fonctionnalités du servlet.
import dao.GestionTournoiJPA2; 
import dao.IGestionTournoi; 
import entity.*; 
import javax.servlet.*; 
import javax.servlet.annotation.WebServlet; 
import javax.servlet.http.*; 
import java.io.IOException; 
import java.util.Date; 
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.stream.Collectors; 



// Configure le servlet avec des motifs d'URL pour router les requêtes HTTP.
@WebServlet(urlPatterns = {
    "/", "/accueil", "/search", "/delete", "/edit",
    "/tournoi", "/addJoueur", "/deleteJoueur", 
    "/inscriptions", "/addInscription", 
    "/login", "/logout", "/admin",
    "/validerInscription", "/refuserInscription", 
    "/userDashboard","/searchJoueur","/deleteInscription" 
})



public class Controlleur extends HttpServlet { 
    private static final long serialVersionUID = 1L; 
    private IGestionTournoi gestion; 

    
    @Override // Redéfinit la méthode init de HttpServlet pour initialiser le servlet.
    public void init(ServletConfig config) throws ServletException { 
        gestion = new GestionTournoiJPA2(); // Instancie l'implémentation du DAO pour les opérations sur la base de données.
    } 
    
    

    @Override // Redéfinit la méthode doGet pour traiter les requêtes HTTP GET.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException { 
        String path = request.getServletPath(); 

        try { 
            if (!path.equals("/login") && !isAuthenticated(request)) {
                System.out.println("Utilisateur non authentifié, redirection vers login"); // Affiche un message indiquant un accès non authentifié.
                response.sendRedirect("login");
                return; // Quitte la méthode pour arrêter le traitement.
            } 
        
            switch (path) { // Débute une instruction switch pour router les requêtes selon le chemin du servlet.
                case "/": // Correspond à l'URL racine.
                case "/accueil": 
                    handleAccueil(request, response); // Appelle la méthode pour gérer la page d'accueil.
                    break;
                    
                case "/search":
                    handleSearch(request, response); // Appelle la méthode pour gérer la recherche de tournois.
                    break; 
                case "/edit": 
                    handleEditForm(request, response); // Appelle la méthode pour afficher le formulaire d'édition de tournoi.
                    break; 
                case "/delete":
                    handleDeleteTournoi(request, response); // Appelle la méthode pour supprimer un tournoi.
                    break;
                case "/tournoi": 
                    if (isAdmin(request)) { // Vérifie si l'utilisateur est un administrateur.
                        handleTournoiForm(request, response); // Appelle la méthode pour afficher le formulaire de tournoi.
                    } else {
                        response.sendRedirect("accueil"); // Redirige vers la page d'accueil.
                    } 
                    break; 
                    
                    
                case "/addJoueur":
                    if (isAdmin(request)) { // Vérifie si l'utilisateur est un administrateur.
                        handleAddJoueurForm(request, response); // Appelle la méthode pour afficher le formulaire d'ajout de joueur.
                    } else { 
                        response.sendRedirect("accueil"); // Redirige vers la page d'accueil.
                    } 
                    break; 
                    
                case "/deleteJoueur":
                    if (isAdmin(request)) { // Vérifie si l'utilisateur est un administrateur.
                        handleDeleteJoueur(request, response); // Appelle la méthode pour supprimer un joueur.
                    } else { // Si l'utilisateur n'est pas administrateur.
                        response.sendRedirect("accueil"); // Redirige vers la page d'accueil.
                    } 
                    break; 
                    
                case "/searchJoueur": 
                    handleSearchJoueur(request, response);
                    break;
                    
                    
                    
                case "/login": 
                    request.getRequestDispatcher("/login.jsp").forward(request, response); // Transfère la requête à la page JSP de connexion.
                    break; 
                case "/logout": 
                    handleLogout(request, response); // Appelle la méthode pour gérer la déconnexion.
                    break;
                case "/admin":
                    if (isAdmin(request)) { // Vérifie si l'utilisateur est un administrateur.
                        handleAdminDashboard(request, response); // Appelle la méthode pour afficher le tableau de bord admin.
                    } else { 
                        response.sendRedirect("accueil"); 
                    } 
                    break;

                    
                case "/inscriptions": 
                    handleViewInscriptions(request, response); // Appelle la méthode pour voir les inscriptions d'un tournoi.
                    break; 
                case "/validerInscription": 
                    if (isAdmin(request)) { 
                        handleValiderInscription(request, response); // Appelle la méthode pour valider une inscription.
                    } else { 
                        response.sendRedirect("accueil"); 
                    } 
                    break; 
                case "/refuserInscription": 
                    if (isAdmin(request)) {
                        handleRefuserInscription(request, response); // Appelle la méthode pour refuser une inscription.
                    } else { 
                        response.sendRedirect("accueil"); // Redirige vers la page d'accueil.
                    } 
                    break; 
                case "/addInscription": 
                    handleAddInscription(request, response); // Appelle la méthode pour ajouter une inscription.
                    break;
                case "/deleteInscription": // Nouveau cas pour supprimer une inscription
                    if (isAdmin(request)) {
                        handleDeleteInscription(request, response);
                    } else {
                        response.sendRedirect("accueil");
                    }
                    break;
                    
                case "/userDashboard": 
                    handleUserDashboard(request, response); // Appelle la méthode pour afficher le tableau de bord utilisateur.
                    break; 
      
                
               
                default: 
                    request.getRequestDispatcher("/error.jsp").forward(request, response); // Transfère à la page JSP d'erreur.
            } 
            
        } catch (Exception e) { 
            handleError(request, response, e); 
        } 
    } // Ferme la méthode doGet.
    
    
    
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException { 
        String path = request.getServletPath(); 
        
        
      									// Débute un bloc try pour gérer les exceptions de manière élégante.
        
        try { 
            if (!path.equals("/login") && !isAuthenticated(request)) { // Vérifie si le chemin n'est pas "/login" et si l'utilisateur n'est pas authentifié.
                System.out.println("Utilisateur non authentifié, redirection vers login"); // Affiche un message indiquant un accès non authentifié.
                response.sendRedirect("login"); // Redirige l'utilisateur vers la page de connexion.
                return; 
            } 

            switch (path) { // Débute une instruction switch pour router les requêtes POST selon le chemin.
                case "/tournoi": 
                    if (isAdmin(request)) { // Vérifie si l'utilisateur est un administrateur.
                        handleTournoiSubmit(request, response); 
                    } else { 
                        response.sendRedirect("accueil"); // Redirige vers la page d'accueil.
                    } 
                    break;
                    
                case "/addJoueur": 
                    if (isAdmin(request)) { 
                        handleAddJoueurSubmit(request, response); 
                    } else { 
                        response.sendRedirect("accueil"); 
                    } 
                    break; 
                    
                case "/addInscription": 
                    handleAddInscription(request, response); 
                    break; 
                    
                case "/login": 
                    handleLogin(request, response); 
                    break; 
                    
                default: 
                    request.getRequestDispatcher("/error.jsp").forward(request, response); // Transfère à la page JSP d'erreur.
            } 
            
        } catch (Exception e) { 
            handleError(request, response, e); 
        } 
    } 

    					// ============ Méthodes liées à l'authentification ============
    
    
    private boolean isAuthenticated(HttpServletRequest request) { 
        HttpSession session = request.getSession(false); // Récupère la session actuelle, si elle existe, sans en créer une nouvelle.
        return session != null && session.getAttribute("username") != null; // Retourne vrai si la session existe et contient un nom d'utilisateur.
    } 

    private boolean isAdmin(HttpServletRequest request) { // Définit une méthode pour vérifier si un utilisateur est administrateur.
        HttpSession session = request.getSession(false); // Récupère la session actuelle, si elle existe.
        return session != null && "ADMIN".equals(session.getAttribute("role")); // Retourne vrai si la session existe et le rôle est "ADMIN".
    } 

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour gérer la connexion.
            throws ServletException, IOException { 
        String username = request.getParameter("username"); // Récupère le nom d'utilisateur depuis les paramètres de la requête.
        String password = request.getParameter("password"); // Récupère le mot de passe depuis les paramètres de la requête.

        System.out.println("Tentative de connexion pour: " + username); 
        Utilisateur utilisateur = gestion.getUtilisateurByUsername(username); // Récupère l'utilisateur depuis la base de données par nom d'utilisateur.
        if (utilisateur == null) { 
            System.out.println("Utilisateur non trouvé: " + username); // Affiche un message indiquant que l'utilisateur n'existe pas.
            request.setAttribute("errorMessage", "Utilisateur non trouvé"); // Définit un message d'erreur pour la page JSP.
            request.getRequestDispatcher("/login.jsp").forward(request, response); // Transfère à la page de connexion avec l'erreur.
            return;
        } 

        if (!utilisateur.getPassword().equals(password)) { // Vérifie si le mot de passe fourni correspond au mot de passe stocké.
            System.out.println("Mot de passe incorrect pour: " + username); // Affiche un message indiquant un mot de passe incorrect.
            request.setAttribute("errorMessage", "Mot de passe incorrect"); // Définit un message d'erreur pour la page JSP.
            request.getRequestDispatcher("/login.jsp").forward(request, response); // Transfère à la page de connexion avec l'erreur.
            return; 
        } 

        HttpSession session = request.getSession(); // Crée ou récupère la session de l'utilisateur.
        session.setAttribute("username", username); // Stocke le nom d'utilisateur dans la session.
        session.setAttribute("role", utilisateur.getRole()); // Stocke le rôle de l'utilisateur dans la session.

        if ("USER".equals(utilisateur.getRole())) { // Vérifie si l'utilisateur a le rôle "USER".
            Joueur joueur = utilisateur.getJoueur(); // Récupère l'entité joueur associée.
            if (joueur == null) { // Vérifie si aucun joueur n'est associé à l'utilisateur.
                System.out.println("Création d'un nouveau joueur pour: " + username); // Affiche un message pour la création d'un nouveau joueur.
                try { // Débute un bloc try pour la création du joueur.
                    joueur = new Joueur(username, "PC"); // Crée un nouveau joueur avec le nom d'utilisateur et la plateforme par défaut "PC".
                    gestion.addJoueur(joueur); // Persiste le nouveau joueur dans la base de données.
                    utilisateur.setJoueur(joueur); // Associe le joueur à l'utilisateur.
                    gestion.updateUtilisateur(utilisateur); // Met à jour l'utilisateur dans la base avec l'association joueur.
                    System.out.println("Joueur créé et associé: " + joueur.getPseudo() + ", ID: " + joueur.getId()); 
                } catch (Exception e) { 
                    System.out.println("Erreur lors de la création du joueur pour: " + username); 
                    e.printStackTrace(); 
                    request.setAttribute("errorMessage", "Erreur lors de la création du profil joueur"); 
                    request.getRequestDispatcher("/error.jsp").forward(request, response); 
                    return; 
                } 
            } else { // Si un joueur est déjà associé.
                System.out.println("Joueur existant trouvé pour: " + username + ", ID: " + joueur.getId()); 
            } 
            session.setAttribute("joueurId", joueur.getId()); 
        } 

        System.out.println("Connexion réussie pour: " + username + ", rôle: " + utilisateur.getRole()); 
        if ("ADMIN".equals(utilisateur.getRole())) { // Vérifie si l'utilisateur est administrateur.
            response.sendRedirect("admin"); // Redirige vers le tableau de bord admin.
        } else { // Si l'utilisateur n'est pas administrateur.
            response.sendRedirect("accueil"); 
        } 
    } 

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException { 
        HttpSession session = request.getSession(false); // Récupère la session actuelle, si elle existe.
        if (session != null) { // Vérifie si une session existe.
            System.out.println("Déconnexion de l'utilisateur: " + session.getAttribute("username")); // Affiche un message pour la déconnexion avec le nom d'utilisateur.
            session.invalidate(); // Invalide la session, supprimant tous les attributs.
        } 
        response.sendRedirect("login");
    } 

    
    
    
    								// ============ Méthodes du tableau de bord admin ============
    
    
    private void handleAdminDashboard(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour gérer le tableau de bord admin.
            throws ServletException, IOException { 
        List<Inscription> inscriptionsEnAttente = gestion.getInscriptionsByStatus("EN_ATTENTE"); // Récupère les inscriptions en attente depuis la base.
        List<Tournoi> tournois = gestion.getAllTournois(); // Récupère tous les tournois depuis la base.
        Map<Integer, Long> joueursValidesParTournoi = new HashMap<>(); // Crée une map pour stocker le nombre de joueurs validés par tournoi.

        for (Tournoi t : tournois) { // Parcourt tous les tournois.
            long count = gestion.getInscriptionsByStatus("VALIDEE").stream() // Récupère les inscriptions validées et les transforme en flux.
                    .filter(i -> i.getTournoi().getId() == t.getId()) // Filtre les inscriptions pour le tournoi actuel.
                    .count(); // Compte le nombre d'inscriptions correspondantes.
            joueursValidesParTournoi.put(t.getId(), count); // Stocke le compte dans la map avec l'ID du tournoi comme clé.
        } 

        request.setAttribute("inscriptions", inscriptionsEnAttente); // Définit les inscriptions en attente comme attribut de requête pour la JSP.
        request.setAttribute("tournois", tournois); // Définit la liste des tournois comme attribut de requête.
        request.setAttribute("joueursValidesParTournoi", joueursValidesParTournoi); // Définit la map des joueurs validés comme attribut de requête.
        request.getRequestDispatcher("/adminDashboard.jsp").forward(request, response); // Transfère au tableau de bord admin JSP.
    } // Ferme la méthode handleAdminDashboard.
    
    

    							// ============ Méthodes liées aux tournois ============
    
    
    private void handleAccueil(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour gérer la page d'accueil.
            throws ServletException, IOException { 
        request.setAttribute("tournois", gestion.getAllTournois()); // Définit tous les tournois comme attribut de requête pour la JSP.
        request.getRequestDispatcher("/accueil.jsp").forward(request, response); // Transfère à la page d'accueil JSP.
    } 

    private void handleSearch(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour gérer la recherche de tournois.
            throws ServletException, IOException { 
        String jeu = request.getParameter("jeu"); // Récupère le nom du jeu depuis les paramètres de la requête.
        request.setAttribute("tournois", gestion.getTournoisByJeu(jeu)); // Définit les tournois correspondant au jeu comme attribut de requête.
        request.setAttribute("jeu", jeu); // Définit le nom du jeu comme attribut de requête pour l'affichage.
        request.getRequestDispatcher("/accueil.jsp").forward(request, response); 
    } 

    private void handleEditForm(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour afficher le formulaire d'édition de tournoi.
            throws ServletException, IOException { 
        int id = Integer.parseInt(request.getParameter("id")); // Récupère et parse l'ID du tournoi depuis la requête.
        Tournoi tournoi = gestion.getTournoi(id); // Récupère le tournoi par ID depuis la base.
        request.setAttribute("tournoi", tournoi); // Définit le tournoi comme attribut de requête pour la JSP.
        request.getRequestDispatcher("/tournoi.jsp").forward(request, response); 
    }

    private void handleDeleteTournoi(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour supprimer un tournoi.
            throws IOException { 
        int id = Integer.parseInt(request.getParameter("id")); // Récupère et parse l'ID du tournoi depuis la requête.
        gestion.deleteTournoi(id); // Supprime le tournoi de la base de données.
        System.out.println("Tournoi supprimé: ID=" + id); 
        response.sendRedirect("accueil");
    } 

    private void handleTournoiForm(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour afficher le formulaire de tournoi.
            throws ServletException, IOException { 
        request.getRequestDispatcher("/tournoi.jsp").forward(request, response); // Transfère au formulaire de tournoi JSP.
    }

    private void handleTournoiSubmit(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour traiter la soumission du formulaire de tournoi.
            throws ServletException, IOException { 
        String id = request.getParameter("id"); // Récupère l'ID du tournoi depuis la requête (null pour un nouveau tournoi).
        String nom = request.getParameter("nom"); // Récupère le nom du tournoi depuis la requête.
        String jeu = request.getParameter("jeu"); // Récupère le nom du jeu depuis la requête.
        String status = request.getParameter("status"); // Récupère le statut du tournoi depuis la requête.

        Tournoi tournoi; // Déclare une variable pour stocker l'objet tournoi.
        if (id != null && !id.isEmpty()) { // Vérifie si un ID est fourni (indiquant une mise à jour).
            tournoi = gestion.getTournoi(Integer.parseInt(id)); // Récupère le tournoi existant par ID.
            tournoi.setNom(nom); // Met à jour le nom du tournoi.
            tournoi.setJeu(jeu); // Met à jour le nom du jeu.
            tournoi.setStatus(status); // Met à jour le statut du tournoi.
            gestion.updateTournoi(tournoi); // Persiste le tournoi mis à jour dans la base.
            System.out.println("Tournoi mis à jour: ID=" + id); // Affiche un message de confirmation de mise à jour.
        } else { 
            tournoi = new Tournoi(nom, jeu, new Date()); // Crée un nouveau tournoi avec les détails fournis.
            tournoi.setStatus(status != null ? status : "PLANIFIE"); // Définit le statut, par défaut "PLANIFIE" si null.
            gestion.addTournoi(tournoi); // Persiste le nouveau tournoi dans la base.
            System.out.println("Nouveau tournoi créé: " + nom); 
        } 
        response.sendRedirect("admin"); 
    } 
    
    

    								// ============ Méthodes liées aux joueurs ============
    
    
    private void handleAddJoueurForm(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour afficher le formulaire d'ajout de joueur.
            throws ServletException, IOException { 
        request.setAttribute("joueurs", gestion.getAllJoueurs()); 
        request.getRequestDispatcher("/addJoueur.jsp").forward(request, response); 
    } 

    private void handleAddJoueurSubmit(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour traiter la soumission d'ajout de joueur.
            throws ServletException, IOException { 
        String pseudo = request.getParameter("pseudo"); // Récupère le pseudonyme du joueur depuis la requête.
        String plateforme = request.getParameter("plateforme"); // Récupère la plateforme du joueur depuis la requête.

        // 1. Crée le Joueur
        Joueur joueur = new Joueur(pseudo, plateforme); // Crée un nouveau joueur avec le pseudonyme et la plateforme.
        gestion.addJoueur(joueur); // Persiste le joueur dans la base, générant un ID.

        // 2. Crée l'Utilisateur associé automatiquement
        Utilisateur user = new Utilisateur( // Crée un nouvel utilisateur associé au joueur.
            pseudo,          // Définit le nom d'utilisateur comme le pseudonyme du joueur.
            "user123",       // Définit un mot de passe par défaut.
            "USER"           // Définit le rôle comme "USER".
        ); 
        user.setJoueur(joueur); // Associe le joueur à l'utilisateur.
        gestion.addUtilisateur(user); // Persiste l'utilisateur dans la base.

     
        response.sendRedirect("addJoueur"); 
    } 
    
 // méthode pour rechercher des joueurs par pseudo
    private void handleSearchJoueur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pseudo = request.getParameter("pseudo");
        List<Joueur> joueurs;//// Déclaration d'une liste de joueurs pour stocker les résultats de la recherche
        if (pseudo != null && !pseudo.trim().isEmpty()) { //// Si le pseudo n'est pas nul et n'est pas une chaîne vide (après suppression des espaces)
        	// On récupère tous les joueurs depuis l'objet "gestion" 
            // Ensuite, on filtre ceux dont le pseudo contient la chaîne recherchée (insensible à la casse)
        	joueurs = gestion.getAllJoueurs().stream()
                    .filter(j -> j.getPseudo().toLowerCase().contains(pseudo.toLowerCase()))//“Je garde seulement les joueurs j dont le pseudo (en minuscules) contient le texte recherché (lui aussi en minuscules).”
                    .collect(Collectors.toList());// On collecte le résultat filtré dans une nouvelle liste

        } else {
        	// Si aucun pseudo n'a été fourni, on récupère simplement tous les joueurs sans filtrage
            joueurs = gestion.getAllJoueurs();
        }
        // On envoie la liste des joueurs à la JSP via l'objet "request"
        // La JSP pourra ensuite afficher ces joueurs dans une table ou une liste
        request.setAttribute("joueurs", joueurs);
        request.setAttribute("searchPseudo", pseudo);
        request.getRequestDispatcher("/addJoueur.jsp").forward(request, response);
    }

    private void handleDeleteJoueur(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour supprimer un joueur.
            throws ServletException, IOException { 
        int id = Integer.parseInt(request.getParameter("id")); // Récupère et parse l'ID du joueur depuis la requête.
        boolean force = "true".equals(request.getParameter("force")); // Vérifie si le paramètre "force" est vrai (non utilisé dans la logique).

        try { 
            gestion.deleteJoueur(id); // Supprime le joueur de la base de données.
            request.setAttribute("successMessage", "Joueur supprimé avec succès"); // Définit un message de succès pour la JSP.
        } catch (Exception e) { // Capture les erreurs pendant la suppression.
            request.setAttribute("successMessage","Joueur supprimé avec succès"); // Définit un message de succès (erreur : devrait être un message d'erreur).
        }

        // Recharge la liste
        request.setAttribute("joueurs", gestion.getAllJoueurs()); // Définit la liste mise à jour des joueurs comme attribut de requête.
        request.getRequestDispatcher("/addJoueur.jsp").forward(request, response); // Transfère à la page JSP d'ajout de joueur.
    } 

    
    
    
    							// ============ Méthodes liées aux inscriptions ============
    
    
    private void handleViewInscriptions(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour voir les inscriptions.
            throws ServletException, IOException { 
        int tournoiId = Integer.parseInt(request.getParameter("tournoiId")); // Récupère et parse l'ID du tournoi depuis la requête.
        Tournoi tournoi = gestion.getTournoi(tournoiId); // Récupère le tournoi par ID depuis la base.

        // Filtrer les inscriptions pour ne garder que celles avec le statut "VALIDEE"
        List<Inscription> inscriptionsValidees = tournoi.getInscriptions().stream() // Transforme les inscriptions du tournoi en flux.
                .filter(i -> "VALIDEE".equals(i.getStatus())) // Filtre pour ne garder que les inscriptions validées.
                .collect(Collectors.toList()); // Collecte les inscriptions filtrées dans une liste.

        request.setAttribute("tournoi", tournoi); // Définit le tournoi comme attribut de requête.
        request.setAttribute("inscriptions", inscriptionsValidees); // Définit les inscriptions validées comme attribut de requête.
        request.getRequestDispatcher("/inscriptions.jsp").forward(request, response); 
    } 
    
 // Méthode utilisée pour gérer la suppression d'une inscription via une requête HTTP
    private void handleDeleteInscription(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));//On récupère le paramètre "id"
            gestion.deleteInscription(id);//On appelle la méthode deleteInscription(id) de l’objet gestion.
            request.setAttribute("successMessage", "Inscription supprimée");
            response.sendRedirect("inscriptions?tournoiId=" + request.getParameter("tournoiId"));// On redirige l’utilisateur vers la page inscriptions
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    
    
    private void handleAddInscription(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour ajouter une inscription.
            throws ServletException, IOException { 
        HttpSession session = request.getSession(); // Récupère la session actuelle.
        String username = (String) session.getAttribute("username"); // Récupère le nom d'utilisateur depuis la session.

        if (username == null) { // Vérifie si aucun utilisateur n'est connecté.
            System.out.println("Aucun utilisateur connecté pour addInscription"); // Affiche un message indiquant qu'aucun utilisateur n'est connecté.
            response.sendRedirect("login");
            return; 
        } 

        int tournoiId; 
        try {
            tournoiId = Integer.parseInt(request.getParameter("tournoiId")); // Parse l'ID du tournoi depuis la requête.
        } catch (NumberFormatException e) {
            System.out.println("tournoiId invalide: " + request.getParameter("tournoiId")); 
            request.setAttribute("errorMessage", "ID de tournoi invalide");
            request.getRequestDispatcher("/error.jsp").forward(request, response); 
            return; 
        } 

        System.out.println("Tentative d'inscription pour utilisateur: " + username + ", tournoiId: " + tournoiId); 

        Tournoi tournoi = gestion.getTournoi(tournoiId); // Récupère le tournoi par ID.
        if (tournoi == null) { // Vérifie si le tournoi n'a pas été trouvé.
            System.out.println("Tournoi non trouvé: ID=" + tournoiId); // Affiche un message indiquant que le tournoi n'existe pas.
            request.setAttribute("errorMessage", "Tournoi non trouvé"); 
            request.getRequestDispatcher("/error.jsp").forward(request, response); 
            return; 
        } 

        Utilisateur utilisateur = gestion.getUtilisateurByUsername(username); // Récupère l'utilisateur par nom d'utilisateur.
        if (utilisateur == null) { // Vérifie si l'utilisateur n'a pas été trouvé.
            System.out.println("Utilisateur non trouvé: " + username); // Affiche un message indiquant que l'utilisateur n'existe pas.
            request.setAttribute("errorMessage", "Utilisateur non trouvé"); // Définit un message d'erreur.
            request.getRequestDispatcher("/error.jsp").forward(request, response); 
            return; 
        } 

        Joueur joueur = utilisateur.getJoueur(); // Récupère le joueur associé.
        if (joueur == null) { // Vérifie si aucun joueur n'est associé.
            System.out.println("Aucun joueur associé à l'utilisateur: " + username); // Affiche un message indiquant qu'aucun joueur n'est associé.
            request.setAttribute("errorMessage", "Aucun profil joueur associé. Veuillez contacter l'administrateur."); // Définit un message d'erreur.
            request.getRequestDispatcher("/error.jsp").forward(request, response); // Transfère à la page d'erreur.
            return; 
        } 

        
        //en attente////
        List<Inscription> existingInscriptions = gestion.getInscriptionsByJoueur(joueur.getPseudo()); // Récupère les inscriptions existantes du joueur.
        for (Inscription i : existingInscriptions) { // Parcourt les inscriptions du joueur.
            if (i.getTournoi().getId() == tournoiId && i.getStatus().equals("EN_ATTENTE")) { // Vérifie une inscription en attente pour le même tournoi.
                System.out.println("Demande en attente existante pour joueur: " + joueur.getPseudo() + ", tournoi: " + tournoiId); 
                request.setAttribute("errorMessage", "Vous avez déjà une demande en attente pour ce tournoi");
                request.getRequestDispatcher("/error.jsp").forward(request, response); // Transfère à la page d'erreur.
                return; 
            } // Ferme la vérification de l'inscription.
        } 

        try { // Débute un bloc try pour la création de l'inscription.
            Inscription inscription = new Inscription(new Date(), "EN_ATTENTE", tournoi, joueur); // Crée une nouvelle inscription en attente.
            gestion.addInscription(inscription); // Persiste l'inscription dans la base.
            System.out.println("Inscription créée pour joueur: " + joueur.getPseudo() + ", tournoi: " + tournoi.getNom() + ", ID: " + tournoiId); // Affiche un message de confirmation de création.
            response.sendRedirect("accueil"); 
        } catch (Exception e) { 
            System.out.println("Erreur lors de la création de l'inscription pour joueur: " + joueur.getPseudo() + ", tournoi: " + tournoiId); 
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Erreur lors de la création de l'inscription: " + e.getMessage()); 
            request.getRequestDispatcher("/error.jsp").forward(request, response); 
        } 
    } 

    private void handleValiderInscription(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour valider une inscription.
            throws ServletException, IOException { 
        int inscriptionId = Integer.parseInt(request.getParameter("id")); // Récupère et parse l'ID de l'inscription depuis la requête.

        Inscription inscription = gestion.getInscriptionById(inscriptionId); // Récupère l'inscription par ID.
        if (inscription == null) { // Vérifie si l'inscription n'a pas été trouvée.
            System.out.println("Inscription non trouvée: ID=" + inscriptionId); // Affiche un message indiquant que l'inscription n'existe pas.
            request.setAttribute("errorMessage", "Inscription non trouvée"); 
            request.getRequestDispatcher("/error.jsp").forward(request, response); 
            return; 
        } 

        if ("VALIDEE".equals(inscription.getStatus())) { // Vérifie si l'inscription est déjà validée.
            System.out.println("Inscription déjà validée: ID=" + inscriptionId); // Affiche un message indiquant que l'inscription est déjà validée.
            response.sendRedirect("admin"); // Redirige vers le tableau de bord admin.
            return; 
        } 

        try { // Débute un bloc try pour la validation.
            gestion.updateInscriptionStatus(inscriptionId, "VALIDEE"); // Met à jour le statut de l'inscription à "VALIDEE".

            Tournoi tournoi = inscription.getTournoi(); // Récupère le tournoi associé.
            Joueur joueur = inscription.getJoueur(); // Récupère le joueur associé.
            if (tournoi != null && joueur != null) { // Vérifie si le tournoi et le joueur existent.
                gestion.inscrireJoueur(tournoi.getId(), joueur.getId()); // Inscrit le joueur au tournoi.
            } else { // Si le tournoi ou le joueur est null.
                System.out.println("Tournoi ou joueur non trouvé pour l'inscription: ID=" + inscriptionId); // Affiche un message d'erreur.
                request.setAttribute("errorMessage", "Tournoi ou joueur non trouvé pour l'inscription"); // Définit un message d'erreur.
                request.getRequestDispatcher("/error.jsp").forward(request, response); // Transfère à la page d'erreur.
                return; 
            }

            System.out.println("Inscription validée et joueur associé: ID=" + inscriptionId); // Affiche un message de confirmation de validation.
            response.sendRedirect("admin"); // Redirige vers le tableau de bord admin.
        } catch (Exception e) { 
            System.out.println("Erreur lors de la validation de l'inscription ID=" + inscriptionId + ": " + e.getMessage()); // Affiche un message d'erreur.
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Erreur lors de la validation de l'inscription: " + e.getMessage()); 
            request.getRequestDispatcher("/error.jsp").forward(request, response); 
        } 
    } 

    
    
    private void handleRefuserInscription(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour refuser une inscription.
            throws ServletException, IOException { 
        int inscriptionId = Integer.parseInt(request.getParameter("id")); // Récupère et parse l'ID de l'inscription depuis la requête.

        Inscription inscription = gestion.getInscriptionById(inscriptionId); // Récupère l'inscription par ID.
        if (inscription == null) { 
            System.out.println("Inscription non trouvée: ID=" + inscriptionId); 
            request.setAttribute("errorMessage", "Inscription non trouvée"); 
            request.getRequestDispatcher("/error.jsp").forward(request, response); 
            return; 
        } 

        try { 
            gestion.updateInscriptionStatus(inscriptionId, "REFUSEE");

            Tournoi tournoi = inscription.getTournoi(); // Récupère le tournoi associé.
            Joueur joueur = inscription.getJoueur(); // Récupère le joueur associé.
            if (tournoi != null && joueur != null && tournoi.getJoueurs().contains(joueur)) { // Vérifie si le joueur est inscrit au tournoi.
                tournoi.getJoueurs().remove(joueur); // Retire le joueur de la liste des joueurs du tournoi.
                joueur.getTournois().remove(tournoi); // Retire le tournoi de la liste des tournois du joueur.
                gestion.updateTournoi(tournoi); // Persiste le tournoi mis à jour.
                gestion.updateJoueur(joueur); // Persiste le joueur mis à jour.
                System.out.println("Joueur dissocié du tournoi après refus: Joueur ID=" + joueur.getId() + ", Tournoi ID=" + tournoi.getId()); 
            }
            System.out.println("Inscription refusée: ID=" + inscriptionId); 
            response.sendRedirect("admin");
            
        } catch (Exception e) {
            System.out.println("Erreur lors du refus de l'inscription ID=" + inscriptionId + ": " + e.getMessage()); 
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Erreur lors du refus de l'inscription: " + e.getMessage()); 
            request.getRequestDispatcher("/error.jsp").forward(request, response); 
        } 
    } 
    
    
    

    						// ============ Méthodes du tableau de bord utilisateur ============
    
    
    private void handleUserDashboard(HttpServletRequest request, HttpServletResponse response) // Définit la méthode pour gérer le tableau de bord utilisateur.
            throws ServletException, IOException { 
        HttpSession session = request.getSession(); // Récupère la session actuelle.
        String username = (String) session.getAttribute("username"); // Récupère le nom d'utilisateur depuis la session.
        System.out.println("Accès au tableau de bord utilisateur pour: " + username); // Affiche un message pour l'accès au tableau de bord.

        Utilisateur utilisateur = gestion.getUtilisateurByUsername(username); 
        if (utilisateur == null) { 
            System.out.println("Utilisateur non trouvé dans userDashboard: " + username); // Affiche un message indiquant que l'utilisateur n'existe pas.
            request.setAttribute("errorMessage", "Utilisateur non trouvé"); // Définit un message d'erreur.
            request.getRequestDispatcher("/error.jsp").forward(request, response); // Transfère à la page d'erreur.
            return; 
        } 

        Joueur joueur = utilisateur.getJoueur(); // Récupère le joueur associé.
        if (joueur == null) { // Vérifie si aucun joueur n'est associé.
            System.out.println("Aucun joueur associé dans userDashboard pour: " + username); // Affiche un message indiquant qu'aucun joueur n'est associé.
            request.setAttribute("errorMessage", "Aucun profil joueur associé"); // Définit un message d'erreur.
            request.getRequestDispatcher("/error.jsp").forward(request, response); // Transfère à la page d'erreur.
            return;
        } 

        List<Tournoi> tournois = gestion.getAllTournois(); 
        List<Inscription> mesInscriptions = gestion.getInscriptionsByJoueur(joueur.getPseudo()); // Récupère les inscriptions du joueur.

        request.setAttribute("tournois", tournois); // Définit les tournois comme attribut de requête.
        request.setAttribute("mesInscriptions", mesInscriptions); // Définit les inscriptions du joueur comme attribut de requête.
        request.getRequestDispatcher("/userDashboard.jsp").forward(request, response); // Transfère au tableau de bord utilisateur JSP.
    } 

    
    
    
    								// ============ Méthodes de gestion des erreurs ============
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e) // Définit la méthode pour gérer les erreurs.
            throws ServletException, IOException { 
        System.out.println("Erreur interceptée: " + e.getMessage());
        e.printStackTrace();
        request.setAttribute("errorMessage", "Erreur: " + (e.getMessage() != null ? e.getMessage() : "Une erreur inattendue s'est produite")); // Définit un message d'erreur, avec un message par défaut si null.
        request.getRequestDispatcher("/error.jsp").forward(request, response); // Transfère à la page JSP d'erreur.
    } // Ferme la méthode handleError.



} 