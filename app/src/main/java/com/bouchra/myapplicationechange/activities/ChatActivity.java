package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {



        private DatabaseReference Base;
        private static final String TAG = "Chat";
        private FirebaseAuth mAuth;
        private ListView listView;
        private MessageListAdapter Adapter;
        private List<Message> messageList;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);
            mAuth = FirebaseAuth.getInstance();
           // Base = FirebaseDatabase.getInstance().getReference();
            listView = (ListView) findViewById(R.id.listmessage);
            final Button button = findViewById(R.id.Sent);
            final EditText txt = findViewById(R.id.TextSent);
            messageList = new ArrayList<>();
            final Intent intent = new Intent(this,ChatActivity.class);
            Adapter = new MessageListAdapter(getApplicationContext(),messageList,mAuth.getCurrentUser().getDisplayName());
            listView.setAdapter(Adapter);

            registerForContextMenu(listView);

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Code here executes on main thread after user presses button

                    Message message = new Message(mAuth.getCurrentUser().getDisplayName().toString(), txt.getText().toString().trim());
                    if ( message.getMsg().length() != 0) {
                        Base.push();
                        /*Base.child(message.getTime().toString()).setValue(message.getMsg());*/
                        Base.child(message.getTime().toString()).setValue(message);

                        //   Adapter.Add(message);
                        // Adapter.notifyDataSetChanged();
                        txt.setText("");

                    }
                }
            });




         /*   Base.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Message message = new Message(dataSnapshot.child("user").getValue().toString(),dataSnapshot.child("msg").getValue().toString());
                    message.setTime(dataSnapshot.child("time").getValue(Long.class));
                    Adapter.Add(message);
                    Adapter.notifyDataSetChanged();

                    if(!message.getUser().equals(mAuth.getCurrentUser().getDisplayName())) {
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(ChatActivity.this);
                        PendingIntent pendingIntent = PendingIntent.getActivity(ChatActivity.this, 0, intent, 0);

                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setAutoCancel(true);
                        mBuilder.setSmallIcon(R.drawable.messagenotif);
                        mBuilder.setContentTitle(message.getUser());
                        mBuilder.setContentText(message.getMsg());

                        NotificationManager mNotificationManager =

                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        mNotificationManager.notify(001, mBuilder.build());
                    }
                }


                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                    Message message = new Message(dataSnapshot.child("user").getValue().toString(), dataSnapshot.child("msg").getValue().toString());
                    message.setTime(dataSnapshot.child("time").getValue(Long.class));
                    Adapter.Add(message);
                    Adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });*/




        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.add(0, v.getId(), 0, "Delete");
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            if (item.getTitle() == "Delete")
            {
                Message message = (Message) Adapter.getItem(index);
                Base.child(message.getTime().toString()).removeValue();
                Adapter.Delete(index);
                Adapter.notifyDataSetChanged();

            }
            return true;
        }
    }