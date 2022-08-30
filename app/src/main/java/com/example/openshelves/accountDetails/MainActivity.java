package com.example.openshelves.accountDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.openshelves.DiscoverFragment;
import com.example.openshelves.MyBooksFragment;
import com.example.openshelves.ProfileFragment;
import com.example.openshelves.R;
import com.example.openshelves.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
        checkUser();

        replaceFragment(new DiscoverFragment());


        BottomNavigationView btnNavView=findViewById(R.id.bottomNavigationView);

        btnNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.discover:
                        replaceFragment(new DiscoverFragment());
                        break;
                    case R.id.mybooks:
                        replaceFragment(new MyBooksFragment());
                        break;
                    case R.id.search:
                        replaceFragment(new SearchFragment());
                        break;
                    case R.id.profile:
                        replaceFragment(new ProfileFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            //not logged in, go to login screen
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}