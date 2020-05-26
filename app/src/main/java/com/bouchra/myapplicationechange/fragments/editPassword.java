package com.bouchra.myapplicationechange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.editMyProfil;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class editPassword extends Fragment {
    private EditText actuel;
    private EditText nouveau;
    private EditText confirmation;
    private Button modifier;
    private Button annuler;
    private TextView tvoublie;
    PreferenceUtils preferenceUtils;
    private Task<Void> databaseReference;
    // kha ndir ta3 mdps oublier kinsgmha mn conect njibha hnayan

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_editpassword, container, false);
        actuel = view.findViewById(R.id.actuel);
        nouveau = view.findViewById(R.id.nouveau);
        confirmation = view.findViewById(R.id.confirmation);
        modifier = view.findViewById(R.id.modifier);
        annuler = view.findViewById(R.id.annuler);
        tvoublie = view.findViewById(R.id.tvoublie);
        preferenceUtils = new PreferenceUtils(getContext());
        modifier.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String passwordActuel = actuel.getText().toString();
                String passwordNouveau = nouveau.getText().toString();
                String passwordConfirmation = confirmation.getText().toString();
                if (!passwordActuel.isEmpty() && !passwordConfirmation.isEmpty() && !passwordNouveau.isEmpty()) {
                    if (passwordActuel.equals(preferenceUtils.getMember().getMotDePasse())) {
                        if (passwordNouveau.equals(passwordConfirmation) && passwordNouveau.length() >= 6) {
                            // aya hna nbadal
                            databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(preferenceUtils.getMember().getIdMembre()).child("motDePasse")
                                    .setValue(passwordNouveau)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // ywali l page lwla
                                                preferenceUtils.getMember().setMotDePasse(passwordNouveau);
                                            }
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {


                                        }
                                    });

                        } else {
                            confirmation.setTextColor(getResources().getColor(R.color.red));
                            confirmation.setHintTextColor(getResources().getColor(R.color.red));
                        }

                    } else {
                        //  actuel rouj
                        actuel.setTextColor(getResources().getColor(R.color.red));
                        actuel.setHintTextColor(getResources().getColor(R.color.red));

                    }

                } else {
                    if (passwordActuel.isEmpty())
                        actuel.setHintTextColor(getResources().getColor(R.color.red));
                    if (passwordNouveau.isEmpty())
                        nouveau.setHintTextColor(getResources().getColor(R.color.red));
                    if (passwordConfirmation.isEmpty())
                        confirmation.setHintTextColor(getResources().getColor(R.color.red));

                }

            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), editMyProfil.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
