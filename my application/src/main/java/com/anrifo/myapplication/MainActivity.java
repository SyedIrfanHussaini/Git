package com.anrifo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar bottomNav;
    Fragment fragment;
    FirebaseUser auth;
    FirebaseAuth firebaseAuth;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        auth = firebaseAuth.getCurrentUser();
        if (auth == null) {
            FirebaseAuth.getInstance().signInAnonymously();
        } else {
            auth.delete();
            firebaseAuth.signOut();
            FirebaseAuth.getInstance().signInAnonymously();
        }

        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setItemSelected(R.id.requests, true);

        fragment = new Requests();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        bottomNav.setOnItemSelectedListener(id -> {
            switch (id) {
                case R.id.requests:
                    fragment = new Requests();
                    break;
                case R.id.settings:
                    fragment = new Settings();
                    break;
                case R.id.images:
                    startActivity(new Intent(getApplicationContext(), UploadImages.class));
                    break;
            }
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
            } else {
                Log.e("NavigationBar Error", "Error in creating fragment");
            }
        });
    }
}