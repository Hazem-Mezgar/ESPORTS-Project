package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Tournoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String jeu;
    private Date date;
    private String status;

    @OneToMany(mappedBy = "tournoi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscription> inscriptions = new ArrayList<>(); // Initialisation ici

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "tournoi_joueur",
        joinColumns = @JoinColumn(name = "tournoi_id"),
        inverseJoinColumns = @JoinColumn(name = "joueur_id")
    )
    private Set<Joueur> joueurs = new HashSet<>();

    // Constructeurs
    public Tournoi() {
        this.inscriptions = new ArrayList<>(); // Initialisation dans le constructeur
        this.joueurs = new HashSet<>();
    }

    public Tournoi(String nom, String jeu, Date date) {
        this.nom = nom;
        this.jeu = jeu;
        this.date = date;
        this.inscriptions = new ArrayList<>(); // Initialisation dans le constructeur
        this.joueurs = new HashSet<>();
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getJeu() {
        return jeu;
    }

    public void setJeu(String jeu) {
        this.jeu = jeu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public Set<Joueur> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(Set<Joueur> joueurs) {
        this.joueurs = joueurs;
    }
}