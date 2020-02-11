package com.bouchra.myapplicationechange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Sinscrire extends AppCompatActivity {
    private TextInputEditText txt_email, txt_username, txt_password;
    private Button btn_register;

    private String name = "";
    private String email = "";
    private String password = "";
    private String ID;
    // declare a instanbce
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinscrire);


        txt_email = findViewById(R.id.emailll);
        txt_username = findViewById(R.id.username);
        txt_password = findViewById(R.id.pasward);
        btn_register = findViewById(R.id.btnresister);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input email,pswrd , name
                name = txt_username.getText().toString();
                email = txt_email.getText().toString();
                password = txt_password.getText().toString();
                //validate
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if (password.length() >= 6) {
                        registerUser();
                    } else {
                        Toast.makeText(Sinscrire.this, "Le mot de passe doit comporter au mois 6 caractéres ", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(Sinscrire.this, "Vous devez remplir les champs", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void registerUser() {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            //hdo ta3 ysjl f firebase

                            ID = firebaseAuth.getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(ID);
                            Membre usr = new Membre();
                            usr.setEmail_Memebre(email);
                            usr.setNom_Membre(name);
                            usr.setMot_de_passe__Membre(password);
                            usr.setId_Membre(ID);
                            databaseReference.setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()) {
                                        startActivity(new Intent(Sinscrire.this, Acceuil.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Sinscrire.this, "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                            // hna sayina darna kimla ta3 facebook bach yjo f fire base fi rahba khrabna mlfog
                         /*  Membre usr = new Membre();
                            usr.setEmail_Memebre(email);
                            usr.setNom_Membre(name);
                            usr.setMot_de_passe__Membre(password);
                            String ID = firebaseAuth.getCurrentUser().getUid();
                            // f class membre nkado ngla3o id psq fire base whdah ya3tina identifiant

                            databaseReference.child("Users").child(ID).setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()) {
                                        startActivity(new Intent(Sinscrire.this, Acceuil.class));
                                        finish();}

                                    else {
                                        Toast.makeText(Sinscrire.this, "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();}
                                }
                            });*/

                        } else {
                            Toast.makeText(Sinscrire.this, "Cet utilisateur ne peut pas etre enregistrè", Toast.LENGTH_LONG).show();
                        }

                    }


                });


    }
}



