package dao;

import entity.*;
import javax.persistence.*;
import java.util.*;

// Classe implémentant l'interface IGestionTournoi pour la gestion des tournois
public class GestionTournoiJPA2 implements IGestionTournoi {
    // Factory pour créer des EntityManager
    private EntityManagerFactory emf;
    // EntityManager pour les opérations JPA
    private EntityManager em;

    // Constructeur initialisant les ressources JPA
    public GestionTournoiJPA2() {
        // Crée l'EntityManagerFactory avec l'unité de persistance définie
        this.emf = Persistence.createEntityManagerFactory("esportPU");
        // Crée un EntityManager à partir de la factory
        this.em = emf.createEntityManager();
    }
    
    
    
    

    // ============ Gestion des Tournois ============
    // Méthode pour ajouter un tournoi
    @Override
    public void addTournoi(Tournoi t) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Persiste l'entité tournoi
            em.persist(t);
            // Affiche un message de confirmation
            System.out.println("Tournoi ajouté: " + t.getNom());
        });
    }
    

    // Récupère tous les tournois
    @Override
    public List<Tournoi> getAllTournois() {
        // Requête JPQL pour sélectionner tous les tournois
        return em.createQuery("SELECT t FROM Tournoi t", Tournoi.class).getResultList();//Tournoi.class est une information essentielle qui permet à JPA de faire le lien entre votre modèle objet et le modèle relationnel de manière type-safe.
    }
    

    // Récupère les tournois par jeu
    @Override
    public List<Tournoi> getTournoisByJeu(String jeu) {
        // Requête JPQL avec paramètre pour filtrer par jeu
        return em.createQuery("SELECT t FROM Tournoi t WHERE t.jeu LIKE :jeu", Tournoi.class)
                // Définit le paramètre de la requête
                .setParameter("jeu", "%" + jeu + "%")
                // Exécute la requête et retourne les résultats
                .getResultList();
    }

    
    // Récupère un tournoi par son ID
    @Override
    public Tournoi getTournoi(int id) {
        // Recherche l'entité par son ID
        Tournoi tournoi = em.find(Tournoi.class, id);
        // Affiche un message de log
        System.out.println("Recherche tournoi ID=" + id + ": " + (tournoi != null ? "Trouvé" : "Non trouvé"));
        // Retourne le tournoi trouvé (ou null)
        return tournoi;
    }

    
    // Supprime un tournoi par son ID
    @Override
    public void deleteTournoi(int id) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Recherche le tournoi à supprimer
            Tournoi t = em.find(Tournoi.class, id);
            // Si trouvé, le supprime
            if (t != null) {
                em.remove(t);
                // Affiche un message de confirmation
                System.out.println("Tournoi supprimé: ID=" + id);
            }
        });
    }

    
    // Met à jour un tournoi
    @Override
    public void updateTournoi(Tournoi t) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Met à jour l'entité
            em.merge(t);
            // Affiche un message de confirmation
            System.out.println("Tournoi mis à jour: " + t.getNom());
        });
    }
    
    
    
    
    

    // ============ Gestion des Joueurs ============
    // Ajoute un joueur
    @Override
    public void addJoueur(Joueur j) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Persiste le joueur
            em.persist(j);
            // Force la génération de l'ID
            em.flush();
            // Affiche un message avec l'ID généré
            System.out.println("Joueur ajouté: " + j.getPseudo() + ", ID: " + j.getId());
        });
    }

    
    // Récupère tous les joueurs
    @Override
    public List<Joueur> getAllJoueurs() {
        // Requête JPQL pour tous les joueurs
        return em.createQuery("SELECT j FROM Joueur j", Joueur.class).getResultList();
    }

    
    // Récupère un joueur par ID
    /*
    @Override
    public Joueur getJoueur(int id) {
        // Recherche le joueur par ID
        Joueur joueur = em.find(Joueur.class, id);
        // Affiche un message de log
        System.out.println("Recherche joueur ID=" + id + ": " + (joueur != null ? "Trouvé" : "Non trouvé"));
        // Retourne le joueur trouvé (ou null)
        return joueur;
    }*/

    
    // Supprime un joueur par ID
    @Override
    public void deleteJoueur(int id) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Recherche le joueur à supprimer
            Joueur joueur = em.find(Joueur.class, id);
            // Si trouvé
            if (joueur != null) {
                // 1. Supprime toutes les inscriptions du joueur
                List<Inscription> inscriptions = em.createQuery(
                    "SELECT i FROM Inscription i WHERE i.joueur.id = :joueurId", Inscription.class)
                    .setParameter("joueurId", id)
                    .getResultList();
                
                // Parcourt et supprime chaque inscription
                for (Inscription i : inscriptions) {
                    em.remove(i);
                }

                // 2. Désassocie le joueur de tous les tournois
                for (Tournoi t : new ArrayList<>(joueur.getTournois())) {
                    t.getJoueurs().remove(joueur);
                    em.merge(t);
                }

                // 3. Supprime l'utilisateur associé si existe
                if (joueur.getUtilisateur() != null) {
                    // Rompt la relation bidirectionnelle
                    joueur.getUtilisateur().setJoueur(null);
                    em.remove(joueur.getUtilisateur());
                }

                // 4. Rafraîchit l'entité avant suppression
                em.flush();
                em.refresh(joueur);
                
                // 5. Supprime enfin le joueur
                em.remove(joueur);
                // Affiche un message de confirmation
                System.out.println("Joueur supprimé avec succès: ID=" + id);
            }
        });
    }

    
    
    // Met à jour un joueur
    @Override
    public void updateJoueur(Joueur j) {///***///
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Met à jour l'entité
            em.merge(j);
            // Affiche un message de confirmation
            System.out.println("Joueur mis à jour: " + j.getPseudo());
        });
    }

    
    // Récupère un joueur par son pseudo
    @Override
    public Joueur getJoueurByUsername(String pseudo) {///***///
        try {
            // Requête JPQL pour trouver un joueur par pseudo
            Joueur joueur = em.createQuery("SELECT j FROM Joueur j WHERE j.pseudo = :pseudo", Joueur.class)
                    .setParameter("pseudo", pseudo)
                    .getSingleResult();
            // Affiche un message de log
            System.out.println("Joueur trouvé par pseudo: " + pseudo);
            return joueur;
        } catch (NoResultException e) {
            // Affiche un message si non trouvé
            System.out.println("Joueur non trouvé par pseudo: " + pseudo);
            return null;
        }
    }
    
    
    
    
    

    // ============ Gestion des Inscriptions ============
    // Ajoute une inscription
    @Override
    public void addInscription(Inscription i) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Persiste l'inscription
            em.persist(i);
          
        });
    }
