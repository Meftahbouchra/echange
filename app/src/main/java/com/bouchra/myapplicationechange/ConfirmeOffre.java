package com.bouchra.myapplicationechange;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bouchra.myapplicationechange.activities.DemandesOffre;


public class ConfirmeOffre extends AppCompatDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String titreOffre = this.getArguments().getString("nomOffre");
        String titreAnnonce = this.getArguments().getString("nomAnnonce");
        String offreId= this.getArguments().getString("idOffre");
        builder.setTitle("Echanger votre object")
                .setMessage("Etes vous sure de bien vouloir accepter cette demande de troc pour echanger votre " + titreAnnonce + " avec " + titreOffre + " ?")
                .setPositiveButton("Accepter", (dialog, which) -> {


                    ((DemandesOffre)getActivity()).selectedoffre(offreId);
                })
                .setNegativeButton("Annuler", (dialog, which) -> {


                });
        return builder.create();

    }
}

