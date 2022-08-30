package com.example.openshelves.accountDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.openshelves.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth firebaseAuth;
    ImageButton btnBack;
    Button btnSubmit;
    EditText etEmail;
    TextInputLayout tilEmail;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        btnBack=findViewById(R.id.btnBack);
        btnSubmit=findViewById(R.id.submitBtn);
        etEmail=findViewById(R.id.etEmail);
        tilEmail=findViewById(R.id.tilEmail);

        //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        //init/setup progress dialog
        progressDialog=new ProgressDialog(this, R.style.ProgressDialogStyle);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //handle click submit, begin recovery
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String email="";
    private void validateData() {
        email=etEmail.getText().toString().trim();
        if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email Address", Toast.LENGTH_SHORT).show();
        }
        else{
            recoverPassword();
        }
    }

    private void recoverPassword() {
        //show progress
        progressDialog.setMessage("Sending the instructions to"+email);
        progressDialog.show();

        //begin sending recovery
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //sent
                progressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this,"Instructions succesfully sent", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this,"Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}