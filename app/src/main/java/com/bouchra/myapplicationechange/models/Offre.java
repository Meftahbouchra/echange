package com.bouchra.myapplicationechange.models;

import java.util.ArrayList;
import java.util.Date;

public class Offre {
    private String idOffre;
    private Date dateOffre;
    private String nomOffre;
    // en +
    private String annonceId;
    private String wilaya;
    private String idUser;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(String annonceId) {
        this.annonceId = annonceId;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    private String commune;
    private ArrayList<String> images = new ArrayList<>();


    public Offre() {
    }


    public Offre(String idOffre, Date dateOffre, String nomOffre, String annonceId, String wilaya, String idUser, String commune, ArrayList<String> images, String descriptionOffre) {
        this.idOffre = idOffre;
        this.dateOffre = dateOffre;
        this.nomOffre = nomOffre;
        this.annonceId = annonceId;
        this.wilaya = wilaya;
        this.idUser = idUser;
        this.commune = commune;
        this.images = images;
        this.descriptionOffre = descriptionOffre;
    }

    public String getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(String idOffre) {
        this.idOffre = idOffre;
    }

    public Date getDateOffre() {
        return dateOffre;
    }

    public void setDateOffre(Date dateOffre) {
        this.dateOffre = dateOffre;
    }

    public String getNomOffre() {
        return nomOffre;
    }

    public void setNomOffre(String nomOffre) {
        this.nomOffre = nomOffre;
    }

    public String getDescriptionOffre() {
        return descriptionOffre;
    }

    public void setDescriptionOffre(String descriptionOffre) {
        this.descriptionOffre = descriptionOffre;
    }

    private String descriptionOffre;
}
