package com.bouchra.myapplicationechange.fragments;
//************************************** hada model t3 annonce li nrmlm, ykon f model w rah na9as
public class PosteModelAnnonce {
    String title;
    String desciption;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public PosteModelAnnonce(String title, String desciption, String img) {
        this.title = title;
        this.desciption = desciption;
        this.img = img;
    }

    String img;

    public PosteModelAnnonce() {
    }
}
