package com.bouchra.myapplicationechange;

  public enum Catègorie {
                          vehicules(1,"Vèhicules"),
                          telephones(2,"Tèlèphones"),
                          automobiles(3,"Automobiles"),
                          piecesDetachees(4,"Pièces dètachèes"),
                          immobilier(5,"Immobilier"),
                          vetements(6,"Vetements"),
                          livres(7,"Livres"),
                          eletroniqueEtElectromenager(8,"Eletronique et electromènager"),
                          accessoiresDeMode(9,"Accessoires de mode"),
                          cosmetiquesEtBeaute(10,"Cosmetiques et Beautè"),
                          maisonEtFournitures(11,"Maison et fournitures"),
                          loisirsEtDevertissements(12,"Loisirs et Dèvertissements"),
                          matiriauxEtEquipements(13,"Matiriaux et Equipements");
         private int point;
         private String nomCatègorie;
   private Catègorie(int point ,String nomCatègorie )
   {
    this.point=point;
    this.nomCatègorie=nomCatègorie;
   }


}