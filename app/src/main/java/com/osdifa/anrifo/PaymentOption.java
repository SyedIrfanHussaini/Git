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

public class PaymentOption extends AppCompatActivity {

    TextView title, comingSoon;
    ImageButton back;
    Button paytm, googlePay, paypal;
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
        setContentView(R.layout.activity_payment_option);

        dialog = new CustomDialog.Builder(PaymentOption.this)
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

        title = findViewById(R.id.title10);
        comingSoon = findViewById(R.id.comingSoon);
        back = findViewById(R.id.backBtn7);
        paytm = findViewById(R.id.paytm);
        googlePay = findViewById(R.id.googlePay);
        paypal = findViewById(R.id.paypal);

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate11", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate && isAnimate) {
            back.setTranslationY(-300);
            title.setTranslationX(300);
            paytm.setTranslationX(300);
            googlePay.setTranslationX(300);
            paypal.setTranslationX(300);
            comingSoon.setTranslationX(300);

            title.setAlpha(0);
            paytm.setAlpha(0);
            googlePay.setAlpha(0);
            paypal.setAlpha(0);
            comingSoon.setAlpha(0);

            back.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(200).start();
            title.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(200).start();
            paytm.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(400).start();
            googlePay.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(600).start();
            paypal.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(800).start();
            comingSoon.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1000).start();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("animate11", false);
            editor.apply();
        }


    }

    public void Back(View view) {
        finish();
    }

    public void paytm(View view) {
        startActivity(new Intent(getApplicationContext(), Paytm.class));
    }

    public void googlePay(View view) {
        startActivity(new Intent(getApplicationContext(), GooglePay.class));
    }

    public void payPal(View view) {
        startActivity(new Intent(getApplicationContext(), PayPal.class));
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