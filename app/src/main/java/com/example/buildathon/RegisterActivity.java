package com.example.buildathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.buildathon.Utils.LoadingBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static com.example.buildathon.Utils.Constants.USER_REF;

public class RegisterActivity extends AppCompatActivity {

    CardView submit,register;
    EditText etMobile,etOtp;
    String otp;
    String numberrr;
    LinearLayout llMobile ,llOtp;
    private String verificationId;
    private static final String KEY_VERIFICATION_ID = "key_verification_id";
    private FirebaseAuth mAuth;
    DatabaseReference userref;
    String mVerificationId ;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    LoadingBar loadingBar;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        type = getIntent().getStringExtra("type");
        submit = findViewById(R.id.submit);
        userref= FirebaseDatabase.getInstance().getReference().child(USER_REF);
        mAuth = FirebaseAuth.getInstance();
        llMobile = findViewById(R.id.ll_number);
        register=findViewById(R.id.register);
        etOtp = findViewById(R.id.etOtp);
        llOtp = findViewById(R.id.ll_otp);
        loadingBar = new LoadingBar(RegisterActivity.this);
        etMobile = findViewById(R.id.etMobile);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = etMobile.getText().toString();
                numberrr = mobile;
                if (mobile.trim().equals("")||mobile.equals(null))
                {
                    etMobile.setError("Enter Mobile Number");
                    etMobile.requestFocus();
                    Toast.makeText(getApplicationContext(),mobile,Toast.LENGTH_LONG).show();
                }
                else
                {
                    loadingBar.show();
                    userref.child("+91"+mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists() && snapshot.getChildrenCount()>0&&type.equalsIgnoreCase("r"))
                            {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(),"Already Registered",Toast.LENGTH_LONG).show();
                            }
                            else if(!snapshot.exists() && snapshot.getChildrenCount()==0&&type.equalsIgnoreCase("f"))
                            {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(),"Not Registered",Toast.LENGTH_LONG).show();
                            }
                            else
                            {

                                String phonenumber = "+91"+mobile;
                                sendVerificationCode(phonenumber);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

                        }
                    });


                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = etOtp.getText().toString();
                verifyCode(otp);
            }
        });
        // put this code after starting phone number verification
        if (verificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }


    }
    private void sendVerificationCode(String number){

        try {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(number)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
        loadingBar.dismiss();
        llMobile.setVisibility(View.GONE);
        llOtp.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"OTP Sent Successfully",Toast.LENGTH_LONG).show();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            Log.e("onVerification" ,""+ credential);

            signInWithCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w( "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.e("Sent", "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            // ...
        }
    };
    private void verifyCode(String code){

        try {
            loadingBar.show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithCredential(credential);
        }
        catch (Exception e)
        {
            Log.e("Errror",e.toString());
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.e("YAHA AA GYA","HAAA");
                            if (type.equalsIgnoreCase("r"))
                            {
                                Intent intent = new Intent(RegisterActivity.this, RegisterDetailsActivity.class);
                                intent.putExtra("mobile",numberrr);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(RegisterActivity.this, ForgotActivity.class);
                                intent.putExtra("mobile",numberrr);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }


                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        loadingBar.dismiss();
                    }

                });
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID,verificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }
}