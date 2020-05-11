package com.bouchra.myapplicationechange;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bouchra.myapplicationechange.activities.DetailMesannonce;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class confirmeSuppAnnonceOffre extends AppCompatDialogFragment {
    String name = null;
    String Offre = null;
    com.bouchra.myapplicationechange.models.Annonce Annonce;
    com.bouchra.myapplicationechange.models.Offre offre;
    String nn = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        Annonce = (com.bouchra.myapplicationechange.models.Annonce) this.getArguments().getSerializable("Annonce");
        offre = (com.bouchra.myapplicationechange.models.Offre) this.getArguments().getSerializable("offreObject");
        if (offre != null) {
            name = offre.getNomOffre();
            builder.setTitle("Supprimer")
                    .setMessage("Souhaitez-vous vraiment supprimer  cette " + name + "?")
                    .setPositiveButton("Oui", (dialog, which) -> {
// mansuprimich datat f info; ndiha l historique
                        deleteOffre(offre);
                        // Toast.makeText(getContext(), " cellte offre va delete", Toast.LENGTH_SHORT).show();

                    })
                    .setNegativeButton("Non", (dialog, which) -> {

                    });
            //return builder.create();
        }
        if (Annonce != null) {
            name = Annonce.getTitreAnnonce();
            builder.setTitle("Supprimer")
                    .setMessage("Souhaitez-vous vraiment supprimer cette " + name + "?")
                    .setPositiveButton("Oui", (dialog, which) -> {
// mansuprimich datat f info; ndiha l historique
                        String shox;


                       ((DetailMesannonce) getActivity()).deleteAnnonce();
                    })
                    .setNegativeButton("Non", (dialog, which) -> {

                    });

        }

        return builder.create();
    }

    private void view(String nomCategorie) {
        //  Toast.makeText(getContext(), "" + nomCategorie, Toast.LENGTH_SHORT).show();
    }

    private void deleteOffre(com.bouchra.myapplicationechange.models.Offre offre) {
        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("Historique").child(offre.getIdUser()).child(offre.getIdOffre());
        Map<String, Object> OFFRE = new HashMap<>();
        OFFRE.put("nomOffre", offre.getNomOffre());
        OFFRE.put("idOffre", offre.getIdOffre());
        OFFRE.put("annonceId", offre.getAnnonceId());
        OFFRE.put("commune", offre.getCommune());
        OFFRE.put("dateOffre", offre.getDateOffre());
        OFFRE.put("descriptionOffre", offre.getDescriptionOffre());
        OFFRE.put("idUser", offre.getIdUser());
        OFFRE.put("images", offre.getImages());
        OFFRE.put("wilaya", offre.getWilaya());
        OFFRE.put("statu", "DELETEOFFRE");
        mDbRef.updateChildren(OFFRE);
        DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre());
        dOffre.removeValue();

    }
}
