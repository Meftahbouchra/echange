package com.bouchra.myapplicationechange.adapters;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bouchra.myapplicationechange.activities.DemandesOffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Notification;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


public class ConfirmeOffre extends AppCompatDialogFragment {

    private Offre offre;
    private String titreAnnonce;
    private PreferenceUtils preferenceUtils;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        offre = (Offre) this.getArguments().getSerializable("offre");
        titreAnnonce = this.getArguments().getString("nomAnnonce");
        preferenceUtils = new PreferenceUtils(getContext());
        builder.setTitle("Echanger votre object")
                .setMessage("Etes vous sure de bien vouloir accepter cette demande de troc pour echanger votre " + titreAnnonce + " avec " + offre.getNomOffre() + " ?")
                .setPositiveButton("Accepter", (dialog, which) -> {
                    addNotification(offre);
                    //selectionne un offre
                    ((DemandesOffre) getActivity()).selectedoffre(offre.getIdOffre());
                    dismiss();
                })
                .setNegativeButton("Annuler", (dialog, which) -> {
                    dismiss();
                });
        return builder.create();

    }

    private void addNotification(Offre offre) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce").child(offre.getAnnonceId());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Annonce annonce = snapshot.getValue(Annonce.class);
                Log.e("is user",annonce.getUserId());
                Log.e("is annonce",annonce.getIdAnnonce());
                DatabaseReference data = FirebaseDatabase.getInstance().getReference("Notification").child(offre.getIdUser());
                Notification notification = new Notification();
                notification.setIdsender(annonce.getUserId());
                notification.setIdreceiver(offre.getIdUser());
                //  notification.setIdsender(preferenceUtils.getMember().getIdMembre());
                notification.setDateNotification(new Date());
                notification.setContenuNotification("acceptOffre");
                notification.setIdNotification(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode());
                data.child(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode()).setValue(notification).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
