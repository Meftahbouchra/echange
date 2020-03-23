package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.adapters.messagesAdapter;
import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private messagesAdapter userAdapter;
    private ArrayList<Membre> mUsers, onlyUsers;
    private PreferenceUtils preferenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masaagelist);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        preferenceUtils = new PreferenceUtils(this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Membre");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Membre membre = snapshot.getValue(Membre.class);
                    mUsers.add(membre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Message");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                onlyUsers = new ArrayList<>();
                for (Membre user : mUsers) {
                    String hash = String.valueOf(user.getIdMembre().hashCode() + preferenceUtils.getMember().getIdMembre().hashCode());
                    if (dataSnapshot2.hasChild(hash)) onlyUsers.add(user);
                }
                userAdapter = new messagesAdapter(MessageList.this, onlyUsers);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}