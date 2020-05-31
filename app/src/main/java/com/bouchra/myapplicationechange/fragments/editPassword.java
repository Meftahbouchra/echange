package com.bouchra.myapplicationechange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.editMyProfil;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAut;


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
        mAut = FirebaseAuth.getInstance();
        actuel.setTextColor(getResources().getColor(R.color.black));
        nouveau.setTextColor(getResources().getColor(R.color.black));
        confirmation.setTextColor(getResources().getColor(R.color.black));
        modifier.setOnClickListener(v -> {
            String passwordActuel = actuel.getText().toString();
            String passwordNouveau = nouveau.getText().toString();
            String passwordConfirmation = confirmation.getText().toString();
            if (!passwordActuel.isEmpty() && !passwordConfirmation.isEmpty() && !passwordNouveau.isEmpty()) {
                if (passwordActuel.equals(preferenceUtils.getMember().getMotDePasse())) {
                    if (passwordNouveau.equals(passwordConfirmation) && passwordNouveau.length() >= 6) {
                        // set paswword
                        databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(preferenceUtils.getMember().getIdMembre()).child("motDePasse")
                                .setValue(passwordNouveau)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        preferenceUtils.getMember().setMotDePasse(passwordNouveau);
                                        Intent intent = new Intent(getActivity(), editMyProfil.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(e -> {


                                });

                    } else {
                        confirmation.setTextColor(getResources().getColor(R.color.red));

                    }

                } else {
                    //  actuel rouj
                    actuel.setTextColor(getResources().getColor(R.color.red));


                }

            } else {
                if (passwordActuel.isEmpty())
                    actuel.setHintTextColor(getResources().getColor(R.color.red));
                if (passwordNouveau.isEmpty())
                    nouveau.setHintTextColor(getResources().getColor(R.color.red));
                if (passwordConfirmation.isEmpty())
                    confirmation.setHintTextColor(getResources().getColor(R.color.red));

            }

        });
        annuler.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), editMyProfil.class);
            startActivity(intent);
        });
        tvoublie.setOnClickListener(v -> {
            showRecorverPasswordDialog();
        });
        return view;
    }
    private void showRecorverPasswordDialog() {
        //Alert Dilaog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Entrez votre adresse e-meil");
        LinearLayout linearLayout = new LinearLayout(getContext());
        EditText emailResorv = new EditText(getContext());
        emailResorv.setHint("Adresse e-mail :");
        emailResorv.setWidth(500);
        emailResorv.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        //emailResorv.setMaxWidth(900);
        linearLayout.addView(emailResorv);
        builder.setView(linearLayout);
        //buton recover
        builder.setPositiveButton("Envoyer", (dialog, which) -> {
            String eeemail = emailResorv.getText().toString().trim();
            beginRecover(eeemail);
        });
//buton cancel
        builder.setNegativeButton("Ignorer", (dialog, which) -> {
//dimiss dialog
            dialog.dismiss();
        });
//show dialog
        builder.create().show();
    }
    private void beginRecover(String eeemail) {

        mAut.sendPasswordResetEmail(eeemail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Un email a ètè envoyè, veuillez consulter votre boite email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Échec de l'envoi", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "Échec de l'envoi", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
