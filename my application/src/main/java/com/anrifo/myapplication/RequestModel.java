package com.anrifo.myapplication;

public class RequestModel {

    String name, profession, image, UID;

    public RequestModel(String name, String profession, String image, String UID) {
        this.name = name;
        this.profession = profession;
        this.image = image;
        this.UID = UID;
    }

    public RequestModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
