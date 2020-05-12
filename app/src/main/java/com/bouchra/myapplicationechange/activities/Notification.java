package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.mynotification;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {
    private mynotification mynotification;
    private ArrayList<com.bouchra.myapplicationechange.models.Notification> notifications;
    PreferenceUtils preferenceUtils;
    private RecyclerView recyclerView;
    private TextView information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView = findViewById(R.id.Notifications);
        information = findViewById(R.id.information);
        notifications = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mynotification = new mynotification(Notification.this, notifications);


        preferenceUtils = new PreferenceUtils(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Notification").child(preferenceUtils.getMember().getIdMembre());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        notifications.add(postSnapshot.getValue(com.bouchra.myapplicationechange.models.Notification.class));
                        mynotification.notifyDataSetChanged();
                        information.setVisibility(View.GONE);
                        recyclerView.setAdapter(mynotification);
                    }

                } else {
                    information.setText("Vous n'avez pas de notification ");
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
