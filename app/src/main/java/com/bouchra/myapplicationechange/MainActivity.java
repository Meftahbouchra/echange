package com.bouchra.myapplicationechange;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;

import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.sql.Array;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;
    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize Firebase Auth


        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        Button loginButtonFB = findViewById(R.id.loginfb); // ta3 afcebook


// hdi tni dkhla f ta3 facebook
        loginButtonFB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:en cas de succès:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:annuler");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:en cas d' erreur", error);
                    }
                });
            }
        });


    }










      /*  TextView textView=(TextView)findViewById(R.id.conect) ;
        TextView textView1=(TextView)findViewById(R.id.inscr);
        String text= "Se connecter";
        String text1= " cree un compte";
        SpannableString ss=new SpannableString(text);
        SpannableString aa=new SpannableString(text1);
        UnderlineSpan underlineSpan=new UnderlineSpan();
          ss.setSpan(underlineSpan,0,4,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        aa.setSpan(underlineSpan,5,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

    //******hadi taa google


    //hadiiiiiiiiiiiii tabla3 taa oncrete fonctiuon nichan }


    // *********************************************************** hado ta3  connection avec facebook mna hta l tli funcyion ta3 update

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "se connecter avec identifiant :en cas de succès");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:echec", task.getException());
                            Toast.makeText(MainActivity.this, "Authentification échouée .",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });
    }

    // hadi tadi l la page zawja li t3rad fiha les D
    private void updateUI() {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        finish();
    }


    public void inscrire(View view) {
        Intent go = new Intent(MainActivity.this, Sinscrire.class);
        startActivity(go);
    }

    public void connecte(View view) {
        Intent gotoo = new Intent(MainActivity.this, connect.class);
        startActivity(gotoo);
    }

}
