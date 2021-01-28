package com.osdifa.anrifo;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.android.customdialog.CustomDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osdifa.anrifo.Helper.InternetService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class Problem extends AppCompatActivity {

    final int min = 10;
    final int max = 1000;
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
    Button timer;
    int random;
    EditText problem;
    String uid, name1, name2, image1, image2;
    FirebaseUser user;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        timer = findViewById(R.id.pickTimingBtn);
        uid = getIntent().getStringExtra("uid");
        problem = findViewById(R.id.question);
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name1 = snapshot.child("name").getValue(String.class);
                String is = snapshot.child("isConsultant").getValue(String.class);
                if (is.equals("true")) {
                    image1 = snapshot.child("consultantImage").getValue(String.class);
                } else {
                    image1 = snapshot.child("image").getValue(String.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference consultantReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        consultantReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name2 = snapshot.child("name").getValue(String.class);
                String is = snapshot.child("isConsultant").getValue(String.class);
                if (is.equals("true")) {
                    image2 = snapshot.child("consultantImage").getValue(String.class);
                } else {
                    image2 = snapshot.child("image").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dialog = new CustomDialog.Builder(Problem.this)
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


        random = new Random().nextInt((max - min) + 1) + min;

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, InternetService.class);
        startService(serviceIntent);

        if (isOnline(getApplicationContext())) {
            SetVisibilityOn();
        } else {
            SetVisibilityOff();
        }

    }

    public void Back(View view) {
        finish();
    }

    public void pickTime(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Date c = Calendar.getInstance().getTime();
        Calendar d = Calendar.getInstance();
        d.setTime(c);
        d.add(Calendar.DATE, 30);
        Date expDate = d.getTime();

        String locale = getApplicationContext().getResources().getConfiguration().locale.getCountry();

        Toast.makeText(this, locale, Toast.LENGTH_SHORT).show();

        if (locale.equals("india") | locale.equals("australia") | locale.equals("US")) {
            new SingleDateAndTimePickerDialog.Builder(Problem.this)
                    .mustBeOnFuture()
                    .curved()
                    .maxDateRange(expDate)
                    .displayMonth(false)
                    .displayYears(false)
                    .mainColor(Color.parseColor("#3279A6"))
                    .listener(date -> {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.DATE, date.getDate());
                        cal.set(Calendar.MONTH, date.getMonth());
                        cal.set(Calendar.HOUR, date.getHours());
                        cal.set(Calendar.MINUTE, date.getMinutes());
                        cal.set(Calendar.YEAR, date.getYear());
                        cal.set(Calendar.SECOND, date.getSeconds());
                        cal.add(Calendar.DATE, -1);

                        Toast.makeText(this, "" + cal.getTime(), Toast.LENGTH_SHORT).show();

                        CharSequence sequence = DateFormat.format("MMM d, h:mm a", cal);

                        timer.setText(sequence);

                        String Date = (String) sequence;
                        editor.putString(String.valueOf(random), Date);
                        editor.apply();

                    })
                    .title("Select Time")
                    .display();
        } else {
            new SingleDateAndTimePickerDialog.Builder(Problem.this)
                    .mustBeOnFuture()
                    .curved()
                    .maxDateRange(expDate)
                    .displayMonth(false)
                    .displayYears(false)
                    .mainColor(Color.parseColor("#3279A6"))
                    .listener(date -> {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.DATE, date.getDate());
                        cal.set(Calendar.MONTH, date.getMonth());
                        cal.set(Calendar.HOUR, date.getHours());
                        cal.set(Calendar.MINUTE, date.getMinutes());
                        cal.set(Calendar.YEAR, date.getYear());
                        cal.set(Calendar.SECOND, date.getSeconds());

                        Toast.makeText(this, "" + cal.getTime(), Toast.LENGTH_SHORT).show();

                        CharSequence sequence = DateFormat.format("MMM d, h:mm a", cal);

                        timer.setText(sequence);

                        String Date = (String) sequence;
                        editor.putString(String.valueOf(random), Date);
                        editor.apply();

                    })
                    .title("Select Time")
                    .display();
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

    public void schedule(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        String date = sharedPreferences.getString(String.valueOf(random), "irfan");

        if (date.equals("irfan")) {
            Toast.makeText(this, "Please Select Date And Time", Toast.LENGTH_SHORT).show();
            return;
        }
        if (problem.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Write Your Problem", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!name1.isEmpty() | !name2.isEmpty()) {
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            DatabaseReference consultantReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            HashMap<String, String> userHashMap = new HashMap<>();
            userHashMap.put("name", name2);
            userHashMap.put("time", date);
            userHashMap.put("image", image2);
            userHashMap.put("problem", problem.getText().toString());
            userReference.child("Meetings").push().setValue(userHashMap);
            HashMap<String, String> consultantHashMap = new HashMap<>();
            consultantHashMap.put("name", name1);
            consultantHashMap.put("time", date);
            consultantHashMap.put("image", image1);
            consultantHashMap.put("problem", problem.getText().toString());
            consultantReference.child("Meetings").push().setValue(consultantHashMap);
        }

    }
}