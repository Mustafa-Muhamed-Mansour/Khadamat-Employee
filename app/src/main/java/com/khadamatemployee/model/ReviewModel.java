package com.khadamatemployee.model;

public class ReviewModel
{
    private String id;
    private String randomKey;
    private String image;
    private String name;
    private String location;
    private String opinion;
    private int numberLikes;
    private int numberDislikes;


    public ReviewModel()
    {
    }

//    public ReviewModel(String id, String randomKey, String image, String name, String location, String opinion, int numberLikes)
//    {
//        this.id = id;
//        this.randomKey = randomKey;
//        this.image = image;
//        this.name = name;
//        this.location = location;
//        this.opinion = opinion;
//        this.numberLikes = numberLikes;
//    }

    public ReviewModel(String id, String randomKey, String image, String name, String location, String opinion, int numberLikes, int numberDislikes)
    {
        this.id = id;
        this.randomKey = randomKey;
        this.image = image;
        this.name = name;
        this.location = location;
        this.opinion = opinion;
        this.numberLikes = numberLikes;
        this.numberDislikes = numberDislikes;
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

    public String getLocation() {
        return location;
    }

    public String getOpinion() {
        return opinion;
    }

    public int getNumberLikes() {
        return numberLikes;
    }

    public int getNumberDislikes() {
        return numberDislikes;
    }
}
