package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.messagesAdapter;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private messagesAdapter userAdapter;
    private ArrayList<Membre> mUsers, onlyUsers;
    private PreferenceUtils preferenceUtils;
    private DatabaseReference l;
    private String lastmsg = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masaagelist);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        preferenceUtils = new PreferenceUtils(this);
        //user
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
//list  users li dar m3ahom chat
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Message");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                onlyUsers = new ArrayList<>();
                for (Membre user : mUsers) {
                    String hash = String.valueOf(user.getIdMembre().hashCode() + preferenceUtils.getMember().getIdMembre().hashCode());
                    if (dataSnapshot2.hasChild(hash)) {
                        //last msg

                        l = FirebaseDatabase.getInstance().getReference("Message").child(hash);//mp
                        Query query = l.orderByKey().limitToLast(1);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    lastmsg = child.child("textMessage").getValue().toString();
                                    if (!isUrl(lastmsg)) {//return false
                                        // affiche txt message
                                        userAdapter.setLastMsg(lastmsg);
                                    } else {
                                        //affiche photo message
                                        String idUser = child.child("idsender").getValue().toString();//lirsal
                                        if (idUser.equals(preferenceUtils.getMember().getIdMembre())) {
                                            // ana rsalt tof
                                            lastmsg = "Vous avez envoye une photo";
                                            userAdapter.setLastMsg(lastmsg);


                                        } else {
                                            String nomsender = child.child("nomsender").getValue().toString();
                                            //homa rsloli tof
                                            lastmsg = nomsender + "a envoye une photo";

                                            userAdapter.setLastMsg(lastmsg);

                                        }

                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        onlyUsers.add(user);
                    }
                }
                userAdapter = new messagesAdapter(MessageList.this, onlyUsers, lastmsg);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private boolean isUrl(String urlString) {
        // URL url =new URL(urlString);
        if (URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()) {
            return true;
        } else {
            return false;
        }

    }
}