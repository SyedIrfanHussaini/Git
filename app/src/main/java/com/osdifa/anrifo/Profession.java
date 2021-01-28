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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;
import com.osdifa.anrifo.Helper.InternetService;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class Profession extends AppCompatActivity {

    Button finish;
    ImageButton back;
    TextView title;
    RadioButton radio1, radio2, radio3, radio4, radio5, radio6;
    boolean happyDestroy = false;
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
        setContentView(R.layout.activity_profession);

        dialog = new CustomDialog.Builder(Profession.this)
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

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);
        radio5 = findViewById(R.id.radio5);
        radio6 = findViewById(R.id.radio6);
        finish = findViewById(R.id.save);
        back = findViewById(R.id.backBtn2);
        title = findViewById(R.id.title3);

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate6", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);
        if (isAnimate) {
            if (animate) {
                radio1.setTranslationX(300);
                radio2.setTranslationX(300);
                radio3.setTranslationX(300);
                radio4.setTranslationX(300);
                radio5.setTranslationX(300);
                radio6.setTranslationX(300);
                finish.setTranslationX(300);
                back.setTranslationY(-300);
                title.setTranslationY(-300);

                radio1.setAlpha(0);
                radio2.setAlpha(0);
                radio3.setAlpha(0);
                radio4.setAlpha(0);
                radio5.setAlpha(0);
                radio6.setAlpha(0);
                finish.setAlpha(0);
                title.setAlpha(0);

                radio1.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(200).start();
                radio2.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(400).start();
                radio3.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(600).start();
                radio4.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(800).start();
                radio5.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1000).start();
                radio6.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1200).start();
                finish.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1400).start();
                back.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(400).start();
                title.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(600).start();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("animate6", false);
                editor.apply();
            }
        }
    }

    public void profession(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profession", view.getTag().toString());
        editor.apply();
    }

    public void Back(View view) {
        finish();
    }

    public void save(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String pro = sharedPreferences.getString("profession", "0");
        if (pro.equals("0")) {
            Toast.makeText(this, "Please Select You Profession", Toast.LENGTH_SHORT).show();
        } else {
            happyDestroy = true;
            editor.putBoolean("professionSelected", true);
            editor.apply();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (!happyDestroy) {
            SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profession", null);
            editor.putBoolean("professionSelected", false);
            editor.apply();
        }
        super.onDestroy();
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