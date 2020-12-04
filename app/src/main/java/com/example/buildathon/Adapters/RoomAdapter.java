package com.example.buildathon.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buildathon.ChatActivity;
import com.example.buildathon.Model.RoomModel;
import com.example.buildathon.R;

import java.util.List;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    private List<RoomModel> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView room;
        TextView name,desc,created;

        public MyViewHolder(View view) {
            super(view);
            room = view.findViewById(R.id.room);
            name = view.findViewById(R.id.room_name);
            desc = view.findViewById(R.id.room_desc);
            created = view.findViewById(R.id.room_created);
        }
    }

    public RoomAdapter(List<RoomModel> modelList , Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public RoomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_room, parent, false);

        return new RoomAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RoomAdapter.MyViewHolder holder, int position) {
        RoomModel mList = modelList.get(position);
        holder.name.setText(mList.getName());
        holder.desc.setText(mList.getDescription());
        holder.created.setText(mList.getCreatedby());
        holder.room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("room",mList);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}


