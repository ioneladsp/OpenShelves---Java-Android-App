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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    EditText etFullName, etEmail, etPass, etPassConfirm;
    TextInputLayout tilFullName, tilEmail, tilPass, tilPassConfirm;
    TextView tvLogIn;
    Button btnRegister;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        //textinputlayout
        tilFullName=findViewById(R.id.tilName);
        tilEmail=findViewById(R.id.tilEmail);
        tilPass=findViewById(R.id.tilPass);
        tilPassConfirm=findViewById(R.id.tilConfirmPass);

        //edit text
        etFullName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        etPass=findViewById(R.id.etPass);
        etPassConfirm=findViewById(R.id.etConfirmPass);


        //text view for back to LogIn
        tvLogIn=findViewById(R.id.tvLogIn);

        //button for back to login
        btnRegister=findViewById(R.id.btnRegister);


        //revenire la login
        tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        NameFocusListener();
        EmailFocusListener();
        PasswordFocusListener();
        PasswordConfirmFocusListener();

        //init firebase auth
        firebaseAuth= FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilPassConfirm.setHelperText(null);
                int ok= validation();
                //se creeaza contul
                if(ok==1){
                    createUserAccount();
                }
                else if(ok==2){

                }
                else{
                    Toast.makeText(RegisterActivity.this,"Account could not be created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }

            }

        });

    }

    private void createUserAccount() {
        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassConfirm.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegisterActivity.this,"Account created", Toast.LENGTH_SHORT).show();
                //account creation succes, now add in firebase realtime database
                updateUserInfo();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInfo() {
        //get current user uid, since user is registered
        String uid=firebaseAuth.getUid();

        //setup data to add in db
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("fullname",etFullName.getText().toString());
        hashMap.put("email",etEmail.getText().toString());
        hashMap.put("books","");
        hashMap.put("goal",12); //goal-ul anual al utilizatorului


        //set data to db
        DatabaseReference ref= FirebaseDatabase.getInstance("https://openshelves-1b6cb-default-rtdb.firebaseio.com").getReference("Users");
        ref.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //data added to db
                Toast.makeText(RegisterActivity.this,"Account created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //data failed adding to db
                Toast.makeText(RegisterActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void NameFocusListener() {
        etFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    tilFullName.setHelperText(validName());
                }
            }
        });
    }

    private String validName(){
        String name=etFullName.getText().toString();
        if(name.length()<5){
            return "Minimum 5 characters";
        }
        return null;
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

    private void PasswordFocusListener() {
        etPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    tilPass.setHelperText(validPassword());
                }
            }
        });
    }

    private String validPassword(){
        String password=etPass.getText().toString();
        if(password.length()<8){
            return "Minimum 8 Character Password";
        }
        if(!password.matches(".*[A-Z].*")){
            return("Minimum 1 Upper-case Character");
        }
        if(!password.matches(".*[a-z].*")){
            return("Minimum 1 Lower-case Character");
        }
        return null;
    }


    private void PasswordConfirmFocusListener() {
        etPassConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    tilPassConfirm.setHelperText(validPasswordConfirm());
                }
            }
        });
    }

    private String validPasswordConfirm(){
        String passwordConfirm=etPassConfirm.getText().toString();
        String pass=etPass.getText().toString(); //initial password
        if(!passwordConfirm.equals(pass)){
            return("Password Do not Match");
        }

        return null;
    }

    private String validPassmethod(){
        String passwordConfirm=etPassConfirm.getText().toString();
        String pass=etPass.getText().toString(); //initial password
        if(!passwordConfirm.equals(pass)){
            return("Password Do not Match");
        }
        return null;
    }

    private int validation() {
        //before creating an account, we do data validation
        int validName=0;
        int validEmail=0;
        int validPass=0;
        int validPassConf=0;
        if(tilFullName.getHelperText()==null)
        {
            validName=1;
        }
        if(tilEmail.getHelperText()==null)
        {
            validEmail=1;
        }
        if(tilPass.getHelperText()==null)
        {
            validPass=1;
        }
        //daca nu are nicio eroare si parolele corespund
        if(tilPassConfirm.getHelperText()==null && etPassConfirm.getText().toString().equals(etPass.getText().toString()))
        {
            validPassConf=1;

        }
        else{
            validPassConf=2;
            tilPassConfirm.setHelperText("Password Does Not Match");
        }

        if(validName==1 && validEmail==1 && validPass==1 && validPassConf==1){
            return 1;
        }
        else if(validPassConf==2){
            return 2;
        }
        else {
            return 0;
        }
    }
}