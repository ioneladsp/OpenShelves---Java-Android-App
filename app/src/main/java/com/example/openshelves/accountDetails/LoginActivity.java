package com.example.openshelves.accountDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openshelves.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPass;
    TextInputLayout tilEmail, tilPass;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        tilEmail=findViewById(R.id.tilEmail);
        tilPass=findViewById(R.id.tilPass);

        etEmail=findViewById(R.id.etEmail);
        etPass=findViewById(R.id.etPass);

        TextView tvSignUp=findViewById(R.id.tvSignUp);
        Button btnLogin=findViewById(R.id.btnlogin);
        TextView tvForgot=findViewById(R.id.tvForgotPass);

        firebaseAuth=FirebaseAuth.getInstance();

        EmailFocusListener();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilPass.setHelperText(null);
                int ok=validation();
                //daca a completat campurile (data is validated)
                if(ok==1){
                    loginUser();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Complete the Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser() {
        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //login succes (user-ul a fost gasit in baza de date)
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"No account with these credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EmailFocusListener() {
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    tilEmail.setHelperText(validEmail());
                }
            }
        });
    }

    private String validEmail(){
        String email=etEmail.getText().toString();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Invalid Email Address";
        }
        return null;
    }

    private int validation() {
        //before trying to log in an account, we do data validation
        int validEmail=0;
        int validPass=0;

        //daca am completat ceva in amandoua, atunci incep sa verific helper text
        if(etEmail.getText().toString().length()>0 && etPass.getText().toString().length()>0){
            if(tilEmail.getHelperText()==null)
            {
                validEmail=1;
            }
            if(tilPass.getHelperText()==null)
            {
                validPass=1;
            }
        }
        if(validEmail==1 && validPass==1){
            return 1;
        }
        else{
            return 0;
        }

    }
}