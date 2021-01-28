package com.osdifa.anrifo;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;

import com.anubhav.android.customdialog.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.osdifa.anrifo.Helper.InternetService;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final String BroadcastStringForAction = "checkinternet";
    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;
    Fragment fragment;
    float v = 0;
    boolean isHome = true;
    FirebaseUser user;
    String isConsultant;
    CustomDialog dialog;
    View fragmentContainer1, fragmentContainer2;
    public BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastStringForAction)) {
                if (intent.getStringExtra("online_status").equals("true")) {
                    SetVisibilityOn();
                } else {
                    SetVisibilityOff();
                }
            }
        }
    };
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new CustomDialog.Builder(MainActivity.this)
                .setTitle("No Connection")
                .setContent("Please Connect To Internet And Try Again")
                .setBtnConfirmText("Connect")
                .setBtnConfirmTextColor("#0C4C82")
                .setBtnCancelText("Cancel")
                .setBtnCancelTextColor("#0C4C82")
                .setGuideImage(R.drawable.no_internet)
                .setGuideImageSizeDp(200, 200)
                .setCancelable(false)
                .onConfirm((dialog, which) -> {
                    try {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    } catch (ActivityNotFoundException e) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
                        } else {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    }
                    dialog.dismiss();
                })
                .onCancel((dialog, which) -> dialog.dismiss())
                .build();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, InternetService.class);
        startService(serviceIntent);

        if (isOnline(getApplicationContext())) {
            SetVisibilityOn();
        } else {
            SetVisibilityOff();
        }

        bottomNav = findViewById(R.id.bottom_bar);
        fragmentContainer1 = findViewById(R.id.fragment_container);
        fragmentContainer2 = findViewById(R.id.fragment_container2);
        bottomNav.setItemSelected(R.id.home, true);
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("showAnimation", false);
        if (animate) {
            bottomNav.setTranslationY(300);
            bottomNav.setAlpha(v);
            bottomNav.animate().alpha(1).translationY(0).setDuration(600).setStartDelay(300).start();
        }
        fragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container2, fragment)
                .commit();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (sharedPreferences.getBoolean("underVerification", false)) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    isConsultant = snapshot.child("isConsultant").getValue().toString();
                    if (isConsultant.equals("Rejected")) {
                        startActivity(new Intent(getApplicationContext(), Rejected.class));
                    } else if (isConsultant.equals("Accepted")) {
                        startActivity(new Intent(getApplicationContext(), Accepted.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        bottomNav.setOnItemSelectedListener(id -> {
            switch (id) {
                case R.id.home:
                    fragmentContainer1.setVisibility(View.GONE);
                    fragmentContainer2.setVisibility(View.VISIBLE);
                    isHome = true;
                    break;
                case R.id.schedule:
                    fragmentContainer2.setVisibility(View.GONE);
                    fragmentContainer1.setVisibility(View.VISIBLE);
                    fragment = new ScheduleFragment();
                    isHome = false;
                    break;
                case R.id.chat:
                    fragmentContainer2.setVisibility(View.GONE);
                    fragmentContainer1.setVisibility(View.VISIBLE);
                    fragment = new ChatFragment();
                    isHome = false;
                    break;
                case R.id.profile:
                    fragmentContainer2.setVisibility(View.GONE);
                    fragmentContainer1.setVisibility(View.VISIBLE);
                    fragment = new ProfileFragment();
                    isHome = false;
                    break;
            }
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            } else {
                Log.e("NavigationBar Error", "Error in creating fragment");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isHome) {
            SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("progress1", true);
            editor.putBoolean("progress2", true);
            editor.putBoolean("progress3", true);
            editor.putBoolean("animate1", true);
            editor.putBoolean("animate2", true);
            editor.putBoolean("animate3", true);
            editor.putBoolean("animate4", true);
            editor.putBoolean("animate5", true);
            editor.putBoolean("animate6", true);
            editor.putBoolean("animate7", true);
            editor.putBoolean("animate8", true);
            editor.putBoolean("animate9", true);
            editor.putBoolean("animate10", true);
            editor.putBoolean("animate11", true);
            editor.putBoolean("animate12", true);
            editor.putBoolean("animate13", true);
            editor.putBoolean("animate14", true);
            editor.putBoolean("animate15", true);
            editor.putBoolean("animate16", true);
            editor.apply();
            super.onBackPressed();
        } else {
            fragmentContainer1.setVisibility(View.GONE);
            fragmentContainer2.setVisibility(View.VISIBLE);
            bottomNav.setItemSelected(R.id.home, true);
        }
    }

    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnectedOrConnecting();
    }

    public void SetVisibilityOn() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void SetVisibilityOff() {
        dialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(MyReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(MyReceiver, mIntentFilter);
    }

}