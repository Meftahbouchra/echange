package com.bouchra.myapplicationechange.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bouchra.myapplicationechange.activities.DetailMesannonce;
import com.bouchra.myapplicationechange.activities.debut;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class confirmeSuppAnnonceOffre extends AppCompatDialogFragment {
    String name = null;
    com.bouchra.myapplicationechange.models.Annonce Annonce;
    com.bouchra.myapplicationechange.models.Offre offre;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        Annonce = (com.bouchra.myapplicationechange.models.Annonce) this.getArguments().getSerializable("Annonce");
        offre = (com.bouchra.myapplicationechange.models.Offre) this.getArguments().getSerializable("offreObject");
        if (offre != null) {
            name = offre.getNomOffre();
            builder.setTitle("Supprimer")
                    .setMessage("Souhaitez-vous vraiment supprimer  cette " + name + " ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // replace a table  historique
                        deleteOffre(offre);
                        dismiss();


                    })
                    .setNegativeButton("Non", (dialog, which) -> {
                        dismiss();
                    });

        }
        if (Annonce != null) {
            name = Annonce.getTitreAnnonce();
            builder.setTitle("Supprimer")
                    .setMessage("Souhaitez-vous vraiment supprimer cette " + name + "?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        ((DetailMesannonce) getActivity()).deleteAnnonce();
                        dismiss();
                    })
                    .setNegativeButton("Non", (dialog, which) -> {
                        dismiss();
                    });

        }

        return builder.create();
    }


    private void deleteOffre(com.bouchra.myapplicationechange.models.Offre offre) {
        // add to table historique
        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("Historique").child(offre.getIdUser()).child(offre.getIdOffre());
        Map<String, Object> OFFRE = new HashMap<>();
        OFFRE.put("nomOffre", offre.getNomOffre());
        OFFRE.put("idOffre", offre.getIdOffre());
        OFFRE.put("annonceId", offre.getAnnonceId());
        OFFRE.put("commune", offre.getCommune());
        OFFRE.put("dateOffre", offre.getDateOffre());
        OFFRE.put("descriptionOffre", offre.getDescriptionOffre());
        OFFRE.put("idUser", offre.getIdUser());
        OFFRE.put("image", offre.getImage());
        OFFRE.put("wilaya", offre.getWilaya());
        OFFRE.put("statu", "DELETEOFFRE");
        mDbRef.updateChildren(OFFRE);
        // delete from tabvle offre
        DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre());
        dOffre.removeValue();
        Intent intent = new Intent(getContext(), debut.class);
        startActivity(intent);
        getActivity().finish();
    }
}
