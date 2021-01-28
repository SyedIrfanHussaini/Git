package com.osdifa.anrifo.Helper;

public class ChatModel {

    private String UID;
    private String name;
    private String image;
    private String consultantImage;
    private String status;

    public ChatModel(String UID, String name, String image, String consultantImage, String status) {
        this.UID = UID;
        this.name = name;
        this.image = image;
        this.consultantImage = consultantImage;
        this.status = status;
    }

    public ChatModel() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getConsultantImage() {
        return consultantImage;
    }

    public void setConsultantImage(String consultantImage) {
        this.consultantImage = consultantImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
