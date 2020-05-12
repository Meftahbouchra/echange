package com.bouchra.myapplicationechange.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Annonce extends Historique implements Serializable {// bch nkdo nrsloha ù blasa lblsa whdkhra
    private String idAnnonce;
    private String titreAnnonce;
    private String descriptionAnnonce;
    private ArrayList<String> articleEnRetour = new ArrayList<>();
    private Date dateAnnonce;
    private String statu;
    private String wilaya;
    private String commune;
    private String IdOffreSelected;

    public String getIdOffreSelected() {
        return IdOffreSelected;
    }

    public void setIdOffreSelected(String idOffreSelected) {
        IdOffreSelected = idOffreSelected;
    }

    public Annonce(String idAnnonce, String titreAnnonce, String descriptionAnnonce, ArrayList<String> articleEnRetour, Date dateAnnonce, String statu, String wilaya, String commune, String idOffreSelected, String userId, ArrayList<String> images) {
        this.idAnnonce = idAnnonce;
        this.titreAnnonce = titreAnnonce;
        this.descriptionAnnonce = descriptionAnnonce;
        this.articleEnRetour = articleEnRetour;
        this.dateAnnonce = dateAnnonce;
        this.statu = statu;
        this.wilaya = wilaya;
        this.commune = commune;
        this.IdOffreSelected = idOffreSelected;
        this.userId = userId;
        this.images = images;
    }

    private String userId;
    private ArrayList<String> images = new ArrayList<>();

    public Annonce() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public String getTitreAnnonce() {
        return titreAnnonce;
    }

    public void setTitreAnnonce(String titreAnnonce) {
        this.titreAnnonce = titreAnnonce;
    }

    public String getDescriptionAnnonce() {
        return descriptionAnnonce;
    }

    public void setDescriptionAnnonce(String descriptionAnnonce) {
        this.descriptionAnnonce = descriptionAnnonce;
    }

    public ArrayList<String> getArticleEnRetour() {
        return articleEnRetour;
    }

    public void setArticleEnRetour(ArrayList<String> articleEnRetour) {
        this.articleEnRetour = articleEnRetour;
    }

    public Date getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(Date dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }


}
