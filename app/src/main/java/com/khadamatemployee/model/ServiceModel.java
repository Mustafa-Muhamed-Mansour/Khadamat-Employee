package com.khadamatemployee.model;

public class ServiceModel
{

    private String image, title;

    public ServiceModel()
    {
    }

    public ServiceModel(String image, String title)
    {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
