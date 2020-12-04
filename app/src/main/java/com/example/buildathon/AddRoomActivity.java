package com.example.buildathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.buildathon.Fragments.DiscussFragment;
import com.example.buildathon.Model.RoomModel;
import com.example.buildathon.Utils.LoadingBar;
import com.example.buildathon.Utils.SessionManagment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.buildathon.Utils.Constants.KEY_NAME;
import static com.example.buildathon.Utils.Constants.ROOM_REF;

public class AddRoomActivity extends AppCompatActivity {

    TextInputEditText etName , etDesc;
    CardView create;
    String name , desc , createdby , roomid , time;
    DatabaseReference roomref;
    SessionManagment sessionManagment;
    LoadingBar loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_room);

        etDesc = findViewById(R.id.room_desc);
        etName = findViewById(R.id.room_name);
        create = findViewById(R.id.create);
        loadingBar = new LoadingBar(AddRoomActivity.this);
        sessionManagment = new SessionManagment(AddRoomActivity.this);
        roomref = FirebaseDatabase.getInstance().getReference().child(ROOM_REF);
        setTitle("Add Discussion Panel");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                loadingBar.show();
                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-YYYY hh:mm:ss");
                Date date = cal.getTime();
                time=sdf.format(date);
                name = etName.getText().toString();
                desc = etDesc.getText().toString();
                createdby = sessionManagment.getUserDetails().get(KEY_NAME);
                if (TextUtils.isEmpty(name))
                {
                    etName.setError("Enter Panel Name");
                    etName.requestFocus();
                    loadingBar.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(desc))
                {
                    etDesc.setError("Enter Panel Description");
                    etDesc.requestFocus();
                    loadingBar.dismiss();
                    return;
                }
                roomid = "RM"+time;
                RoomModel model = new RoomModel(name,desc,createdby,roomid);
                roomref.child(roomid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        roomref.child(roomid).child("info").setValue(model);
                        etDesc.setText("");
                        etName.setText("");
                        Toast.makeText(getApplicationContext(),"Discussion Panel Created Succesfully",Toast.LENGTH_LONG).show();
                        onBackPressed();
                        loadingBar.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loadingBar.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}