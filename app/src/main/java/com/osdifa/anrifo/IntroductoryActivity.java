package com.osdifa.anrifo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroductoryActivity extends AppCompatActivity {

    public static final int NUM_PAGES = 3;
    static final int Request_CODE = 123;
    ImageView logo, appName, splashImg;
    LottieAnimationView lottieAnimationView;
    FirebaseUser auth;
    float v = 0;
    Animation anim;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.appName);
        splashImg = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.lottie);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        requestPermission();
        viewPager = findViewById(R.id.pager);
        viewPager.setAlpha(v);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        splashImg.animate().translationY(-3500).setDuration(1000).setStartDelay(3000);
        logo.animate().translationY(2500).setDuration(1000).setStartDelay(3000);
        appName.animate().translationY(2500).setDuration(1000).setStartDelay(3000);
        lottieAnimationView.animate().translationY(2500).setDuration(1000).setStartDelay(3000);

        new Handler().postDelayed(this::getUsedMemorySize, 5500);

        anim = AnimationUtils.loadAnimation(this, R.anim.o_b_anim);
        viewPager.setAnimation(anim);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(IntroductoryActivity.this,
                Manifest.permission.CAMERA) +
                ContextCompat.checkSelfPermission(IntroductoryActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(IntroductoryActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(IntroductoryActivity.this,
                        Manifest.permission.ACCESS_NETWORK_STATE) +
                ContextCompat.checkSelfPermission(IntroductoryActivity.this,
                        Manifest.permission.ACCESS_WIFI_STATE) +
                ContextCompat.checkSelfPermission(IntroductoryActivity.this,
                        Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(IntroductoryActivity.this,
                    Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(IntroductoryActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(IntroductoryActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(IntroductoryActivity.this,
                            Manifest.permission.ACCESS_NETWORK_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(IntroductoryActivity.this,
                            Manifest.permission.ACCESS_WIFI_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(IntroductoryActivity.this,
                            Manifest.permission.INTERNET)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(IntroductoryActivity.this);
                builder.setTitle("Please Grant Those Permission To Use This App")
                        .setMessage("Camera, Read Storage, Access Location And Internet")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(
                                IntroductoryActivity.this,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_NETWORK_STATE,
                                        Manifest.permission.ACCESS_WIFI_STATE,
                                        Manifest.permission.INTERNET,
                                },
                                Request_CODE
                        )).setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(
                        IntroductoryActivity.this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.INTERNET,
                        },
                        Request_CODE
                );
            }
        }

    }

    public void getUsedMemorySize() {
        long maxHeapSizeInMB = 0L;
        try {
            Runtime info = Runtime.getRuntime();
            maxHeapSizeInMB = info.maxMemory() / 1048576L;
            if (maxHeapSizeInMB > 250) {
                SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("showAnimation", true);
                editor.apply();
            }
            if (auth != null) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                v = 1;
                viewPager.setAlpha(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
                    return tab1;
                case 1:
                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
                    return tab2;
                case 2:
                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}