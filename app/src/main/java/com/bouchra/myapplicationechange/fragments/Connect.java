package com.bouchra.myapplicationechange.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.debut;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
        txt_forgtpaswrd.setOnClickListener(v -> {
            showRecorverPasswordDialog();
        });

       imageButton.setOnClickListener(v -> {
           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Sinscrire(),"insr").commit();
       });


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

    private void showRecorverPasswordDialog() {
        //Alert Dilaog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Entrez votre adresse e-meil");
        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(getContext());
        //views to set in dalog
        EditText emailResorv = new EditText(getContext());
        emailResorv.setHint("Adresse e-mail :");
        emailResorv.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailResorv.setMaxWidth(900);
      //  emailResorv.setMaxEms(16);
        linearLayout.addView(emailResorv);
       // linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        //buton recover
        builder.setPositiveButton("Envoyer", (dialog, which) -> {
// input email
            String eeemail=emailResorv.getText().toString().trim();
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
               if(task.isSuccessful()){
                   Toast.makeText(getContext(), "Un email a ètè envoyè, veuillez consulter votre boite email", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(getContext(), "Échec de l'envoi", Toast.LENGTH_SHORT).show();
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
// get and show proper error message
               // Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Échec de l'envoi", Toast.LENGTH_SHORT).show();
            }
        });

    }


    // f loginUser nshako fire base
    private void loginUser() {
        mAut.signInWithEmailAndPassword(eemail, ppassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(getActivity(), debut.class));
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), " impossible de se connecter ,vèrifier vos donnèes", Toast.LENGTH_SHORT).show();
            }

        });

    }





}