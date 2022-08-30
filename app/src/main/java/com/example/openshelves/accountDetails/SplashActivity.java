package com.example.openshelves.accountDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.openshelves.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //hiding the navigation bar
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView tvQuoteSplash=findViewById(R.id.tvQuoteSplash);
        TextView tvQuoteAuthor=findViewById(R.id.tvQuoteAuthor);

        ImageView imgSplash=findViewById(R.id.imgSplash);
        imgSplash.animate().translationX(-1000).setDuration(1000).setStartDelay(2500);

        tvQuoteSplash.animate().translationX(1000).setDuration(1000).setStartDelay(2500);
        tvQuoteAuthor.animate().translationX(1000).setDuration(1000).setStartDelay(2500);

        //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        Thread thread=new Thread(){
            public void run(){
                try{
                    Thread.sleep(4000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    checkUser();

                }
            }
        };

        thread.start();

    }

    private void checkUser() {
        //get current user, if logged in
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            //user is not logged in => go to login screen
            Intent intent =new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            //user is already logged in => go to main screen, so skip login
            Intent intent =new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}