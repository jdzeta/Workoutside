package com.ecolem.workoutside.object;

import java.util.HashMap;

/**
 * Created by akawa_000 on 23/10/2015.
 */
public class Categorie
{
    private String nom;
    private HashMap<String, Catalogue> catalogues;

    public Categorie(String nom) {
        this.nom = nom;
        this.catalogues = new HashMap<String, Catalogue>();
    }

    public HashMap<String, String> catMap(){
        HashMap<String, String> catM = new HashMap<String, String>();
        //@TODO set catMap code
        return catM;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public HashMap<String, Catalogue> getCatalogues() {
        return catalogues;
    }

    public void setCatalogues(HashMap<String, Catalogue> catalogues) {
        this.catalogues = catalogues;
    }
}
