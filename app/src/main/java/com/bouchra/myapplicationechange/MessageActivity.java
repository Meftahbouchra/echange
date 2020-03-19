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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

//////////////////////////////////////////////////////////khasni nzid nfwt l fire base nom sender
public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference, r;


    ImageButton btn_send;
    EditText txt_send;
    MessageAdapter messageAdapter;
    ArrayList<Message> mchat;
    RecyclerView recyclerView;//recycle_view

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
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Membre").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Membre membre = dataSnapshot.getValue(Membre.class);
                username.setText(membre.getNomMembre());
                //  Glide.with(MessageActivity.this).load(membre.getImg).into(profile_image); m3ndich img ta3 user
               readMessage(fuser.getUid(), userid);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_send.setOnClickListener(v -> {
            String msg = txt_send.getText().toString();
            if (!msg.equals("")) {
                // sendMessage(fuser.getUid(), userid, msg);
                //DatabaseReference r = FirebaseDatabase.getInstance().getReference("Message").child(String.valueOf(fuser.getUid().hashCode())+userid.hashCode());
                r = FirebaseDatabase.getInstance().getReference("Message").child(String.valueOf((fuser.getUid().hashCode()) + (userid.hashCode())));
                Message chat = new Message();
                chat.setTextMessage(msg);
                chat.setIdreceiver(userid);
                chat.setIdsender(fuser.getUid());
                chat.setDateMessage(new Date());
                chat.setIdMessage(String.valueOf(chat.getIdreceiver().hashCode()) + chat.getIdsender().hashCode());
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

    private void readMessage(String myid, String userid) {
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Message");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    //if (message.getIdreceiver().equals(myid) && message.getIdsender().equals(userid) ||
                            if(message.getIdreceiver().equals(userid) && message.getIdsender().equals(myid)) {
                        mchat.add(message);

                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
   /* private void sendMessage(String sender, String receiver,String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Message").child(String.valueOf(fuser.getUid().hashCode())+userid.hashCode());
        Message msg=new Message();
       msg.setTextMessage(message);
       msg.setIdreceiver(receiver);
       msg.setIdsender(sender);
       msg.setDateMessage(new Date());
       msg.setIdMessage(String.valueOf(msg.getIdreceiver().hashCode())+msg.getIdsender().hashCode());
        reference.setValue(msg).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Votre message a été soumise auec succès ", Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(this, "Votre message a été soumise auec succès ", Toast.LENGTH_LONG).show();
            }

        });
    }
}*/
