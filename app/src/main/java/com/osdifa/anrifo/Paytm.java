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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.osdifa.anrifo.Helper.InternetService;

import java.util.HashMap;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class Paytm extends AppCompatActivity {

    TextView countryCode, title, subTitle;
    ImageButton back;
    ProgressBar progressBar;
    Button button;
    EditText editText;
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
        setContentView(R.layout.activity_paytm);

        dialog = new CustomDialog.Builder(Paytm.this)
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

        countryCode = findViewById(R.id.countryCode);
        subTitle = findViewById(R.id.subTitle3);
        title = findViewById(R.id.title9);
        back = findViewById(R.id.backBtn8);
        progressBar = findViewById(R.id.progressBar8);
        button = findViewById(R.id.paytmBtn);
        editText = findViewById(R.id.paytmNumber);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate12", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate && isAnimate) {
            back.setTranslationY(-300);
            title.setTranslationX(300);
            subTitle.setTranslationX(300);
            countryCode.setTranslationX(300);
            editText.setTranslationX(300);
            button.setTranslationX(300);

            title.setAlpha(0);
            subTitle.setAlpha(0);
            countryCode.setAlpha(0);
            editText.setAlpha(0);
            button.setAlpha(0);

            back.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(200).start();
            title.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(200).start();
            subTitle.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(400).start();
            countryCode.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(600).start();
            editText.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(800).start();
            button.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1000).start();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("animate12", false);
            editor.apply();
        }

    }

    public void Back(View view) {
        finish();
    }

    public void verifyNumber(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (!checkNumber()) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("underVerification", false);
        editor.apply();
        String number = countryCode.getText().toString() + editText.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isConsultant", true);
        hashMap.put("payOut", "Paytm");
        hashMap.put("paytmNumber", number);
        hashMap.put("1star", 0.1);
        hashMap.put("2star", 0.1);
        hashMap.put("3star", 0.1);
        hashMap.put("4star", 0.1);
        hashMap.put("5star", 0.1);
        hashMap.put("totalReviews", 0);
        hashMap.put("totalRating", 0);
        reference.updateChildren(hashMap);
        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        progressBar.setVisibility(View.INVISIBLE);
    }

    public boolean checkNumber() {
        String var = editText.getText().toString().trim();

        if (var.isEmpty()) {
            editText.setError("Please Enter Your Mobile Number");
            return false;
        } else if (var.length() != 10) {
            editText.setError("Invalid Mobile Number");
            return false;
        } else {
            editText.setError(null);
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