package com.bouchra.myapplicationechange.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bouchra.myapplicationechange.activities.MessageActivity;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
//Nous recevrons un message dans ce service.
/*

Vous pouvez également contrôler le titre, l'icone.....
 */
public class FirebaseMessaging extends FirebaseMessagingService {//service pour gerer l'envoi de msg
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {// nous affichons  la notification contenant le message envoyé.
        super.onMessageReceived(remoteMessage);

        //get current user from shared preferences
        PreferenceUtils preferenceUtils = new PreferenceUtils(this);
        String savedCurrentUser = preferenceUtils.getMember().getIdMembre();
        String sent = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null && sent.equals(fuser.getUid())) {
            if (!savedCurrentUser.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOAndAboveNotification(remoteMessage);

                } else {
                    sendNormalNotifcation(remoteMessage);
                }
            }

        }
    }

    private void sendNormalNotifcation(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defsoundUri)
                .setContentIntent(pIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int j = 0;
        if (i > 0) {
            j = i;
        }
        notificationManager.notify(j, builder.build());


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOAndAboveNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoAndAboveNotification notification1 = new OreoAndAboveNotification(this);
        Notification.Builder builder = notification1.getONotifications(title, body, pIntent, defsoundUri, icon);


        int j = 0;
        if (i > 0) {
            j = i;
        }
        notification1.getManager().notify(j, builder.build());


    }
}
//Ajouter des services dans AndroidManifest