package com.ecolem.workoutside.model;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by akawa_000 on 21/10/2015.
 */
public class User {
    private String uid;
    private String login;
    private String email;
    private String password;
    private String lastname;
    private String firstname;
    private int gender;
    private Date birthdate;
    private String city;
    private int level;
    private String picture;
    private String description;
    private Float weight;
    private int size;
    private HashMap<String, User> friends = new HashMap<>();

    public User() {
    }

    public String getUID() {
        return uid;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(Date date) {
        this.birthdate = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String pic) {
        this.picture = pic;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getWeight() {
        return this.weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public HashMap<String, User> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, User> friends) {
        this.friends = friends;
    }

    public User(String uid, String login, String email, String password, String lastname, String firstname, int gender, Date birthdate, String city, int level, String picture, String description, Float weight, int size, HashMap<String, User> friends) {
        this.uid = uid;
        this.login = login;
        this.email = email;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.city = city;
        this.level = level;
        this.picture = picture;
        this.description = description;
        this.weight = weight;
        this.size = size;
        this.friends = friends;
    }

    public User copy() {
        return new User(uid, login, email, password, lastname, firstname, gender, birthdate, city, level, picture, description, weight, size, friends);
    }

}
