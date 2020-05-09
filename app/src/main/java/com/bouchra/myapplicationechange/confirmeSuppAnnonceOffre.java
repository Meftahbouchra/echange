package com.bouchra.myapplicationechange;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bouchra.myapplicationechange.activities.DetailMesannonce;

public class confirmeSuppAnnonce extends AppCompatDialogFragment {
    String name = null;
    String Offre = null;
    String Annonce = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        Offre = this.getArguments().getString("Offre");
        Annonce = this.getArguments().getString("Annonce");
        if (Offre != null) {
            name = Offre;
            builder.setTitle("Supprimer")
                    .setMessage("Souhaitez-vous vraiment supprimer  cette " + name + "?")
                    .setPositiveButton("Oui", (dialog, which) -> {
// mansuprimich datat f info; ndiha l historique
                        Toast.makeText(getContext(), " cellte offre va delete", Toast.LENGTH_SHORT).show();

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
}
