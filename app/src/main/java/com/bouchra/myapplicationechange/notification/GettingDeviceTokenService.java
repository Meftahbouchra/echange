package com.bouchra.myapplicationechange.notification;

import androidx.annotation.NonNull;

import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;


public class GettingDeviceTokenService extends FirebaseMessagingService {// FirebaseInstanceIdService is deprecated by the FirebaseMessagingService

    PreferenceUtils preferenceUtils;
    // nous l'utilisons  ce jeton/token dans la console Firebase pour définir le bon périphérique de destination.

    @Override
    public void onNewToken(@NonNull String s) {// this Override function return  the token ...
        super.onNewToken(s);
        preferenceUtils = new PreferenceUtils(this);// user
  // nous obtiendrons le jeton de l'appareil.
        s = FirebaseInstanceId.getInstance().getToken();
        if (preferenceUtils.getMember() != null) {
            updateToken(s);
        }


    }

    private void updateToken(String tokenRefresh) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tokenRefresh);
        ref.child(preferenceUtils.getMember().getIdMembre()).setValue(token);

    }
}
