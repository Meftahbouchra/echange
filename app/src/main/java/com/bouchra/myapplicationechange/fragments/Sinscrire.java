package com.bouchra.myapplicationechange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.debut;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class Sinscrire extends Fragment {
    private TextInputEditText textEmail;
    private TextInputEditText textUsername;
    private TextInputEditText textPassword;
    private Button btnRegister;
    private PreferenceUtils preferenceUtils;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String name = "";
    private String email = "";
    private String password = "";
    private String ID;

    public Sinscrire() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_sinscrire, container, false);
        textEmail = view.findViewById(R.id.emailll);
        textUsername = view.findViewById(R.id.username);
        textPassword = view.findViewById(R.id.pasward);
        btnRegister = view.findViewById(R.id.btnresister);
        preferenceUtils = new PreferenceUtils(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        btnRegister.setOnClickListener(v -> {
            name = textUsername.getText().toString();
            email = textEmail.getText().toString();
            password = textPassword.getText().toString();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                if (password.length() >= 6) {
                    registerUser();
                } else {
                    Toast.makeText(getContext(), "Le mot de passe doit comporter au mois 6 caractéres ", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getContext(), "Vous devez remplir les champs", Toast.LENGTH_LONG).show();
            }

        });
        return view;
    }


    private void registerUser() {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String imageUser = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSNe5yo7hl-b5UHwropa_-4hNehtgV4w6wkFM1gw-o59SW93FNt";
                        ID = firebaseAuth.getCurrentUser().getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(ID);
                        Membre usr = new Membre();
                        usr.setEmail(email);
                        usr.setNomMembre(name);
                        usr.setMotDePasse(password);
                        usr.setIdMembre(ID);
                        usr.setPhotoUser(imageUser);
                        usr.setDateInscription(new Date());
                        databaseReference.setValue(usr).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                preferenceUtils.setMember(usr);
                                startActivity(new Intent(getActivity(), debut.class));
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                            }
                        });


                    } else {
                        Toast.makeText(getContext(), "Cet utilisateur ne peut pas etre enregistrè", Toast.LENGTH_LONG).show();
                    }

                });


    }
}



