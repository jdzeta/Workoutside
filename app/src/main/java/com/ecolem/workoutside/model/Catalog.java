package com.ecolem.workoutside.model;

import java.util.ArrayList;

/**
 * Created by akawa_000 on 23/10/2015.
 */
public class Catalog
{
    private String nom;
    private String image;
    private String description;
    private ArrayList<Movement> movements;

    public Catalog() {}

    public Catalog(String nom) {
        this.nom = nom;
        this.movements = new ArrayList<>();
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

    public ArrayList<Movement> getMovements() {
        return movements;
    }

    public void setMovements(ArrayList<Movement> movements) { this.movements = movements; }
}
