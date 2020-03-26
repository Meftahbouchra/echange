package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.Connect;
import com.bouchra.myapplicationechange.fragments.Sinscrire;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;
    private TextView connect;
    private final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private PreferenceUtils preferenceUtils;
    private String facebookUserTd = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ta3 ggole
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        preferenceUtils = new PreferenceUtils(this);
        // Initialize Firebase Auth

        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        Button loginButtonFB = findViewById(R.id.loginfb); // ta3 afcebook
        Button loginButtonGoogle = findViewById(R.id.logingoogle);// ta3 google

        connect = findViewById(R.id.connecte);

        connect.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new Connect(), "connect").commit();
        });

// hdi tni dkhla f ta3 facebook
        loginButtonFB.setOnClickListener(view -> {
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
        });
// hadi ta3 gooooogle
        loginButtonGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }


    // *********************************************************** hado ta3  connection avec facebook mna hta l tli funcyion ta3 update

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // hadi taaaaaaaaaaaaaa3 goooooooogle
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


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
                            String ID = firebaseAuth.getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(ID);


                            for (UserInfo profile : user.getProviderData()) {

                                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                    facebookUserTd = profile.getUid();
                                }

                            }


                            String photoUrl = "https://graph.facebook.com/" + facebookUserTd + "/picture?height=500";
                            Membre usr = new Membre();
                            usr.setEmail(user.getEmail());
                            usr.setNomMembre(user.getDisplayName());
                            usr.setIdMembre(ID);
                            usr.setPhotoUser(photoUrl);
                            //usr.setNumTel(Integer.parseInt(user.getPhoneNumber()));
                            usr.setDateInscription(new Date());
                            databaseReference.setValue(usr).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    //shared
                                    preferenceUtils.setMember(usr);
                                    startActivity(new Intent(MainActivity.this, debut.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:echec", task.getException());
                            Toast.makeText(MainActivity.this, "Authentification échouée .",
                                    Toast.LENGTH_SHORT).show();
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
        // hada ta3 mn acti  nhal fragm
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new Sinscrire(), "inscrire").commit();
    }
/////// hadi ta3 gooooogle

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String photoesy="https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSNe5yo7hl-b5UHwropa_-4hNehtgV4w6wkFM1gw-o59SW93FNt";
                        // Sign in success, update UI with the signed-in user's informatio
                        FirebaseUser user = mAuth.getCurrentUser();
                        String ID = firebaseAuth.getCurrentUser().getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(ID);
                        Membre usr = new Membre();
                        usr.setEmail(user.getEmail());
                        usr.setNomMembre(user.getDisplayName());
                        usr.setIdMembre(ID);
                        usr.setPhotoUser(photoesy);
                        if (user.getPhoneNumber() != null) {
                            usr.setNumTel(Integer.parseInt(user.getPhoneNumber().replaceAll("[^0-9]", "")));
                        }

                        usr.setDateInscription(new Date());
                        databaseReference.setValue(usr).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                //shared
                                preferenceUtils.setMember(usr);
                                startActivity(new Intent(MainActivity.this, debut.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                            }
                        });
                        // updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(MainActivity.this, "Login Failed....", Toast.LENGTH_SHORT).show();
                        // updateUI(null);
                    }


                }).addOnFailureListener(e -> {
            //grt and show error mesage
            Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    // tgla3 l frag ila kyn ila mknch tkhroj ga3  mn had l act
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("connect") != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("connect")).commit();
        } else if (getSupportFragmentManager().findFragmentByTag("inscrire") != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("inscrire")).commit();
        } else {
            super.onBackPressed();
        }
    }
}
