package com.bouchra.myapplicationechange;

public enum Categorie {
    VEHICULES(1, "Vèhicules"),
    TELEPHONES(2, "Tèlèphones"),
    AUTOMOBILES(3, "Automobiles"),
    PIECESDETACHEES(4, "Pièces dètachèes"),
    IMMOBILIER(5, "Immobilier"),
    VETEMENTS(6, "Vetements"),
    LIVRES(7, "Livres"),
    ELETRONIQUEETELECTROMENAGER(8, "Eletronique et electromènager"),
    ACCESSOIRESDEMODE(9, "Accessoires de mode"),
    COSMETIQUESETBEAUTE(10, "Cosmetiques et Beautè"),
    MAISONETFOUNITURES(11, "Maison et fournitures"),
    LOISIRSETDEVERTISSEMENTS(12, "Loisirs et Dèvertissements"),
    MATIEIAUXETEQUIPEMENTS(13, "Matiriaux et Equipements");

    private int point;
    private String nomCategorie;

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }


    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    private Categorie(int point, String nomCategorie) {
        this.point = point;
        this.nomCategorie = nomCategorie;
    }


}