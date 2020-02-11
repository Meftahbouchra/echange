package com.bouchra.myapplicationechange;

  public enum Catègorie {
                          Vèhicules(1,"Vèhicules"),
                          Tèlèphones(2,"Tèlèphones"),
                          Automobiles(3,"Automobiles"),
                          Pièces_dètachèes(4,"Pièces dètachèes"),
                          Immobilier(5,"Immobilier"),
                          Vetements(6,"Vetements"),
                          Livres(7,"Livres"),
                          Eletronique_et_electromènager(8,"Eletronique et electromènager"),
                          Accessoires_de_mode(9,"Accessoires de mode"),
                          Cosmetiques_et_Beautè(10,"Cosmetiques et Beautè"),
                          Maison_et_fournitures(11,"Maison et fournitures"),
                          Loisirs_et_Dèvertissements(12,"Loisirs et Dèvertissements"),
                          Matiriaux_et_Equipements(13,"Matiriaux et Equipements");
         private int point;
         private String nomCatègorie;
   private Catègorie(int point ,String nomCatègorie )
   {
    this.point=point;
    this.nomCatègorie=nomCatègorie;
   }


}