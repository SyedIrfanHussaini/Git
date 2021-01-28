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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.osdifa.anrifo.Helper.InternetService;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class ForgetPassword extends AppCompatActivity {

    EditText email;
    FirebaseAuth auth;
    ProgressBar progressBar;
    String error, error2;
    Button button;
    float v = 0;
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
        setContentView(R.layout.activity_forget_password);

        dialog = new CustomDialog.Builder(ForgetPassword.this)
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

        email = findViewById(R.id.FPEmail);
        progressBar = findViewById(R.id.progressBar5);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.FPButton);
        error = "There is no user record corresponding to this identifier. The user may have been deleted.";
        error2 = "The email address is badly formatted.";

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate) {
            email.setTranslationX(300);
            button.setTranslationX(300);

            email.setAlpha(v);
            button.setAlpha(v);

            email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
            button.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        }

    }

    public void ForgetPassword(View view) {
        String Email = email.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        if (Email.isEmpty()) {
            email.setError("Please Enter You Email Address");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        button.setOnClickListener(null);
        auth.sendPasswordResetEmail(Email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgetPassword.this, "We Have Send Reset Password Link On You Email Address", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            } else if (task.getException().getMessage().equals(error)) {
                email.setError("No Such User Exist");
                progressBar.setVisibility(View.INVISIBLE);
                button.setOnClickListener(this::ForgetPassword);
            } else if (task.getException().getMessage().equals(error2)) {
                email.setError("Invalid email address");
                progressBar.setVisibility(View.INVISIBLE);
                button.setOnClickListener(this::ForgetPassword);
            } else {
                Toast.makeText(ForgetPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                button.setOnClickListener(this::ForgetPassword);
                Log.e("error", task.getException().getMessage());
            }
        });
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