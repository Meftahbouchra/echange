package com.bouchra.myapplicationechange.models;

import java.io.Serializable;
import java.util.Date;

public class Offre extends Historique implements Serializable {
    private String idOffre;
    private Date dateOffre;
    private String nomOffre;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Offre(String idOffre, Date dateOffre, String nomOffre, String image, String annonceId, String wilaya, String idUser, String statu, String commune, String descriptionOffre) {
        this.idOffre = idOffre;
        this.dateOffre = dateOffre;
        this.nomOffre = nomOffre;
        this.image = image;
        this.annonceId = annonceId;
        this.wilaya = wilaya;
        this.idUser = idUser;
        this.statu = statu;
        this.commune = commune;
        this.descriptionOffre = descriptionOffre;
    }

    // en +
    private String annonceId;
    private String wilaya;

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    private String idUser;
    private String statu;

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



    private String commune;



    public Offre() {
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
