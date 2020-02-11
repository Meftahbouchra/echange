package com.bouchra.myapplicationechange;

import java.util.Date;

public class Annonce {
    private String idAnnonce;
    private String  titreAnnonce;

    public Annonce() {
    }

    public Annonce(String idAnnonce, String titreAnnonce, String descriptionAnnonce, String articleEnRetour, Date dateAnnonce, String statu) {
        this.idAnnonce = idAnnonce;
        this.titreAnnonce = titreAnnonce;
        this.descriptionAnnonce = descriptionAnnonce;
        this.articleEnRetour = articleEnRetour;
        this.dateAnnonce = dateAnnonce;
        this.statu = statu;
    }

    private String  descriptionAnnonce;
    private String  articleEnRetour;

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

    public String getArticleEnRetour() {
        return articleEnRetour;
    }

    public void setArticleEnRetour(String articleEnRetour) {
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

    private Date dateAnnonce;
    private String  statu;
}
