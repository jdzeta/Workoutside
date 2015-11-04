package com.ecolem.workoutside.model;

/**
 * Created by akawa_000 on 23/10/2015.
 */
public class Comment
{
    private User user;
    private float note;
    private String comment;

    public Comment(User user, float note) {
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
