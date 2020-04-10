package com.bouchra.myapplicationechange.fragments;

import android.content.Context;
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
    private TextInputEditText txt_email, txt_username, txt_password;
    private Button btn_register;

    private String name = "";
    private String email = "";
    private PreferenceUtils preferenceUtils;

    public Sinscrire() {
    }

    private String password = "";
    private String ID;
    // declare a instanbce
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_sinscrire, container, false);
        txt_email = view.findViewById(R.id.emailll);
        txt_username = view.findViewById(R.id.username);
        txt_password = view.findViewById(R.id.pasward);
        btn_register = view.findViewById(R.id.btnresister);
        preferenceUtils = new PreferenceUtils(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        btn_register.setOnClickListener(v -> {
            //input email,pswrd , name
            name = txt_username.getText().toString();
            email = txt_email.getText().toString();
            password = txt_password.getText().toString();
            //validate
            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                if (password.length() >= 6) {
                    registerUser();
                    //shared referecnces
                    //  PreferenceUtils.saveEmail(email, getContext());
                    //  PreferenceUtils.savePassword(password, getContext());

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
        Context context = null;
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {


                        //hdo ta3 ysjl f firebase
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
                                //shared
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



