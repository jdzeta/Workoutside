package com.ecolem.workoutside.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by akawa_000 on 23/10/2015.
 */
public class Event implements Serializable {
    private String name;
    private Date date;
    private double latitude;
    private double longitude;
    private String description;
    private int minLevel;
    private int maxParticipants;
    private HashMap<Integer, User> participants;
    private float note;
    private HashMap<Integer, Comment> comments;
    private User creator;

    public Event() {}

    public Event(String name, Date date, double latitude, double longitude, String description, Integer minLevel, Integer maxParticipants, User creator) {
        this.name = name;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.minLevel = minLevel;
        this.maxParticipants = maxParticipants;
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
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

    public HashMap<Integer, Comment> getComments() {
        return comments;
    }

    public void setComments(HashMap<Integer, Comment> comments) {
        this.comments = comments;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public boolean hasSameDate(Event event){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(this.getDate());

        Calendar c2 = Calendar.getInstance();
        c2.setTime(event.getDate());

        return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);

    }
}
