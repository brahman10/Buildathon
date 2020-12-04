package com.example.buildathon.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.buildathon.LoginActivity;
import com.example.buildathon.MainActivity;
import com.example.buildathon.Model.RoomModel;
import com.example.buildathon.R;
import com.example.buildathon.Utils.SessionManagment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

/**
 By Yash Chaubey on 24/11/2020
 */
public class AddRoomFragment extends Fragment {


    public AddRoomFragment() {
        // Required empty public constructor
    }

    TextInputEditText etName , etDesc;
    CardView create;
    String name , desc , createdby , roomid , time;
    DatabaseReference roomref;
    SessionManagment sessionManagment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_room, container, false);
        ((MainActivity)getActivity()).setTitle("Create Room");
        etDesc = v.findViewById(R.id.room_desc);
        etName = v.findViewById(R.id.room_name);
        create = v.findViewById(R.id.create);
        sessionManagment = new SessionManagment(getActivity());
        roomref = FirebaseDatabase.getInstance().getReference().child(ROOM_REF);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
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
                    return;
                }
                if (TextUtils.isEmpty(desc))
                {
                    etDesc.setError("Enter Panel Description");
                    etDesc.requestFocus();
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
                        Toast.makeText(getActivity(),"Discussion Panel Created Succesfully",Toast.LENGTH_LONG).show();
                        getFragmentManager().beginTransaction().replace(R.id.container_fragment,new DiscussFragment()).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        return v;
    }
}