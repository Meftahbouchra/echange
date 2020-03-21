package com.bouchra.myapplicationechange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.adapters.MessageAdapter;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Message;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;
    // FirebaseUser fuser;
    DatabaseReference reference, r, l;
    ImageButton btn_send;
    EditText txt_send;
    MessageAdapter messageAdapter;
    ArrayList<Message> mchat;
    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.tollbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        btn_send = findViewById(R.id.btn_send);
        txt_send = findViewById(R.id.txt_send);

        intent = getIntent();
        String userid = intent.getStringExtra("user");
        // fuser = FirebaseAuth.getInstance().getCurrentUser();
        PreferenceUtils preferenceUtils = new PreferenceUtils(this);
        reference = FirebaseDatabase.getInstance().getReference("Membre").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Membre membre = dataSnapshot.getValue(Membre.class);
                username.setText(membre.getNomMembre());
                Glide.with(MessageActivity.this).load(membre.getPhotoUser()).into(profile_image);

                mchat = new ArrayList<>();

                l = FirebaseDatabase.getInstance().getReference("Message").child(String.valueOf((preferenceUtils.getMember().getIdMembre().hashCode()) + (userid.hashCode())));

                l.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mchat.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Message message = snapshot.getValue(Message.class);// getvalue  de lbrary gson yroh yhws yjbd direct child w ydirh f object message ila li jbdth mchikima classe yhbs

                            if (message.getIdreceiver().equals(preferenceUtils.getMember().getIdMembre()) && message.getIdsender().equals(userid) ||
                                    message.getIdreceiver().equals(userid) && message.getIdsender().equals(preferenceUtils.getMember().getIdMembre())) {
                                mchat.add(message);

                            }
                            messageAdapter = new MessageAdapter(MessageActivity.this, mchat, membre.getPhotoUser());
                            recyclerView.setAdapter(messageAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_send.setOnClickListener(v -> {
            String msg = txt_send.getText().toString();
            if (!msg.equals("")) {

                r = FirebaseDatabase.getInstance().getReference("Message").child(String.valueOf((preferenceUtils.getMember().getIdMembre().hashCode()) + (userid.hashCode())));

                Message chat = new Message();
                chat.setTextMessage(msg);
                chat.setIdreceiver(userid);
                chat.setIdsender(preferenceUtils.getMember().getIdMembre());
                chat.setNomsender(preferenceUtils.getMember().getNomMembre());
                chat.setDateMessage(new Date());
                chat.setIdMessage(String.valueOf(chat.getDateMessage().getTime()));
                r.child(String.valueOf(chat.getDateMessage().getTime())).setValue(chat).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Votre message a été soumise avec succès ", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(this, "Votre message a été soumise avec succès ", Toast.LENGTH_LONG).show();
                    }

                });
            } else {
                Toast.makeText(this, "you can't send empty message", Toast.LENGTH_SHORT).show();
            }
            txt_send.setText("");

        });
    }


}
