package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Connect extends AppCompatActivity {

    private TextInputEditText txtt_email, txtt_password;
    private Button btnn_Login;
    private ImageButton imageButton;
    private TextView txt_forgtpaswrd;

    private String eemail = "";
    private String ppassword = "";
    private FirebaseAuth mAut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);


        mAut = FirebaseAuth.getInstance();
        txtt_email = findViewById(R.id.emaill);
        txtt_password = findViewById(R.id.pasward);
        btnn_Login = findViewById(R.id.btnresister);
        imageButton = findViewById(R.id.btnplac);
        txt_forgtpaswrd = findViewById(R.id.tvoublie);

        btnn_Login.setOnClickListener(v -> {
            eemail = txtt_email.getText().toString();
            ppassword = txtt_password.getText().toString();

            if (!eemail.isEmpty() && !ppassword.isEmpty()) {
                if (ppassword.length() >= 6) {
                    loginUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Le mot de passe doit comporter au mois 6 caractéres ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
            }
        });


        /*txt_forgtpaswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showReceverPasswordDialog();

            }

            private void showReceverPasswordDialog() {
                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Mot de passe oublie ?");
                // set layout linear layput;
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                // views to set in dialog
                EditText emailEt = new EditText(getApplicationContext());
                emailEt.setHint("Adresse E-mail");
                emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                linearLayout.addView(emailEt);
                linearLayout.setPadding(10, 10, 10, 10);
                emailEt.setMaxEms(10);
                builder.setView(linearLayout);
                // button recorv
                builder.setPositiveButton("Recover", (dialog, which) -> {
                    // input email
                    String emaill = emailEt.getText().toString();
                    beginRecovery(emaill);

                });
// btn cancel
                builder.setNegativeButton("ingnorer", (dialog, which) -> {
                    //dimis dialog
                    dialog.dismiss();

                });
// show dialog
                builder.create().show();

            }
        });*/
    }

  /*  private void beginRecovery(String emaill) {

        mAut.sendPasswordResetEmail(emaill).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Email sent ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), " falailed..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            // get send and prper error message
            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

        });

    }*/

    // f loginUser nshako fire base
    private void loginUser() {
        mAut.signInWithEmailAndPassword(eemail, ppassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(getApplicationContext(), Acceuil.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), " impossible de se connecter ,vèrifier vos donnèes", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void go_to_inscri(View view) {
        Intent insc = new Intent(getApplicationContext(), Sinscrire.class);
        startActivity(insc);
    }


    /*public void gotoo_insc(View view) {
        Intent insct = new Intent(getApplicationContext(), Sinscrire.class);
        startActivity(insct);
    }*/
}