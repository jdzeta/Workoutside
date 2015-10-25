package com.ecolem.workoutside.object;

/**
 * Created by akawa_000 on 23/10/2015.
 */
public class Commentaire
{
    private User user;
    private float note;
    private String comment;

    public Commentaire(User user, float note) {
        this.user = user;
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
