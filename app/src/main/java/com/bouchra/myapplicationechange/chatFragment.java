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
    private ArrayList<Membre> mUsers,onlyUsers;
    private PreferenceUtils preferenceUtils;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferenceUtils = new PreferenceUtils(getContext());
     /*   userAdapter = new userAdapter(getContext(), mUsers);
        recyclerView.setAdapter(userAdapter);*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Membre");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mUsers=new ArrayList<>();
                    Membre membre = snapshot.getValue(Membre.class);
                    mUsers.add(membre);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Message");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            onlyUsers = new ArrayList<>();
                            for(Membre user : mUsers){
                                String hash = String.valueOf(user.getIdMembre().hashCode() + preferenceUtils.getMember().getIdMembre().hashCode());
                                if(dataSnapshot2.hasChild(hash))onlyUsers.add(user);
                            }
                            userAdapter = new userAdapter(getContext(), onlyUsers);
                            recyclerView.setAdapter(userAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return view;
    }
}