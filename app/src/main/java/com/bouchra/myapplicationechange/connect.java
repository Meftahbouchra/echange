package com.bouchra.myapplicationechange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class connect extends AppCompatActivity {

    private  TextInputEditText txtt_email, txtt_password;
    private Button btnn_Login;
     private  ImageButton imageButton;
     private  TextView txt_forgtpaswrd;

   private String eemail="";
   private  String ppassword="";
   private  FirebaseAuth mAut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);




        mAut=FirebaseAuth.getInstance();
        txtt_email=findViewById(R.id.emaill);
        txtt_password=findViewById(R.id.pasward);
        btnn_Login=findViewById(R.id.btnresister);
        imageButton = findViewById(R.id.btnplac);
        txt_forgtpaswrd =findViewById(R.id.tvoublie);

  btnn_Login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        eemail=txtt_email.getText().toString();
        ppassword=txtt_password.getText().toString();

        if(!eemail.isEmpty()&& !ppassword.isEmpty()){
        if (ppassword.length() >= 6) {    loginUser();    }
        else { Toast.makeText(connect.this, "Le mot de passe doit comporter au mois 6 caractéres ", Toast.LENGTH_LONG).show(); }
        }
        else{ Toast.makeText(connect.this,"Vous devez remplir les champs",Toast.LENGTH_SHORT).show(); }
    }
});




  txt_forgtpaswrd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

          showReceverPasswordDialog();

      }

      private void showReceverPasswordDialog() {
          //alert dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(connect.this);
        builder.setTitle("Mot de passe oublie ?");
          // set layout linear layput;
          LinearLayout linearLayout = new LinearLayout(connect.this);
          // views to set in dialog
          EditText emailEt = new EditText(connect.this);
          emailEt.setHint("Adresse E-mail");
          emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
          linearLayout.addView(emailEt);
          linearLayout.setPadding(10,10,10,10);
          emailEt.setMaxEms(10);
          builder.setView(linearLayout);
          // button recorv
          builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  // input email
                   String  emaill  = emailEt.getText().toString();
                   beginRecovery(emaill);

              }
          });
// btn cancel
          builder.setNegativeButton("ingnorer", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  //dimis dialog
                  dialog.dismiss();

              }
          });
// show dialog
          builder.create().show();

      }
  });
    }

    private void beginRecovery(String emaill) {

        mAut.sendPasswordResetEmail(emaill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if ( task.isSuccessful()){
                    Toast.makeText(connect.this, "Email sent ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(connect.this, " falailed..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // get send and prper error message
                Toast.makeText(connect.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    // f loginUser nshako fire base
    private void loginUser() {
        mAut.signInWithEmailAndPassword(eemail,ppassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   startActivity(new Intent(connect.this,Acceuil.class));
                   finish();}
               else { Toast.makeText(connect.this," impossible de se connecter ,vèrifier vos donnèes",Toast.LENGTH_SHORT).show();}

            }
        });

    }

    public void go_to_inscri(View view) {
        Intent insc= new Intent(connect.this,Sinscrire.class);
        startActivity(insc);
    }


    public void gotoo_insc(View view) {
        Intent insct= new Intent(connect.this,Sinscrire.class);
        startActivity(insct);
    }
}