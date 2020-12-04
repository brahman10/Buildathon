package com.example.buildathon.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buildathon.Adapters.RoomAdapter;
import com.example.buildathon.AddRoomActivity;
import com.example.buildathon.MainActivity;
import com.example.buildathon.Model.RoomModel;
import com.example.buildathon.R;
import com.example.buildathon.Utils.LoadingBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.buildathon.Utils.Constants.ROOM_INFO;
import static com.example.buildathon.Utils.Constants.ROOM_REF;

public class DiscussFragment extends Fragment {

    public DiscussFragment() {
        // Required empty public constructor
    }

    FloatingActionButton fab;
    RecyclerView recyclerView;
    DatabaseReference roomref;
    List<RoomModel> roomlist;
    LoadingBar loadingBar;
    List<String> roomids;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_discuss, container, false);
        roomref = FirebaseDatabase.getInstance().getReference().child(ROOM_REF);
        ((MainActivity)getActivity()).setTitle("Discuss Panel");
        recyclerView = v.findViewById(R.id.rec_rooms);
        roomids = new ArrayList<>();
        loadingBar = new LoadingBar(getActivity());
        loadingBar.show();
        roomlist = new ArrayList<>();
        fab = v.findViewById(R.id.add_rooms);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), AddRoomActivity.class));
               // getFragmentManager().beginTransaction().replace(R.id.container_fragment,new AddRoomFragment()).addToBackStack("addrooms").commit();
            }
        });


        roomref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("onDataChange: ",dataSnapshot.toString() );
                roomlist.clear();
                RoomModel model=null;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String datakey = dsp.getKey();
                    roomids.add(datakey);
                    for (DataSnapshot snapshot:dsp.getChildren())
                    {
                        if (snapshot.getKey().equals(ROOM_INFO))
                        {
                            model = snapshot.getValue(RoomModel.class);
                        }
                    }
                    roomlist.add(model);

                }
                RoomAdapter adapter = new RoomAdapter(roomlist,getActivity());
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loadingBar.dismiss();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                loadingBar.dismiss();
            }
        });

        return v;
    }

}