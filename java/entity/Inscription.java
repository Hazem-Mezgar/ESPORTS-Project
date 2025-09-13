package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date dateInscription;
    private String status;

    @ManyToOne
    @JoinColumn(name = "tournoi_id")
    private Tournoi tournoi;

    @ManyToOne
    @JoinColumn(name = "joueur_id")
    private Joueur joueur;

    // Constructeurs
    public Inscription() {
    }

    public Inscription(Date dateInscription, String status, Tournoi tournoi, Joueur joueur) {
        this.dateInscription = dateInscription;
        this.status = status;
        setTournoi(tournoi); // Utiliser le setter pour maintenir la relation bidirectionnelle
        setJoueur(joueur);// Utilise le setter pour mettre à jour la relation bidirectionnelle 
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tournoi getTournoi() {
        return tournoi;
    }

    public void setTournoi(Tournoi tournoi) {
        this.tournoi = tournoi;
     // Si le tournoi n'est pas null et ne contient pas déjà cette inscription
        if (tournoi != null && !tournoi.getInscriptions().contains(this)) {
            tournoi.getInscriptions().add(this); // On ajoute cette inscription à la liste du tournoi
        }
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;// L'attribut local de l'inscription reçoit le tournoi donné
        
     
        if (joueur != null && !joueur.getInscriptions().contains(this)) {
            joueur.getInscriptions().add(this);
        }
    }
}