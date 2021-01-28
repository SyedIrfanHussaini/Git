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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.osdifa.anrifo.Helper.InternetService;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class NumberVerification extends AppCompatActivity {

    CountryCodePicker codePicker;
    EditText number;
    ProgressBar progressBar;
    Button verificationBtn;
    String phoneNumber;
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
        setContentView(R.layout.activity_number_verification);

        dialog = new CustomDialog.Builder(NumberVerification.this)
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

        codePicker = findViewById(R.id.countryCode);
        number = findViewById(R.id.verificationNumber);
        verificationBtn = findViewById(R.id.verificationBtn);
        progressBar = findViewById(R.id.progressBar8);

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate4", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);

        if (isAnimate) {
            if (animate) {
                codePicker.setTranslationX(300);
                number.setTranslationX(300);
                verificationBtn.setTranslationY(300);

                codePicker.setAlpha(0);
                number.setAlpha(0);
                verificationBtn.setAlpha(0);

                codePicker.animate().alpha(1).translationX(0).setDuration(800).setStartDelay(200).start();
                number.animate().alpha(1).translationX(0).setDuration(1000).setStartDelay(200).start();
                verificationBtn.animate().alpha(1).translationY(0).setDuration(800).setStartDelay(200).start();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("animate4", false);
                editor.apply();
            }
        }
    }

    public void verifyNumber(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (!checkNumber()) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        verificationBtn.setOnClickListener(null);
        phoneNumber = "+" + codePicker.getFullNumber() + number.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("number").equalTo(phoneNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    number.setError("This Phone Number Already Exist");
                } else {
                    Intent intent = new Intent(getApplicationContext(), Verification.class);
                    intent.putExtra("number", phoneNumber);
                    intent.putExtra("verifyNum", true);
                    startActivity(intent);
                }
                progressBar.setVisibility(View.INVISIBLE);
                verificationBtn.setOnClickListener(v -> verifyNumber(null));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                verificationBtn.setOnClickListener(v -> verifyNumber(null));
            }
        });
    }

    public boolean checkNumber() {
        String var = number.getText().toString().trim();

        if (var.isEmpty()) {
            number.setError("Please Enter Your Mobile Number");
            return false;
        } else if (var.length() < 4 | var.length() > 14) {
            number.setError("Invalid Mobile Number");
            return false;
        } else {
            number.setError(null);
            return true;
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