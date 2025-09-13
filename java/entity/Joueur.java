package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Joueur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String pseudo;
    private String plateforme;

    @OneToMany(mappedBy = "joueur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscription> inscriptions = new ArrayList<>(); // Initialisation ici

    @ManyToMany(mappedBy = "joueurs")
    private Set<Tournoi> tournois = new HashSet<>();
    @OneToOne(mappedBy = "joueur")  // Relation inverse
    private Utilisateur utilisateur;

    // Constructeurs
    public Joueur() {
        this.inscriptions = new ArrayList<>(); // Initialisation dans le constructeur
        this.tournois = new HashSet<>();
    }

    public Joueur(String pseudo, String plateforme) {
        this.pseudo = pseudo;
        this.plateforme = plateforme;
        this.inscriptions = new ArrayList<>(); // Initialisation dans le constructeur
        this.tournois = new HashSet<>();
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPlateforme() {
        return plateforme;
    }

    public void setPlateforme(String plateforme) {
        this.plateforme = plateforme;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public Set<Tournoi> getTournois() {
        return tournois;
    }

    public void setTournois(Set<Tournoi> tournois) {
        this.tournois = tournois;
    }
 // Ajoutez le getter/setter
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}