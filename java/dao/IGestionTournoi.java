// Déclaration du package dans lequel se trouve cette interface
package dao;

// Importation des classes nécessaires depuis le package "entity"
import entity.*;
import java.util.List;

// Déclaration de l'interface IGestionTournoi
// Une interface définit les méthodes qu'une classe doit implémenter, sans fournir leur implémentation.
public interface IGestionTournoi {
											
	
											    // ---------------------
											    // GESTION DES TOURNOIS
											    // ---------------------

	
    // Ajouter un nouveau tournoi dans la base de données
    void addTournoi(Tournoi t);

    
    // Récupérer la liste de tous les tournois
    List<Tournoi> getAllTournois();

    
    // Récupérer les tournois en fonction du nom du jeu (ex: "FIFA", "LOL")
    List<Tournoi> getTournoisByJeu(String jeu);

    
    // Récupérer un tournoi à partir de son identifiant unique (id)
    Tournoi getTournoi(int id);

    
    // Mettre à jour les informations d'un tournoi existant
    void updateTournoi(Tournoi t);

    
    // Supprimer un tournoi de la base en fonction de son identifiant
    void deleteTournoi(int id);

    
    
										    // ---------------------
										    // GESTION DES JOUEURS
										    // ---------------------

    // Ajouter un nouveau joueur dans la base de données
    void addJoueur(Joueur j);

    // Récupérer tous les joueurs enregistrés
    List<Joueur> getAllJoueurs();

    // Récupérer un joueur à partir de son identifiant
   // Joueur getJoueur(int id);

    // Récupérer un joueur à partir de son pseudo (nom d'utilisateur)
    Joueur getJoueurByUsername(String pseudo);

    // Mettre à jour les informations d’un joueur (ex : pseudo, email, etc.)
    void updateJoueur(Joueur j);

    // Supprimer un joueur de la base selon son identifiant
    void deleteJoueur(int id);
    
    
    
    

											    // -------------------------
											    // GESTION DES INSCRIPTIONS
											    // -------------------------

    // Ajouter une nouvelle inscription (un joueur qui s'inscrit à un tournoi)
    void addInscription(Inscription i);

    // Récupérer toutes les inscriptions effectuées
   // List<Inscription> getAllInscriptions();

    // Récupérer une inscription à partir de son identifiant
    //Inscription getInscription(int id);

    // (Redondant mais présent) : même fonction que ci-dessus
    Inscription getInscriptionById(int id);

    // Récupérer les inscriptions selon leur statut (ex : "acceptée", "en attente", "refusée")
    List<Inscription> getInscriptionsByStatus(String status);

    // Récupérer les inscriptions faites par un joueur à partir de son pseudo
    List<Inscription> getInscriptionsByJoueur(String pseudo);

    // Obtenir la liste des joueurs inscrits à un tournoi donné
    //List<Joueur> getJoueursByTournoi(int tournoiId);

    // Obtenir la liste des tournois auxquels un joueur est inscrit
    //List<Tournoi> getTournoisByJoueur(int joueurId);

    // Mettre à jour le statut d’une inscription (ex : changer de "en attente" à "acceptée")
    void updateInscriptionStatus(int inscriptionId, String status);

    // Inscrire un joueur à un tournoi (liaison entre joueurId et tournoiId)
    void inscrireJoueur(int joueurId, int tournoiId);

    // Désinscrire un joueur d’un tournoi (supprime l'inscription)
    //void desinscrireJoueur(int joueurId, int tournoiId);

    // Supprimer complètement une inscription par son identifiant
    void deleteInscription(int id);
    
    

									    // --------------------------
									    // GESTION DES UTILISATEURS
									    // --------------------------

    
    // Ajouter un nouvel utilisateur (admin ou joueur avec accès au système)
    void addUtilisateur(Utilisateur user);

    // Récupérer les informations d’un utilisateur à partir de son nom d’utilisateur
    Utilisateur getUtilisateurByUsername(String username);

    // Mettre à jour les informations d’un utilisateur
    void updateUtilisateur(Utilisateur u);
}
