package com.bouchra.myapplicationechange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.Acceuil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Connect extends Fragment {

    private TextInputEditText txtt_email, txtt_password;
    private Button btnn_Login;
    private ImageButton imageButton;
    private TextView txt_forgtpaswrd , sins;

    private String eemail = "";
    private String ppassword = "";
    private FirebaseAuth mAut;


    public Connect() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_connect, container, false);

        mAut = FirebaseAuth.getInstance();
        txtt_email = view.findViewById(R.id.emaill);
        txtt_password = view.findViewById(R.id.pasward);
        btnn_Login = view.findViewById(R.id.btnresister);
        imageButton = view.findViewById(R.id.btnplac);
        txt_forgtpaswrd = view.findViewById(R.id.tvoublie);
        sins=view.findViewById(R.id.go_insc);

        sins.setOnClickListener(v -> {
           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Sinscrire(),"inscrire").commit();

        });
        btnn_Login.setOnClickListener(v -> {
            eemail = txtt_email.getText().toString();
            ppassword = txtt_password.getText().toString();

            if (!eemail.isEmpty() && !ppassword.isEmpty()) {
                if (ppassword.length() >= 6) {
                    loginUser();
                } else {
                    Toast.makeText(getContext(), "Le mot de passe doit comporter au mois 6 caractéres ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }



    // f loginUser nshako fire base
    private void loginUser() {
        mAut.signInWithEmailAndPassword(eemail, ppassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(getContext(), Acceuil.class));
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), " impossible de se connecter ,vèrifier vos donnèes", Toast.LENGTH_SHORT).show();
            }

        });

    }





}