package com.bouchra.myapplicationechange;

import java.util.Date;

public class Membre {
    private String idMembre;
    private String nomMembre;
    private int numTel;
    private char email;
    private String adresseMembre;
    private char motDePasse;

    public Membre() {
    }

    private Date dateInscription;

    public Membre(String idMembre, String nomMembre, int numTel, char email, String adresseMembre, char motDePasse, Date dateInscription, String lienCompteFb, String lienCompteGoogle) {
        this.idMembre = idMembre;
        this.nomMembre = nomMembre;
        this.numTel = numTel;
        this.email = email;
        this.adresseMembre = adresseMembre;
        this.motDePasse = motDePasse;
        this.dateInscription = dateInscription;
        this.lienCompteFb = lienCompteFb;
        this.lienCompteGoogle = lienCompteGoogle;
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

    public char getEmail() {
        return email;
    }

    public void setEmail(char email) {
        this.email = email;
    }

    public String getAdresseMembre() {
        return adresseMembre;
    }

    public void setAdresseMembre(String adresseMembre) {
        this.adresseMembre = adresseMembre;
    }

    public char getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(char motDePasse) {
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

    private String lienCompteFb;
    private String lienCompteGoogle;

}
