package com.bouchra.myapplicationechange.models;

import java.util.Date;

public class Commentaire {
    private String idCommentaire;
    private Date dateCommentaire;
    private String contenuCommentaire;
    private float repos;// nbr * yad y3amar b nos ykad ykon 3.4
    private String IdSender;
    private String IDResiver;

    public Commentaire(String idCommentaire, Date dateCommentaire, String contenuCommentaire, float repos, String idSender, String IDResiver) {
        this.idCommentaire = idCommentaire;
        this.dateCommentaire = dateCommentaire;
        this.contenuCommentaire = contenuCommentaire;
        this.repos = repos;
        this.IdSender = idSender;
        this.IDResiver = IDResiver;
    }

    public String getIdSender() {
        return IdSender;
    }

    public void setIdSender(String idSender) {
        IdSender = idSender;
    }

    public String getIDResiver() {
        return IDResiver;
    }

    public void setIDResiver(String IDResiver) {
        this.IDResiver = IDResiver;
    }

    public Commentaire() {
    }


    public String getIdCommentaire() {
        return idCommentaire;
    }

    public void setIdCommentaire(String idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public Date getDateCommentaire() {
        return dateCommentaire;
    }

    public void setDateCommentaire(Date dateCommentaire) {
        this.dateCommentaire = dateCommentaire;
    }

    public String getContenuCommentaire() {
        return contenuCommentaire;
    }

    public void setContenuCommentaire(String contenuCommentaire) {
        this.contenuCommentaire = contenuCommentaire;
    }

    public float getRepos() {
        return repos;
    }

    public void setRepos(float repos) {
        this.repos = repos;
    }

    public int Calcul_Score() {// score des commentaie
        float score = 0;
        return (int) score;
    }
}

