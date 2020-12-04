package com.example.buildathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.example.buildathon.Utils.Constants.USER_REF;

public class RegisterDetailsActivity extends AppCompatActivity {

    EditText etMobile , etName , etPass , etCnfPass;
    String name , mobile , pass,cnfpass;
    CardView register;
    DatabaseReference userref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        etMobile = findViewById(R.id.etMobile);
        etName = findViewById(R.id.etName);
        etPass = findViewById(R.id.etPassword);
        etCnfPass = findViewById(R.id.etCnfPassword);
        register = findViewById(R.id.Register);
        mobile = "+91"+getIntent().getStringExtra("mobile");
        etMobile.setText(mobile);
        etMobile.setEnabled(false);
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                pass = etPass.getText().toString();
                cnfpass = etCnfPass.getText().toString();
                if (TextUtils.isEmpty(name))
                {
                    etName.setError("Enter Name");
                    etName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pass))
                {
                    etPass.setError("Enter Password");
                    etPass.requestFocus();
                    return;
                }
                if (pass.length()<6)
                {
                    etPass.setError("Password Length cant be less than 6");
                    etPass.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(cnfpass)||(!cnfpass.equals(pass)))
                {
                    etCnfPass.setError("Password should be same");
                    etCnfPass.requestFocus();
                    return;
                }
                else
                {
                    Map<String , Object > map = new HashMap<>();
                    map.put("name",name);
                    map.put("mobile",mobile);
                    map.put("password",pass);
                    map.put("id","BUI"+mobile);
                    map.put("status","0");
                    userref.child(mobile).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),""+"Registered Successfully",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
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
            }
        });
    }
}