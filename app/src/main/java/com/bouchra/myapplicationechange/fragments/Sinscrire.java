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
import com.bouchra.myapplicationechange.activities.Acceuil;
import com.bouchra.myapplicationechange.models.Membre;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sinscrire extends Fragment {
    private TextInputEditText txt_email, txt_username, txt_password;
    private Button btn_register;

    private String name = "";
    private String email = "";

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
        View view =  inflater.inflate(R.layout.activity_sinscrire, container, false);
        txt_email = view.findViewById(R.id.emailll);
        txt_username =view.findViewById(R.id.username);
        txt_password = view.findViewById(R.id.pasward);
        btn_register = view.findViewById(R.id.btnresister);

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


                        //hdo ta3 ysjl f firebase

                        ID = firebaseAuth.getCurrentUser().getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(ID);
                        Membre usr = new Membre();
                        usr.setEmail(email);
                        usr.setNomMembre(name);
                        usr.setMotDePasse(password);
                        usr.setIdMembre(ID);
                        databaseReference.setValue(usr).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                startActivity(new Intent(getActivity(), Acceuil.class));
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



