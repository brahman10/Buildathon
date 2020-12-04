package com.example.buildathon.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buildathon.MainActivity;
import com.example.buildathon.Model.EducationModel;
import com.example.buildathon.Model.SkillModel;
import com.example.buildathon.R;
import com.example.buildathon.Utils.LoadingBar;
import com.example.buildathon.Utils.SessionManagment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.buildathon.Utils.Constants.KEY_MOBILE;
import static com.example.buildathon.Utils.Constants.USER_REF;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SkillFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SkillFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SkillFragment newInstance(String param1, String param2) {
        SkillFragment fragment = new SkillFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String skillname,rating;
    String type;
    EditText etName;
    RatingBar ratingBar;
    Button update;
    SessionManagment sessionManagment;
    DatabaseReference userref;
    LoadingBar loadingBar;
    int position;
    List<SkillModel> skill = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_skill, container, false);

        sessionManagment = new SessionManagment(getActivity());
        loadingBar = new LoadingBar(getActivity());
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(sessionManagment.getUserDetails().get(KEY_MOBILE));
        etName = v.findViewById(R.id.etSkill);
        ratingBar = v.findViewById(R.id.rateSkill);
        update = v.findViewById(R.id.update);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            type = bundle.getString("type");
            position = bundle.getInt("pos");
            skill =  (ArrayList) bundle.getSerializable("model");
            if (skill==null)
            {
                skill= new ArrayList<>();
            }

            if (type.equalsIgnoreCase("a"))
            {
                update.setText("Add");
                ((MainActivity)getActivity()).setTitle("Add Skill");
            }
            else
            {
                ((MainActivity)getActivity()).setTitle("Edit Skill");
            }
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skillname= etName.getText().toString();
                rating = String.valueOf(ratingBar.getRating());
                if (type.equalsIgnoreCase("a"))
                {
                    skill.add(new SkillModel(skillname,rating));
                    userref.child("skill").setValue(skill).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getActivity(),"Added Successfully",Toast.LENGTH_LONG).show();
                                ((MainActivity)getActivity()).loadFragment(new ProfileFragment(),null);
                            }
                        }
                    });
                }
                else
                {
                    Map<String,Object> map = new HashMap<>();
                    map.put("skill_name",skillname);
                    map.put("stars",rating);
                    userref.child("skill").child(String.valueOf(position)).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_LONG).show();
                                ((MainActivity)getActivity()).loadFragment(new ProfileFragment(),null);

                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Something Went Wrong",Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }


            }
        });

        return v;
    }
}