package com.ecolem.workoutside.object;

import java.util.HashMap;

/**
 * Created by akawa_000 on 23/10/2015.
 */
public class Catalogue
{
    private String nom;
    private String image;
    private String description;
    private HashMap<String, Mouvement> mouvements;

    public Catalogue(String nom) {
        this.nom = nom;
        this.mouvements = new HashMap<String, Mouvement>();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, Mouvement> getMouvements() {
        return mouvements;
    }

    public void setMouvements(HashMap<String, Mouvement> mouvements) {
        this.mouvements = mouvements;
    }
}
