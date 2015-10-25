package com.ecolem.workoutside.object;

import java.util.HashMap;

/**
 * Created by akawa_000 on 22/10/2015.
 */
public class Mouvement
{
    private String nom;
    private String image;
    private String description;

    public Mouvement(String nom, String image, String description) {
        this.nom = nom;
        this.image = image;
        this.description = description;
    }

    public HashMap<String, String> mouvMap(){
        HashMap<String, String> mouvM = new HashMap<String, String>();
        mouvM.put("nom", this.nom);
        mouvM.put("image", this.image);
        mouvM.put("description", this.description);
        return mouvM;
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
}
