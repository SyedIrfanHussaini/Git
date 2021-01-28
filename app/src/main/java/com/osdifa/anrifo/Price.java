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
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osdifa.anrifo.Helper.InternetService;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;

import java.util.HashMap;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class Price extends AppCompatActivity {

    String country, Max, Min;
    TextView title, max, min, selected;
    ImageButton back;
    ProgressBar progressBar;
    TickSeekBar seekBar;
    View makeInvisibleView;
    Button button;
    FirebaseUser user;
    DatabaseReference reference;
    CustomDialog dialog;
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
        setContentView(R.layout.activity_price);

        dialog = new CustomDialog.Builder(Price.this)
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

        progressBar = findViewById(R.id.progressBar10);
        title = findViewById(R.id.title7);
        button = findViewById(R.id.payOut);
        max = findViewById(R.id.max);
        min = findViewById(R.id.min);
        selected = findViewById(R.id.selected);
        back = findViewById(R.id.backBtn6);
        seekBar = findViewById(R.id.seekBar);
        makeInvisibleView = findViewById(R.id.makeInvisibleView2);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                country = snapshot.child("country").getValue().toString();
                Max = snapshot.child("end").getValue().toString();
                Min = snapshot.child("start").getValue().toString();
                max.setText("Max\n" + Max);
                min.setText("Min\n" + Min);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        makeInvisibleView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.INVISIBLE);
            makeInvisibleView.setVisibility(View.INVISIBLE);
            next();
        }, 1000);
    }

    private void next() {
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate10", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate && isAnimate) {
            back.setTranslationY(-300);
            title.setTranslationX(300);
            max.setTranslationX(300);
            min.setTranslationX(300);
            selected.setTranslationX(300);
            seekBar.setTranslationX(300);
            button.setTranslationX(300);

            title.setAlpha(0);
            max.setAlpha(0);
            min.setAlpha(0);
            selected.setAlpha(0);
            seekBar.setAlpha(0);
            button.setAlpha(0);

            back.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(200).start();
            title.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(200).start();
            max.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(400).start();
            min.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(600).start();
            selected.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(800).start();
            seekBar.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1000).start();
            button.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1000).start();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("animate10", false);
            editor.apply();
        }

        seekBar.setMax(Integer.parseInt(Max));
        seekBar.setMin(Integer.parseInt(Min));
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                selected.setText("" + seekParams.progress);
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {

            }
        });

    }

    public void Back(View view) {
        finish();
    }

    public void paymentOutMethod(View view) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("price", seekBar.getProgress());
        reference.updateChildren(hashMap);
        Intent intent;
        if (country.toLowerCase().equals("india")) {
            intent = new Intent(Price.this, PaymentOption.class);
        } else {
            intent = new Intent(Price.this, PaymentOption2.class);
        }
        intent.putExtra("country", country);
        startActivity(intent);
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
        if (!dialog.isShowing()) {
            dialog.show();
        }
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