package com.example.buildathon.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buildathon.MainActivity;
import com.example.buildathon.Model.EducationModel;
import com.example.buildathon.Model.UserModel;
import com.example.buildathon.R;
import com.example.buildathon.Utils.LoadingBar;
import com.example.buildathon.Utils.SessionManagment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.buildathon.Utils.Constants.KEY_MOBILE;
import static com.example.buildathon.Utils.Constants.USER_REF;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EducationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EducationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EducationFragment() {
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
    String start_date , end_date , date,school,degree,grade,description,field;
    TextView tv_start_date , tv_end_date;
    int y,m,d;
    String type;
    EditText etSchool , etGrades , etDegree, etField , etDesc;
    Button update;
    SessionManagment sessionManagment;
    DatabaseReference userref;
    LoadingBar loadingBar;
    int position;
    List<EducationModel> education = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_education, container, false);
        sessionManagment = new SessionManagment(getActivity());
        loadingBar = new LoadingBar(getActivity());
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(sessionManagment.getUserDetails().get(KEY_MOBILE));
        tv_end_date = v.findViewById(R.id.tv_end_date);
        tv_start_date = v.findViewById(R.id.tv_start_date);
        etSchool = v.findViewById(R.id.etSchool);
        etDegree = v.findViewById(R.id.etDegree);
        etField = v.findViewById(R.id.etField);
        etDesc = v.findViewById(R.id.description);
        etGrades = v.findViewById(R.id.etGrade);
        update = v.findViewById(R.id.update);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            type = bundle.getString("type");
            position = bundle.getInt("pos");
            education =  (ArrayList) bundle.getSerializable("model");
            if (education==null)
            {
                education= new ArrayList<>();
            }

            if (type.equalsIgnoreCase("a"))
            {
                update.setText("Add");
                ((MainActivity)getActivity()).setTitle("Add Education Details");
            }
            else
            {
                ((MainActivity)getActivity()).setTitle("Edit Educational Details");
            }
        }
        tv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        tv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                school= etSchool.getText().toString();
                degree = etDegree.getText().toString();
                field = etField.getText().toString();
                grade = etGrades.getText().toString();
                description = etDesc.getText().toString();
                if (type.equalsIgnoreCase("a"))
                {
                    education.add(new EducationModel(school,degree,field,start_date,end_date,grade,description));
                    userref.child("education").setValue(education).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    map.put("degree",degree);
                    map.put("description",description);
                    map.put("grade",grade);
                    map.put("field",field);
                    map.put("school",school);
                    map.put("start_date",start_date);
                    map.put("end_date",end_date);
                    userref.child("education").child(String.valueOf(position)).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getActivity(),"Added Successfully",Toast.LENGTH_LONG).show();
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
    private void showDatePicker(View v) {
        Calendar calendar=Calendar.getInstance();
        y=calendar.get(Calendar.YEAR);
        m=calendar.get(Calendar.MONTH);
        d=calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int mth, int dayOfMonth) {
                int month=mth+1;
                if(dayOfMonth<10)
                {
                    if(month<10)
                    {
                        date="0"+dayOfMonth+"-0"+month+"-"+year;
                    }
                    else
                    {
                        date="0"+dayOfMonth+"-"+month+"-"+year;
                    }
                }
                else
                {
                    if(month<10)
                    {
                        date=dayOfMonth+"-0"+month+"-"+year;
                    }
                    else
                    {
                        date=dayOfMonth+"-"+month+"-"+year;
                    }
                }
                if (v.getId()==R.id.tv_start_date)
                {
                    tv_start_date.setText(date);
                    start_date = date;
                }
                else
                {
                    tv_end_date.setText(date);
                    end_date = date;
                }
            }
        },y,m,d);

        datePickerDialog.getDatePicker().setMinDate(0);
        datePickerDialog.show();


    }
}