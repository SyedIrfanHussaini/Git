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
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;
import com.chaos.view.PinView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.osdifa.anrifo.Helper.InternetService;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class Verification extends AppCompatActivity {

    private static final long START_TIME_IN_MILLS = 60000;
    ProgressBar progressBar;
    Button button, resend;
    PinView pinView;
    String phoneNumber, code, email, password, name, image = "Default";
    FirebaseUser user;
    FirebaseAuth mAuth;
    float v = 0;
    boolean onlyNumberVerification;
    TextView timer;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMills = START_TIME_IN_MILLS;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
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
        setContentView(R.layout.activity_verification);

        dialog = new CustomDialog.Builder(Verification.this)
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

        pinView = findViewById(R.id.pinView);
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        onlyNumberVerification = getIntent().getBooleanExtra("verifyNum", false);
        resend = findViewById(R.id.resend);
        timer = findViewById(R.id.timer);
        button = findViewById(R.id.otpButton);
        password = getIntent().getStringExtra("password");
        progressBar = findViewById(R.id.progressBar4);
        phoneNumber = getIntent().getStringExtra("number");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate) {
            pinView.setTranslationX(300);
            button.setTranslationX(300);
            timer.setTranslationX(300);

            pinView.setAlpha(v);
            button.setAlpha(v);
            timer.setAlpha(v);

            pinView.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
            button.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
            timer.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        }

        initiateOtp();
        updateCountDownText();
        startTimer();

    }

    public void resend(View view) {
        resend.setVisibility(View.INVISIBLE);
        mCountDownTimer.cancel();
        initiateOtp();
    }

    public void sendHome(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String otp = pinView.getText().toString();
        if (otp.isEmpty()) {
            Toast.makeText(this, "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMills / 1000) / 60;
        int second = (int) (mTimeLeftInMills / 1000) % 60;

        String TimeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, second);
        timer.setText(TimeLeftFormatted);
    }

    private void startTimer() {
        timer.setVisibility(View.VISIBLE);
        resend.setVisibility(View.INVISIBLE);
        mCountDownTimer = new CountDownTimer(mTimeLeftInMills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMills = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                timer.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if (onlyNumberVerification) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            reference.child("number").setValue(phoneNumber);
            Toast.makeText(this, "Number Verified", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    user = mAuth.getCurrentUser();
                                    createUser(user);
                                } else {
                                    Toast.makeText(this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("Email Error", task1.getException().getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Phone Error", task.getException().getMessage());
                        }
                    });
        }
    }

    private void initiateOtp() {
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Verification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                code = s;
            }
        };

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(Verification.this)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        startTimer();
    }

    private void createUser(FirebaseUser user) {
        String UID = user.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("number", phoneNumber);
        hashMap.put("name", name);
        hashMap.put("email", email);
        hashMap.put("UID", UID);
        hashMap.put("status", "Online");
        hashMap.put("image", image);
        hashMap.put("isConsultant", false);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(UID).setValue(hashMap);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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