package com.example.buildathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

import static com.example.buildathon.Utils.Constants.KEY_DESCRIPTION;
import static com.example.buildathon.Utils.Constants.KEY_EMAIL;
import static com.example.buildathon.Utils.Constants.KEY_LOCATION;
import static com.example.buildathon.Utils.Constants.KEY_MOBILE;
import static com.example.buildathon.Utils.Constants.KEY_NAME;
import static com.example.buildathon.Utils.Constants.KEY_PROFILE_IMG;
import static com.example.buildathon.Utils.Constants.KEY_TITLE;
import static com.example.buildathon.Utils.Constants.USER_REF;

public class RegisterBasicDetails extends AppCompatActivity {

    Spinner year,branch;
    List<String> yearlist;
    RadioButton rbStudent , rbAlumni , rbFaculty;
    RadioGroup rgType;
    String type,stYear , stBranch , stDesign ;
    EditText etDesign;
    LinearLayout llYear,llDesign;
    CardView add;
    DatabaseReference userref;
    SessionManagment sessionManagment;
    LoadingBar loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_basic_details);
        year = findViewById(R.id.spiner_year);
        branch = findViewById(R.id.spiner_branch);
        rbStudent = findViewById(R.id.rb_student);
        rbAlumni = findViewById(R.id.rb_alumni);
        rbFaculty = findViewById(R.id.rb_faculty);
        rgType = findViewById(R.id.rg_type);
        llDesign = findViewById(R.id.lldesign);
        etDesign = findViewById(R.id.etDesignation);
        llYear = findViewById(R.id.llyear);
        add = findViewById(R.id.add);
        loadingBar = new LoadingBar(this);
        sessionManagment = new SessionManagment(this);
        rbStudent.setChecked(true);
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF);
        yearlist = new ArrayList<>();
        for(int i = 1993 ; i<=2050;i++)
        {
            yearlist.add(String.valueOf(i));
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_layout, yearlist );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        year.setAdapter(spinnerArrayAdapter);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.branch, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        branch.setAdapter(adapter);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=(RadioButton)findViewById(checkedId);
                type = rb.getText().toString();
                if (checkedId==R.id.rb_faculty)
                {
                    llYear.setVisibility(View.GONE);
                    llDesign.setVisibility(View.VISIBLE);
                }
                else
                {
                    llYear.setVisibility(View.VISIBLE);
                    llDesign.setVisibility(View.GONE);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stDesign = etDesign.getText().toString();
                stYear = year.getSelectedItem().toString();
                stBranch = branch.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(),stBranch+stYear+stDesign+type,Toast.LENGTH_LONG).show();
                if(TextUtils.isEmpty(stDesign)&&type.equalsIgnoreCase("faculty"))
                {
                    etDesign.setError("Enter Designation");
                    etDesign.requestFocus();
                    return;
                }
                Map<String,Object> map = new HashMap<>();
                map.put("type",type);
                map.put("designation",stDesign);
                map.put("passyear",stYear);
                map.put("branch",stBranch);
                map.put("status","1");
                userref.child(sessionManagment.getUserDetails().get(KEY_MOBILE)).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            sessionManagment.updateProfile(sessionManagment.getUserDetails().get(KEY_NAME),sessionManagment.getUserDetails().get(KEY_EMAIL),sessionManagment.getUserDetails().get(KEY_PROFILE_IMG),"1",sessionManagment.getUserDetails().get(KEY_TITLE),sessionManagment.getUserDetails().get(KEY_LOCATION),sessionManagment.getUserDetails().get(KEY_DESCRIPTION));
                            Toast.makeText(getApplicationContext(),""+"Added Successfully",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
}