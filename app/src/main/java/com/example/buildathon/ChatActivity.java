package com.example.buildathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.buildathon.Model.ChatMessage;
import com.example.buildathon.Model.RoomModel;
import com.example.buildathon.Utils.SessionManagment;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.buildathon.Utils.Constants.KEY_NAME;
import static com.example.buildathon.Utils.Constants.ROOM_REF;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference chatref;
    SessionManagment sessionManagment;
    private FirebaseListAdapter<ChatMessage> adapter;
    ListView listOfMessages;
    RoomModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        sessionManagment = new SessionManagment(ChatActivity.this);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        model = (RoomModel) bundle.getSerializable("room");
        setTitle(model.getName());
        chatref = FirebaseDatabase.getInstance().getReference().child(ROOM_REF).child(model.getRoomid()).child("chat");
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);
        listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                String mes = input.getText().toString();
                if (!(mes==null || mes.equals("")))
                {
                    chatref.push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    sessionManagment.getUserDetails().get(KEY_NAME)));

                    input.setText("");
                    displayChatMessages();
                }

            }
        });
    }

    private void displayChatMessages() {

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child(ROOM_REF).child(model.getRoomid()).child("chat")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        displayChatMessages();
    }
}