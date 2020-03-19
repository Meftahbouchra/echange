package com.bouchra.myapplicationechange.models;

import java.io.Serializable;
import java.util.Date;

public class Membre implements Serializable {
    private String idMembre;
    private String nomMembre;
    private int numTel;
    private String email;
    private String adresseMembre;
    private String motDePasse;
    private String lienCompteFb;
    private String lienCompteGoogle;
    private Date dateInscription;
    private String photoUser;


    public Membre() {
    }


    public Membre(String idMembre, String nomMembre, int numTel, String email, String adresseMembre, String motDePasse, String lienCompteFb, String lienCompteGoogle, Date dateInscription, String photoUser) {
        this.idMembre = idMembre;
        this.nomMembre = nomMembre;
        this.numTel = numTel;
        this.email = email;
        this.adresseMembre = adresseMembre;
        this.motDePasse = motDePasse;
        this.lienCompteFb = lienCompteFb;
        this.lienCompteGoogle = lienCompteGoogle;
        this.dateInscription = dateInscription;
        this.photoUser = photoUser;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
    }

    public String getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(String idMembre) {
        this.idMembre = idMembre;
    }

    public String getNomMembre() {
        return nomMembre;
    }

    public void setNomMembre(String nomMembre) {
        this.nomMembre = nomMembre;
    }

    public int getNumTel() {
        return numTel;
    }

    public void setNumTel(int numTel) {
        this.numTel = numTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresseMembre() {
        return adresseMembre;
    }

    public void setAdresseMembre(String adresseMembre) {
        this.adresseMembre = adresseMembre;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getLienCompteFb() {
        return lienCompteFb;
    }

    public void setLienCompteFb(String lienCompteFb) {
        this.lienCompteFb = lienCompteFb;
    }

    public String getLienCompteGoogle() {
        return lienCompteGoogle;
    }

    public void setLienCompteGoogle(String lienCompteGoogle) {
        this.lienCompteGoogle = lienCompteGoogle;
    }


}
