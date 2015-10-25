package com.ecolem.workoutside.object;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by akawa_000 on 21/10/2015.
 */
public class User
{
    private String pseudo;
    private String email;
    private String mdp;
    private String nom;
    private String prenom;
    private String sexe;
    private Date dateNaissance;
    private String ville;
    private String niveau;
    private String photo;
    private String description;
    private Float poids;
    private Float taille;
    private HashMap<String, User> amis;

    public User(String pseudo, String email, String mdp, String nom, String prenom, String sexe, Date dateNaissance, String ville, String niveau) {
        this.pseudo = pseudo;
        this.email = email;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.ville = ville;
        this.niveau = niveau;

        this.photo = "";
        this.description = "";
        this.poids = null;
        this.taille = null;
        this.amis = null;
    }

    public User(String pseudo, String email, String mdp, String nom, String prenom, String sexe, Date dateNaissance, String ville, String niveau, String photo, String description, Float poids, Float taille) {
        this.pseudo = pseudo;
        this.email = email;
        this.mdp = mdp;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.ville = ville;
        this.niveau = niveau;
        this.photo = photo;
        this.description = description;
        this.poids = poids;
        this.taille = taille;

        this.amis = null;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPoids() {
        return poids;
    }

    public void setPoids(Float poids) {
        this.poids = poids;
    }

    public Float getTaille() {
        return taille;
    }

    public void setTaille(Float taille) {
        this.taille = taille;
    }

    public HashMap<String, User> getAmis() {
        return amis;
    }

    public void setAmis(HashMap<String, User> amis) {
        this.amis = amis;
    }
}
