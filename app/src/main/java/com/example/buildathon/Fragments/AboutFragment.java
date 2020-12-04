package com.example.buildathon.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buildathon.MainActivity;
import com.example.buildathon.R;
import com.example.buildathon.Utils.LoadingBar;
import com.example.buildathon.Utils.SessionManagment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.example.buildathon.Utils.Constants.KEY_DESCRIPTION;
import static com.example.buildathon.Utils.Constants.KEY_EMAIL;
import static com.example.buildathon.Utils.Constants.KEY_LOCATION;
import static com.example.buildathon.Utils.Constants.KEY_MOBILE;
import static com.example.buildathon.Utils.Constants.KEY_NAME;
import static com.example.buildathon.Utils.Constants.KEY_PROFILE_IMG;
import static com.example.buildathon.Utils.Constants.KEY_STATUS;
import static com.example.buildathon.Utils.Constants.KEY_TITLE;
import static com.example.buildathon.Utils.Constants.USER_REF;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EducationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EducationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EducationFragment newInstance(String param1, String param2) {
        EducationFragment fragment = new EducationFragment();
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
    String name , title , location , description;
    EditText etFname , etTitle , etLocation , etDescription;
    SessionManagment sessionManagment;
    LoadingBar loadingBar;
    Button update;
    MainActivity activity;
    DatabaseReference userref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.activity_about, container, false);
        sessionManagment = new SessionManagment(getActivity());
        ((MainActivity)getActivity()).setTitle("About");
        loadingBar = new LoadingBar(getActivity());
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(sessionManagment.getUserDetails().get(KEY_MOBILE));
        etFname = v.findViewById(R.id.fname);
        activity = new MainActivity();
        update = v.findViewById(R.id.update);
        etTitle = v.findViewById(R.id.headline);
        etDescription = v.findViewById(R.id.description);
        etLocation = v.findViewById(R.id.location);
        etFname.setText(sessionManagment.getUserDetails().get(KEY_NAME));
        etTitle.setText(sessionManagment.getUserDetails().get(KEY_TITLE));
        etDescription.setText(sessionManagment.getUserDetails().get(KEY_DESCRIPTION));
        etLocation.setText(sessionManagment.getUserDetails().get(KEY_LOCATION));
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etFname.getText().toString();
                title = etTitle.getText().toString();
                location = etLocation.getText().toString();
                description = etDescription.getText().toString();
                if (TextUtils.isEmpty(name))
                {
                    etFname.setError("Enter Name");
                    etFname.requestFocus();
                }
                else
                {
                    loadingBar.show();
                    Map<String,Object> map = new HashMap<>();
                    map.put(KEY_NAME,name);
                    map.put(KEY_TITLE,title);
                    map.put(KEY_DESCRIPTION,description);
                    map.put(KEY_LOCATION,location);
                    userref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                sessionManagment.updateProfile(name,sessionManagment.getUserDetails().get(KEY_EMAIL),sessionManagment.getUserDetails().get(KEY_PROFILE_IMG),sessionManagment.getUserDetails().get(KEY_STATUS),title,location,description);
                                Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_LONG).show();
                                ((MainActivity)getActivity()).loadFragment(new ProfileFragment(),null);

                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Something Went Wrong",Toast.LENGTH_LONG).show();
                                ((MainActivity)getActivity()).loadFragment(new ProfileFragment(),null);

                            }
                            loadingBar.dismiss();
                        }

                    });
                }
            }
        });
        return v;
    }
}