/*
    // Récupère toutes les inscriptions
    @Override
    public List<Inscription> getAllInscriptions() {
        // Requête JPQL pour toutes les inscriptions
        return em.createQuery("SELECT i FROM Inscription i", Inscription.class).getResultList();
    }
*/
    
   
    // Récupère une inscription par ID
    //@Override
   // public Inscription getInscription(int id) {///***///
        // Recherche l'inscription par ID
       // Inscription inscription = em.find(Inscription.class, id);
        // Affiche un message de log
       // System.out.println("Recherche inscription ID=" + id + ": " + (inscription != null ? "Trouvée" : "Non trouvée"));
        // Retourne l'inscription (ou null)
       // return inscription;
  // } 


    

    // Récupère une inscription par ID (méthode alternative)
    @Override
    public Inscription getInscriptionById(int id) {///***///
        // Recherche directe par ID
        return em.find(Inscription.class, id);
    }

    // Supprime une inscription par ID
    @Override
    public void deleteInscription(int id) {
        executeInTransaction(() -> {
            Inscription i = em.find(Inscription.class, id);
            if (i != null) {
                // Retirer l'inscription des collections du tournoi et du joueur
                Tournoi tournoi = i.getTournoi();
                Joueur joueur = i.getJoueur();
                if (tournoi != null) {
                    tournoi.getInscriptions().remove(i);
                }
                if (joueur != null) {
                    joueur.getInscriptions().remove(i);
                }
                // Supprimer l'inscription
                em.remove(i);
                System.out.println("Inscription supprimée: ID=" + id);
            }
        });
    }
    

    
    // Récupère les inscriptions par statut
    @Override
    public List<Inscription> getInscriptionsByStatus(String status) {///***////
        // Requête JPQL filtrée par statut
        return em.createQuery("SELECT i FROM Inscription i WHERE i.status = :status", Inscription.class)
                .setParameter("status", status)
                .getResultList();
    }

    
    // Récupère les inscriptions d'un joueur par pseudo
    @Override
    public List<Inscription> getInscriptionsByJoueur(String pseudo) {
        // Requête JPQL avec jointure
        List<Inscription> inscriptions = em.createQuery("SELECT i FROM Inscription i JOIN i.joueur j WHERE j.pseudo = :pseudo", Inscription.class)
                .setParameter("pseudo", pseudo)
                .getResultList();
        // Affiche le nombre d'inscriptions trouvées
        System.out.println("Inscriptions trouvées pour joueur: " + pseudo + ", Nombre: " + inscriptions.size());
        return inscriptions;
    }

    
    // Met à jour le statut d'une inscription
    @Override
    public void updateInscriptionStatus(int inscriptionId, String status) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Recherche l'inscription à mettre à jour
            Inscription inscription = em.find(Inscription.class, inscriptionId);
            // Si trouvée, met à jour le statut
            if (inscription != null) {
                inscription.setStatus(status);
                em.merge(inscription);
                // Affiche un message de confirmation
                System.out.println("Statut inscription mis à jour: ID=" + inscriptionId + ", Statut=" + status);
            }
        });
    }

    
    // Inscrit un joueur à un tournoi
    @Override
    public void inscrireJoueur(int tournoiId, int joueurId) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Affiche un message de début
            System.out.println("Début de l'inscription: Tournoi ID=" + tournoiId + ", Joueur ID=" + joueurId);
            
            // Recherche le tournoi et le joueur
            Tournoi tournoi = em.find(Tournoi.class, tournoiId);
            Joueur joueur = em.find(Joueur.class, joueurId);
            
            // Vérifie si le tournoi existe
            if (tournoi == null) {
                System.out.println("Tournoi non trouvé: ID=" + tournoiId);
                throw new IllegalArgumentException("Tournoi non trouvé: ID=" + tournoiId);
            }
            // Vérifie si le joueur existe
            if (joueur == null) {
                System.out.println("Joueur non trouvé: ID=" + joueurId);
                throw new IllegalArgumentException("Joueur non trouvé: ID=" + joueurId);
            }

            // Affiche les détails trouvés
            System.out.println("Tournoi trouvé: " + tournoi.getNom() + ", Joueur trouvé: " + joueur.getPseudo());
            
            // Vérifie si le joueur n'est pas déjà inscrit
            if (!tournoi.getJoueurs().contains(joueur)) {
                // Ajoute le joueur au tournoi et vice versa
                tournoi.getJoueurs().add(joueur);
                joueur.getTournois().add(tournoi);
                // Affiche un message de confirmation
                System.out.println("Association ajoutée: Joueur " + joueur.getPseudo() + " au tournoi " + tournoi.getNom());
                // Met à jour les entités
                em.merge(tournoi);
                em.merge(joueur);
                // Force la synchronisation
                em.flush();
                System.out.println("Association persistée dans la base: Tournoi ID=" + tournoiId + ", Joueur ID=" + joueurId);
            } else {
                // Affiche un message si déjà inscrit
                System.out.println("Joueur déjà inscrit au tournoi: Joueur ID=" + joueurId + ", Tournoi ID=" + tournoiId);
            }
        });
    }

    
    /*
    // Désinscrit un joueur d'un tournoi
    @Override
    public void desinscrireJoueur(int joueurId, int tournoiId) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Recherche le joueur et le tournoi
            Joueur j = em.find(Joueur.class, joueurId);
            Tournoi t = em.find(Tournoi.class, tournoiId);
            
            // Si les deux existent
            if (j != null && t != null) {
                // Supprime le joueur du tournoi et vice versa
                t.getJoueurs().remove(j);
                j.getTournois().remove(t);
                // Met à jour les entités
                em.merge(t);
                em.merge(j);
                // Affiche un message de confirmation
                System.out.println("Joueur " + j.getPseudo() + " désinscrit du tournoi: " + tournoiId);
            }
        });
    }
*/
  
    
    /*
    // Récupère les joueurs d'un tournoi
    @Override
    public List<Joueur> getJoueursByTournoi(int tournoiId) {
        // Requête JPQL avec jointure
        return em.createQuery("SELECT j FROM Joueur j JOIN j.tournois t WHERE t.id = :tournoiId", Joueur.class)
                .setParameter("tournoiId", tournoiId)
                .getResultList();
    
    }
    
*/
    
    /*
    // Récupère les tournois d'un joueur
    @Override
    public List<Tournoi> getTournoisByJoueur(int joueurId) {
        // Requête JPQL avec jointure
        return em.createQuery("SELECT t FROM Tournoi t JOIN t.joueurs j WHERE j.id = :joueurId", Tournoi.class)
                .setParameter("joueurId", joueurId)
                .getResultList();
    }
    */
    
    
    
    
    
    
    
    
    

    // ============ Gestion des Utilisateurs ============
    // Ajoute un utilisateur
    @Override
    public void addUtilisateur(Utilisateur u) {
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Persiste l'utilisateur
            em.persist(u);
            // Affiche un message de confirmation
            System.out.println("Utilisateur créé: " + u.getUsername());
        });
    }

    
    // Récupère un utilisateur par nom d'utilisateur
    @Override
    public Utilisateur getUtilisateurByUsername(String username) {///***///
        try {
            // Requête JPQL pour trouver un utilisateur
            Utilisateur utilisateur = em.createQuery("SELECT u FROM Utilisateur u WHERE u.username = :username", Utilisateur.class)
                    .setParameter("username", username)
                    .getSingleResult();
            // Affiche un message de log
            System.out.println("Utilisateur trouvé: " + username);
            return utilisateur;
        } catch (NoResultException e) {
            // Affiche un message si non trouvé
            System.out.println("Utilisateur non trouvé: " + username);
            return null;
        }
    }

    
    // Met à jour un utilisateur
    @Override
    public void updateUtilisateur(Utilisateur u) {///*!*///
        // Exécute dans une transaction
        executeInTransaction(() -> {
            // Met à jour l'entité
            em.merge(u);
            // Affiche un message de confirmation
            System.out.println("Utilisateur mis à jour: " + u.getUsername());
        });
    }

    
    
    
    
    
    
    
    // 								============ Méthodes Utilitaires ============
    
    // Méthode utilitaire pour exécuter des opérations dans une transaction
    private void executeInTransaction(Runnable operation) {
        // Récupère la transaction courante
        EntityTransaction et = em.getTransaction();
        try {
            // Démarre la transaction
            et.begin();
            // Exécute l'opération
            operation.run();
            // Valide la transaction
            et.commit();
        } catch (Exception e) {
            // En cas d'erreur, annule la transaction si active
            if (et != null && et.isActive()) {
                et.rollback();
            }
            // Affiche l'erreur
            System.out.println("Erreur lors de l'opération en base: " + e.getMessage());
            // Relance une exception
            throw new RuntimeException("Erreur lors de l'opération en base de données", e);
        }
    }
}