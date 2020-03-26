package com.bouchra.myapplicationechange;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bouchra.myapplicationechange.activities.DetailMesannonce;

public class confirmeSuppAnnonce extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Supprimer")
                .setMessage("Souhaitez-vous vraiment supprimer cette annonce?")
                .setPositiveButton("Oui", (dialog, which) -> {

                    ((DetailMesannonce) getActivity()).deleteAnnonce();

                })
                .setNegativeButton("Non", (dialog, which) -> {

                });
         return builder.create();

    }
}
