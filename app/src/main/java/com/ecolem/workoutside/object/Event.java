package com.ecolem.workoutside.object;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by akawa_000 on 23/10/2015.
 */
public class Event
{
    private String nom;
    private Date date;
    private String lieu;
    private String description;
    private Integer niveauMin;
    private Integer limiteP;
    private HashMap<Integer,User> participants;
    private float note;
    private HashMap<Integer,Commentaire> commentaires;

    public Event(String nom, Date date, String lieu, String description, Integer niveauMin) {
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.description = description;
        this.niveauMin = niveauMin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNiveauMin() {
        return niveauMin;
    }

    public void setNiveauMin(Integer niveauMin) {
        this.niveauMin = niveauMin;
    }

    public Integer getLimiteP() {
        return limiteP;
    }

    public void setLimiteP(Integer limiteP) {
        this.limiteP = limiteP;
    }

    public HashMap<Integer, User> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<Integer, User> participants) {
        this.participants = participants;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public HashMap<Integer, Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(HashMap<Integer, Commentaire> commentaires) {
        this.commentaires = commentaires;
    }
}
