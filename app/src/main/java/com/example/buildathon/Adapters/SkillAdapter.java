package com.example.buildathon.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buildathon.Fragments.EducationFragment;
import com.example.buildathon.Fragments.SkillFragment;
import com.example.buildathon.MainActivity;
import com.example.buildathon.Model.EducationModel;
import com.example.buildathon.Model.SkillModel;
import com.example.buildathon.R;
import com.example.buildathon.Utils.SessionManagment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

import static com.example.buildathon.Utils.Constants.KEY_MOBILE;
import static com.example.buildathon.Utils.Constants.USER_REF;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MyViewHolder> {

    List<SkillModel> modelList;
    Context context;
    DatabaseReference reference;
    SessionManagment sessionManagment;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView skillname ;
        RatingBar rating;
        ImageView edit,delete;

        public MyViewHolder(View view) {
            super(view);
            skillname=view.findViewById(R.id.txtSkill);
            rating = view.findViewById(R.id.ratingBar);
            edit = view.findViewById(R.id.editEducation);
            delete = view.findViewById(R.id.deleteEducation);
        }
    }
    public SkillAdapter(List<SkillModel> modelList , Context context) {
        this.modelList = modelList;
        this.context = context;
    }
    @NonNull
    @Override
    public SkillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_skills, parent, false);

        return new SkillAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillAdapter.MyViewHolder holder, int position) {
        SkillModel model = modelList.get(position);
        holder.skillname.setText(model.getSkill_name());
        holder.rating.setRating(Float.valueOf(model.getStars()));
        sessionManagment = new SessionManagment(context);
        reference = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(sessionManagment.getUserDetails().get(KEY_MOBILE)).child("skill");
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SkillFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", (Serializable) modelList);
                bundle.putInt("pos",position);
                bundle.putString("type","e");
                ((MainActivity)context).loadFragment(fragment,bundle);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        reference.child(String.valueOf(position)).removeValue();
                        modelList.remove(position);
                        reference.setValue(modelList).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        notifyDataSetChanged();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
