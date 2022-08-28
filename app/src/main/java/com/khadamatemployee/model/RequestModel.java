package com.khadamatemployee.model;

import java.io.Serializable;

public class RequestModel implements Serializable
{
    private String id;
    private String randomKey;
    private String image;
    private String name;
    private String locatiom;
    private String phone;
    private String email;
    private String day;
    private String time;

    public RequestModel()
    {
    }

    public RequestModel(String id, String randomKey, String image, String name, String locatiom, String phone, String email, String day, String time)
    {
        this.id = id;
        this.randomKey = randomKey;
        this.image = image;
        this.name = name;
        this.locatiom = locatiom;
        this.phone = phone;
        this.email = email;
        this.day = day;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getRandomKey() {
        return randomKey;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocatiom() {
        return locatiom;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

}
