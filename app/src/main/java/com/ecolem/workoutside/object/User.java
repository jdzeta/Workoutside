package com.ecolem.workoutside.object;

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
    private String dateNaissance;
    private String ville;
    private String niveau;
    private String photo;
    private String description;
    private String poids;
    private String taille;
    private HashMap<String, User> amis;

    public User(String pseudo, String email, String mdp, String nom, String prenom, String sexe, String dateNaissance, String ville, String niveau) {
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
        this.poids = "";
        this.taille = "";
        this.amis = null;
    }

    public User(String pseudo, String email, String mdp, String nom, String prenom, String sexe, String dateNaissance, String ville, String niveau, String photo, String description, String poids, String taille) {
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

    public HashMap<String, String> userMap(){
        HashMap<String, String> userM = new HashMap<String, String>();
        userM.put("email", this.email);
        userM.put("mdp", this.mdp);
        userM.put("nom", this.nom);
        userM.put("prenom", this.prenom);
        userM.put("sexe", this.sexe);
        userM.put("date de naissance", this.dateNaissance);
        userM.put("ville", this.ville);
        userM.put("niveau", this.niveau);
        return userM;
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

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
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

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public HashMap<String, User> getAmis() {
        return amis;
    }

    public void setAmis(HashMap<String, User> amis) {
        this.amis = amis;
    }
}
