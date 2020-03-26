package com.bouchra.myapplicationechange;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class ConfirmeOffre extends AppCompatDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String titreOffre = this.getArguments().getString("nomOffre");//
        String titreAnnonce = this.getArguments().getString("nomAnnonce");
        builder.setTitle("Echanger votre object")
                .setMessage("Etes vous sure de bien vouloir accepter cette demande de troc pour echanger votre"+titreAnnonce + "avec" + titreOffre+"?")
                .setPositiveButton("Accepter", (dialog, which) -> {
                    //((DetailMesannonce) getActivity()).deleteAnnonce();



                })
                .setNegativeButton("Annuler", (dialog, which) -> {


                });
        return builder.create();

    }
}

