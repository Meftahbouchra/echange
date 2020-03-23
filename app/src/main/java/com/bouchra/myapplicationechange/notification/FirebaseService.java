package com.bouchra.myapplicationechange.notification;

import androidx.annotation.NonNull;

import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseService extends FirebaseMessagingService {// FirebaseInstanceIdService is deprecated by the FirebaseMessagingService

    PreferenceUtils preferenceUtils;

    @Override
    public void onNewToken(@NonNull String s) {// this Override function return  the token ...
        super.onNewToken(s);
        preferenceUtils = new PreferenceUtils(this);// user
        // tokenRefresh = s
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
