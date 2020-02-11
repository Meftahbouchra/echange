package com.bouchra.myapplicationechange;

public class Adresse {
    private String idAdresse;
    private String nomVille;
    private  String districts;

    public Adresse() {
    }

    public Adresse(String idAdresse, String nomVille, String districts, String quartier, int numDePorte) {
        this.idAdresse = idAdresse;
        this.nomVille = nomVille;
        this.districts = districts;
        this.quartier = quartier;
        this.numDePorte = numDePorte;
    }

    public String getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(String idAdresse) {
        this.idAdresse = idAdresse;
    }

    public String getNomVille() {
        return nomVille;
    }

    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }

    public String getDistricts() {
        return districts;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public int getNumDePorte() {
        return numDePorte;
    }

    public void setNumDePorte(int numDePorte) {
        this.numDePorte = numDePorte;
    }

    private  String quartier;
    private  int numDePorte;





}
