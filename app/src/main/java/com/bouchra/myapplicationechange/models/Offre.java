package com.bouchra.myapplicationechange.models;

import java.util.Date;

public class Offre {
    private String idOffre;
    private Date dateOffre;
    private String nomOffre;

    public Offre() {
    }

    public Offre(String idOffre, Date dateOffre, String nomOffre, String descriptionOffre) {
        this.idOffre = idOffre;
        this.dateOffre = dateOffre;
        this.nomOffre = nomOffre;
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
