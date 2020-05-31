package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private messagesAdapter userAdapter;
    private ArrayList<Membre> mUsers, onlyUsers;
    private PreferenceUtils preferenceUtils;
    private DatabaseReference l;
    private String lastmsg = "";
    private TextView information;
    private TextView back;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masaagelist);

        recyclerView = findViewById(R.id.recycle_view);
        information = findViewById(R.id.information);
        back = findViewById(R.id.retour);
        preferenceUtils = new PreferenceUtils(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //get all users
        reference = FirebaseDatabase.getInstance().getReference("Membre");
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
                        l = FirebaseDatabase.getInstance().getReference("Message").child(hash);
                        // get last child
                        Query query = l.orderByKey().limitToLast(1);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    lastmsg = child.child("textMessage").getValue().toString();
                                    String idUser = child.child("idsender").getValue().toString();
                                    String datee = child.child("idMessage").getValue().toString();
// drna id machi date psq rana dyrinha f fire base in id w psq kon drna date njiboha khlt njobo arbre ta3 les d, wla kon jbnha attribut par attribut
                                    Date date = new Date(Long.parseLong(datee));
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
                                    String str = simpleDateFormat.format(date);
                                    Log.e("date here", str);


                                    if (!isUrl(lastmsg)) {//return false
                                        // affiche txt message
                                        if (idUser.equals(preferenceUtils.getMember().getIdMembre())) {
                                            userAdapter.setLastMsg("Vous : " + lastmsg, str);
                                        } else {
                                            userAdapter.setLastMsg(lastmsg, str);
                                        }

                                    } else {
                                        //affiche photo message
                                        if (idUser.equals(preferenceUtils.getMember().getIdMembre())) {
                                            // ana rsalt tof
                                            lastmsg = "Vous avez envoye une photo";
                                            userAdapter.setLastMsg(lastmsg, str);


                                        } else {
                                            String nomsender = child.child("nomsender").getValue().toString();
                                            //homa rsloli tof
                                            lastmsg = nomsender + "a envoye une photo";

                                            userAdapter.setLastMsg(lastmsg, str);

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
                if (onlyUsers.size() == 0) {
                    information.setText("Vous n'avez pas de conversation ");
                    recyclerView.setVisibility(View.GONE);

                } else {
                    information.setVisibility(View.GONE);
                    userAdapter = new messagesAdapter(MessageList.this, onlyUsers, lastmsg);
                    recyclerView.setAdapter(userAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //String is url!
    private boolean isUrl(String urlString) {
        // URL url =new URL(urlString);
        if (URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()) {
            return true;
        } else {
            return false;
        }

    }
}