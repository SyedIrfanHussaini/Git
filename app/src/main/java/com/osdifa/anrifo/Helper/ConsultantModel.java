package com.osdifa.anrifo.Helper;

public class ConsultantModel {

    private String name, consultantImage, profession, price;

    public ConsultantModel(String name, String consultantImage, String profession, String price) {
        this.name = name;
        this.consultantImage = consultantImage;
        this.profession = profession;
        this.price = price;
    }

    public ConsultantModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConsultantImage() {
        return consultantImage;
    }

    public void setConsultantImage(String consultantImage) {
        this.consultantImage = consultantImage;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
