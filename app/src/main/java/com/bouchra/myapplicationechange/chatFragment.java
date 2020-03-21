package com.bouchra.myapplicationechange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Message;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class chatFragment extends Fragment {
    private RecyclerView recyclerView;
    private userAdapter userAdapter;
    private ArrayList<Membre> mUsers;

    // FirebaseUser fuser;
    DatabaseReference reference,r;

    private ArrayList<String> userListe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PreferenceUtils preferenceUtils = new PreferenceUtils(getContext());
        // fuser= FirebaseAuth.getInstance().getCurrentUser();
        userListe = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Message");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListe.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                  /*  Membre utilisateur = new Membre();
                    String roomUser = snapshot.child(String.valueOf((preferenceUtils.getMember().getIdMembre().hashCode()) + (utilisateur.getIdMembre().hashCode()))).getValue().toString();*/
                 //  Message m = new Message();
                // String msg=snapshot.child(m.getIdMessage()).getValue().toString();
                    r = FirebaseDatabase.getInstance().getReference(snapshot.toString());
                    r.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Message message=dataSnapshot1.getValue(Message.class);
                                if(message.getIdsender().equals(preferenceUtils.getMember().getIdMembre())){
                                    userListe.add(message.getIdreceiver());
                                }
                                if(message.getIdreceiver().equals(preferenceUtils.getMember().getIdMembre())){
                                    userListe.add(message.getIdsender());
                                }


                            }
                            redChat();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
               // redChat();
            }


            /*  @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  userListe.clear();
                  for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                      Message message=snapshot.getValue(Message.class);
                      if(message.getIdsender().equals(preferenceUtils.getMember().getIdMembre())){
                          userListe.add(message.getIdreceiver());
                      }
                      if(message.getIdreceiver().equals(preferenceUtils.getMember().getIdMembre())){
                          userListe.add(message.getIdsender());
                      }
                  }
                  redChat();
              }
  */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void redChat() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Membre");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Membre membre = snapshot.getValue(Membre.class);
                    //display 1 user from messages
                    for (String id : userListe) {
                        if (membre.getIdMembre().equals(id)) {
                            if (mUsers.size() != 0) {
                                for (Membre m : mUsers) {
                                    if (!membre.getIdMembre().equals(m.getIdMembre())) {
                                        mUsers.add(membre);
                                    }
                                }
                            } else {
                                mUsers.add(membre);
                            }
                        }

                    }


                }
                userAdapter = new userAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}