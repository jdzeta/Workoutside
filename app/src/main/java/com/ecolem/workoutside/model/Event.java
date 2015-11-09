package com.ecolem.workoutside.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by akawa_000 on 23/10/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private String uid;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private double latitude;
    private double longitude;
    private String description;
    private int minLevel;
    private int maxParticipants;
    private HashMap<String, User> participants;
    private float note;
    private HashMap<String, Comment> comments;
    private User creator;

    public Event() {
    }

    public Event(String uid, String name, Date dateStart, Date dateEnd, double latitude, double longitude, String description, int minLevel, int maxParticipants, User creator) {
        this.uid = uid;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.minLevel = minLevel;
        this.maxParticipants = maxParticipants;
        this.creator = creator;
    }

    public String getUID() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
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

    public HashMap<String, User> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<String, User> participants) {
        this.participants = participants;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public HashMap<String, Comment> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, Comment> comments) {
        this.comments = comments;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    public boolean hasSameDate(Event event) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(this.getDateStart());

        Calendar c2 = Calendar.getInstance();
        c2.setTime(event.getDateStart());

        return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);

    }


}
