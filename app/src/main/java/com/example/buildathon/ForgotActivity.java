package com.example.buildathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

import static com.example.buildathon.Utils.Constants.USER_REF;

public class ForgotActivity extends AppCompatActivity {

    EditText etPass, etCnfPass;
    String pass , cnfpass,mobile;
    DatabaseReference userref;
    CardView set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        etCnfPass = findViewById(R.id.et_con_pass);
        etPass = findViewById(R.id.et_new_pass);
        set = findViewById(R.id.Set);
        mobile = "+91"+getIntent().getStringExtra("mobile");
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = etPass.getText().toString();
                cnfpass =etCnfPass.getText().toString();
                if (TextUtils.isEmpty(pass))
                {
                    etPass.setError("Enter Password");
                    etPass.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(cnfpass)||(!cnfpass.equals(pass)))
                {
                    etCnfPass.setError("Confirm Password");
                    etCnfPass.requestFocus();
                    return;
                }
                else
                {
                    Map<String , Object > map = new HashMap<>();
                    map.put("password",pass);
                    userref.child(mobile).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),""+"Password Chnaged Successfully",Toast.LENGTH_LONG).show();
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