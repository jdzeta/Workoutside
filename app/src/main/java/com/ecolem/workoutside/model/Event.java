package com.ecolem.workoutside.model;

import com.firebase.geofire.GeoLocation;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by akawa_000 on 23/10/2015.
 */
public class Event {
    private String name;
    private Date date;
    private GeoLocation location;
    private String description;
    private Integer minLevel;
    private Integer maxParticipants;
    private HashMap<Integer, User> participants;
    private float note;
    private HashMap<Integer, Comment> comments;

    public Event() {}

    public Event(String name, Date date, GeoLocation location, String description, Integer minLevel, Integer maxParticipants) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.minLevel = minLevel;
        this.maxParticipants = maxParticipants;
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

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {

        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int level) {
        this.minLevel = level;
    }

    public Integer getMaxParticipants() {
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

    public HashMap<Integer, Comment> getCommentaires() {
        return comments;
    }

    public void setComments(HashMap<Integer, Comment> comments) {
        this.comments = comments;
    }
}
