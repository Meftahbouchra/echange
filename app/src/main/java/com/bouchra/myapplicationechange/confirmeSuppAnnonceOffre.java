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
    String Annonce = null;
    com.bouchra.myapplicationechange.models.Offre offre;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        Annonce = this.getArguments().getString("Annonce");
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
            name = Annonce;
            builder.setTitle("Supprimer")
                    .setMessage("Souhaitez-vous vraiment supprimer cette " + name + "?")
                    .setPositiveButton("Oui", (dialog, which) -> {
// mansuprimich datat f info; ndiha l historique
                        ((DetailMesannonce) getActivity()).deleteAnnonce();

                    })
                    .setNegativeButton("Non", (dialog, which) -> {

                    });

        }

        return builder.create();
    }

    private void deleteOffre(com.bouchra.myapplicationechange.models.Offre offre) {
        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("Historique").child(offre.getIdUser()).child(offre.getIdOffre());
        Map<String, Object> OFFRE = new HashMap<>();
        OFFRE.put("NomOffre", offre.getNomOffre());
        OFFRE.put("IdOffre", offre.getIdOffre());
        OFFRE.put("IdAnnonceOffre", offre.getAnnonceId());
        OFFRE.put("CommuneOffre", offre.getCommune());
        OFFRE.put("DateOffre", offre.getDateOffre());
        OFFRE.put("DesciptionOffre", offre.getDescriptionOffre());
        OFFRE.put("IdUserOffre", offre.getIdUser());
        OFFRE.put("ImageOffre", offre.getImages());
        OFFRE.put("WilayaOffre", offre.getWilaya());
        OFFRE.put("statuOffre", "DELETEOFFRE");
        mDbRef.updateChildren(OFFRE);
        DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre());
        dOffre.removeValue();

    }
}
