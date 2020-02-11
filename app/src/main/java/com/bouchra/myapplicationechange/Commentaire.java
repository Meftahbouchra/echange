package com.bouchra.myapplicationechange;

import java.util.Date;

public class Commentaire {
    private String idCommentaire;
    private Date dateCommentaire;
    private String contenuCommentaire;
    private float repos;// nbr *

    public Commentaire() {
    }

    public Commentaire(String idCommentaire, Date dateCommentaire, String contenuCommentaire, float repos) {
        this.idCommentaire = idCommentaire;
        this.dateCommentaire = dateCommentaire;
        this.contenuCommentaire = contenuCommentaire;
        this.repos = repos;
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

    public int Calcul_Score(){// score des commentaie
        float score=0;
      return (int) score;
    }
}

