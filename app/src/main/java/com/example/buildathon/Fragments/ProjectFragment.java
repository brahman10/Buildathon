package com.example.buildathon.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buildathon.MainActivity;
import com.example.buildathon.Model.AccomplishmentModel;
import com.example.buildathon.Model.EducationModel;
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


public class ProjectFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
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
    String start_date , end_date , date,projectname,url,description,creator;
    TextView tv_start_date , tv_end_date;
    int y,m,d;
    String type;
    EditText etName , etUrl , etCreator, etDesc;
    Button update;
    SessionManagment sessionManagment;
    DatabaseReference userref;
    LoadingBar loadingBar;
    int position;
    List<AccomplishmentModel> project = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_project, container, false);
        sessionManagment = new SessionManagment(getActivity());
        loadingBar = new LoadingBar(getActivity());
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(sessionManagment.getUserDetails().get(KEY_MOBILE));
        tv_end_date = v.findViewById(R.id.tv_end_date);
        tv_start_date = v.findViewById(R.id.tv_start_date);
        etName = v.findViewById(R.id.etProject);
        etCreator = v.findViewById(R.id.etCreator);
        etUrl = v.findViewById(R.id.etURL);
        etDesc = v.findViewById(R.id.etDesc);
        update = v.findViewById(R.id.update);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            type = bundle.getString("type");
            position = bundle.getInt("pos");
            project =  (ArrayList) bundle.getSerializable("model");
            if (project==null)
            {
                project= new ArrayList<>();
            }

            if (type.equalsIgnoreCase("a"))
            {
                update.setText("Add");
                ((MainActivity)getActivity()).setTitle("Add Project Details");
            }
            else
            {
                ((MainActivity)getActivity()).setTitle("Edit Project Details");
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
                projectname= etName.getText().toString();
                creator = etCreator.getText().toString();
                url = etUrl.getText().toString();
                description = etDesc.getText().toString();
                if (type.equalsIgnoreCase("a"))
                {
                    project.add(new AccomplishmentModel(projectname,String.valueOf(start_date),String.valueOf(end_date),description,creator,url));
                    userref.child("project").setValue(project).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    map.put("name",projectname);
                    map.put("description",description);
                    map.put("creator",creator);
                    map.put("url",url);
                    map.put("start_date",start_date);
                    map.put("end_date",end_date);
                    userref.child("project").child(String.valueOf(position)).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getActivity(),"updated Successfully",Toast.LENGTH_LONG).show();
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