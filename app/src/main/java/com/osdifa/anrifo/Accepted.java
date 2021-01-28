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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;
import com.osdifa.anrifo.Helper.InternetService;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class Accepted extends AppCompatActivity {

    ImageButton back;
    TextView title, subTitle;
    Button button;
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
        setContentView(R.layout.activity_accepted);

        dialog = new CustomDialog.Builder(Accepted.this)
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

        title = findViewById(R.id.title5);
        subTitle = findViewById(R.id.subTitle2);
        back = findViewById(R.id.backBtn5);
        button = findViewById(R.id.choosePrice);

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate9", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate && isAnimate) {
            back.setTranslationY(-300);
            title.setTranslationX(300);
            subTitle.setTranslationX(300);
            button.setTranslationX(300);

            title.setAlpha(0);
            subTitle.setAlpha(0);
            button.setAlpha(0);

            back.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(200).start();
            title.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(200).start();
            subTitle.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(400).start();
            button.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(600).start();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("animate9", false);
            editor.apply();
        }

    }

    public void Back(View view) {
        finish();
    }

    public void paymentMethod(View view) {
        Intent intent = new Intent(getApplicationContext(), Price.class);
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