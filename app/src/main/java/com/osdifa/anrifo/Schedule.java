package com.osdifa.anrifo;

import android.app.TimePickerDialog;
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
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.anubhav.android.customdialog.CustomDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.osdifa.anrifo.Helper.InternetService;

import java.util.Calendar;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class Schedule extends AppCompatActivity {

    ConstraintLayout mondayConstraint, tuesdayConstraint, wednesdayConstraint, thursdayConstraint;
    ConstraintLayout fridayConstraint, saturdayConstraint, sundayConstraint;
    ConstraintLayout constraintLayout1, constraintLayout2, constraintLayout3, constraintLayout4;
    ConstraintLayout constraintLayout5, constraintLayout6, constraintLayout7, constraintLayout8;
    ConstraintLayout constraintLayout9, constraintLayout10, constraintLayout11, constraintLayout12;
    ConstraintLayout constraintLayout13, constraintLayout14;
    TextView mondayTxtBtn, mondayStart1, mondayEnd1, mondayStart2, mondayEnd2;
    TextView tuesdayTxtBtn, tuesdayStart1, tuesdayEnd1, tuesdayStart2, tuesdayEnd2;
    TextView wednesdayTxtBtn, wednesdayStart1, wednesdayEnd1, wednesdayStart2, wednesdayEnd2;
    TextView thursdayTxtBtn, thursdayStart1, thursdayEnd1, thursdayStart2, thursdayEnd2;
    TextView fridayTxtBtn, fridayStart1, fridayEnd1, fridayStart2, fridayEnd2;
    TextView saturdayTxtBtn, saturdayStart1, saturdayEnd1, saturdayStart2, saturdayEnd2;
    TextView sundayTxtBtn, sundayStart1, sundayEnd1, sundayStart2, sundayEnd2;
    int MS1M, MS1H = 25, ME1M, ME1H = 25, MS2M, MS2H = 25, ME2M, ME2H = 25;
    int TS1M, TS1H = 25, TE1M, TE1H = 25, TS2M, TS2H = 25, TE2M, TE2H = 25;
    int WS1M, WS1H = 25, WE1M, WE1H = 25, WS2M, WS2H = 25, WE2M, WE2H = 25;
    int THS1M, THS1H = 25, THE1M, THE1H = 25, THS2M, THS2H = 25, THE2M, THE2H = 25;
    int FS1M, FS1H = 25, FE1M, FE1H = 25, FS2M, FS2H = 25, FE2M, FE2H = 25;
    int SS1M, SS1H = 25, SE1M, SE1H = 25, SS2M, SS2H = 25, SE2M, SE2H = 25;
    int SUS1M, SUS1H = 25, SUE1M, SUE1H = 25, SUS2M, SUS2H = 25, SUE2M, SUE2H = 25;
    Button save;
    ImageButton back;
    TextView title, subTitle;
    SwitchMaterial monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    boolean mondayCheck = true, tuesdayCheck = true, wednesdayCheck = true, thursdayCheck = true;
    boolean fridayCheck = true, saturdayCheck = true, sundayCheck = false, happyDestroy = false;
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
        setContentView(R.layout.activity_schedule);

        dialog = new CustomDialog.Builder(Schedule.this)
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

        constraintLayout1 = findViewById(R.id.constraintLayout1);
        constraintLayout2 = findViewById(R.id.constraintLayout2);
        constraintLayout3 = findViewById(R.id.constraintLayout3);
        constraintLayout4 = findViewById(R.id.constraintLayout4);
        constraintLayout5 = findViewById(R.id.constraintLayout5);
        constraintLayout6 = findViewById(R.id.constraintLayout6);
        constraintLayout7 = findViewById(R.id.constraintLayout7);
        constraintLayout8 = findViewById(R.id.constraintLayout8);
        constraintLayout9 = findViewById(R.id.constraintLayout9);
        constraintLayout10 = findViewById(R.id.constraintLayout10);
        constraintLayout11 = findViewById(R.id.constraintLayout11);
        constraintLayout12 = findViewById(R.id.constraintLayout12);
        constraintLayout13 = findViewById(R.id.constraintLayout13);
        constraintLayout14 = findViewById(R.id.constraintLayout14);

        save = findViewById(R.id.save2);
        back = findViewById(R.id.backBtn3);
        title = findViewById(R.id.title2);
        subTitle = findViewById(R.id.subTitle1);

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);

        monday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                constraintLayout8.setVisibility(View.GONE);
                mondayConstraint.setVisibility(View.GONE);
                mondayCheck = false;
            } else {
                constraintLayout8.setVisibility(View.VISIBLE);
                if (mondayTxtBtn.getVisibility() == View.VISIBLE) {
                    mondayConstraint.setVisibility(View.GONE);
                } else {
                    mondayConstraint.setVisibility(View.VISIBLE);
                }
                mondayCheck = true;
            }
        });

        tuesday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                constraintLayout9.setVisibility(View.GONE);
                tuesdayConstraint.setVisibility(View.GONE);
                tuesdayCheck = false;
            } else {
                constraintLayout9.setVisibility(View.VISIBLE);
                if (mondayTxtBtn.getVisibility() == View.VISIBLE) {
                    tuesdayConstraint.setVisibility(View.GONE);
                } else {
                    tuesdayConstraint.setVisibility(View.VISIBLE);
                }
                tuesdayCheck = true;
            }
        });

        wednesday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                constraintLayout10.setVisibility(View.GONE);
                wednesdayConstraint.setVisibility(View.GONE);
                wednesdayCheck = false;
            } else {
                constraintLayout10.setVisibility(View.VISIBLE);
                if (wednesdayTxtBtn.getVisibility() == View.VISIBLE) {
                    wednesdayConstraint.setVisibility(View.GONE);
                } else {
                    wednesdayConstraint.setVisibility(View.VISIBLE);
                }
                wednesdayCheck = true;
            }
        });

        thursday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                constraintLayout11.setVisibility(View.GONE);
                thursdayConstraint.setVisibility(View.GONE);
                thursdayCheck = false;
            } else {
                constraintLayout11.setVisibility(View.VISIBLE);
                if (thursdayTxtBtn.getVisibility() == View.VISIBLE) {
                    thursdayConstraint.setVisibility(View.GONE);
                } else {
                    thursdayConstraint.setVisibility(View.VISIBLE);
                }
                thursdayCheck = true;
            }
        });

        friday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                constraintLayout12.setVisibility(View.GONE);
                fridayConstraint.setVisibility(View.GONE);
                fridayCheck = false;
            } else {
                constraintLayout12.setVisibility(View.VISIBLE);
                if (fridayTxtBtn.getVisibility() == View.VISIBLE) {
                    fridayConstraint.setVisibility(View.GONE);
                } else {
                    fridayConstraint.setVisibility(View.VISIBLE);
                }
                fridayCheck = true;
            }
        });

        saturday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                constraintLayout13.setVisibility(View.GONE);
                saturdayConstraint.setVisibility(View.GONE);
                saturdayCheck = false;
            } else {
                constraintLayout13.setVisibility(View.VISIBLE);
                if (saturdayTxtBtn.getVisibility() == View.VISIBLE) {
                    saturdayConstraint.setVisibility(View.GONE);
                } else {
                    saturdayConstraint.setVisibility(View.VISIBLE);
                }
                saturdayCheck = true;
            }
        });

        sunday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                constraintLayout14.setVisibility(View.GONE);
                sundayConstraint.setVisibility(View.GONE);
                sundayCheck = false;
            } else {
                constraintLayout14.setVisibility(View.VISIBLE);
                if (sundayTxtBtn.getVisibility() == View.VISIBLE) {
                    sundayConstraint.setVisibility(View.GONE);
                } else {
                    sundayConstraint.setVisibility(View.VISIBLE);
                }
                sundayCheck = true;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate7", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);
        if (isAnimate && animate) {
            constraintLayout1.setTranslationX(300);
            constraintLayout2.setTranslationX(300);
            constraintLayout3.setTranslationX(300);
            constraintLayout4.setTranslationX(300);
            constraintLayout5.setTranslationX(300);
            constraintLayout6.setTranslationX(300);
            constraintLayout7.setTranslationX(300);
            save.setTranslationX(300);
            back.setTranslationY(-300);
            title.setTranslationY(-300);
            subTitle.setTranslationY(-300);

            constraintLayout1.setAlpha(0);
            constraintLayout2.setAlpha(0);
            constraintLayout3.setAlpha(0);
            constraintLayout4.setAlpha(0);
            constraintLayout5.setAlpha(0);
            constraintLayout6.setAlpha(0);
            constraintLayout7.setAlpha(0);
            save.setAlpha(0);
            title.setAlpha(0);
            subTitle.setAlpha(0);

            constraintLayout1.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
            constraintLayout2.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(400).start();
            constraintLayout3.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(600).start();
            constraintLayout4.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(800).start();
            constraintLayout5.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(1000).start();
            constraintLayout6.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(1200).start();
            constraintLayout7.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(1400).start();
            save.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(1600).start();
            back.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(400).start();
            title.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(600).start();
            subTitle.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(800).start();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("animate7", false);
            editor.apply();
        }

        mondayConstraint = findViewById(R.id.mondayConstraint);
        tuesdayConstraint = findViewById(R.id.tuesdayConstraint);
        wednesdayConstraint = findViewById(R.id.wednesdayConstraint);
        thursdayConstraint = findViewById(R.id.thursdayConstraint);
        fridayConstraint = findViewById(R.id.fridayConstraint);
        saturdayConstraint = findViewById(R.id.saturdayConstraint);
        sundayConstraint = findViewById(R.id.sundayConstraint);

        mondayTxtBtn = findViewById(R.id.mondayTxtBtn);
        mondayStart1 = findViewById(R.id.mondayStart);
        mondayEnd1 = findViewById(R.id.mondayEnd);
        mondayStart2 = findViewById(R.id.mondayStart2);
        mondayEnd2 = findViewById(R.id.mondayEnd2);

        tuesdayTxtBtn = findViewById(R.id.tuesdayTxtBtn);
        tuesdayStart1 = findViewById(R.id.tuesdayStart);
        tuesdayEnd1 = findViewById(R.id.tuesdayEnd);
        tuesdayStart2 = findViewById(R.id.tuesdayStart2);
        tuesdayEnd2 = findViewById(R.id.tuesdayEnd2);

        wednesdayTxtBtn = findViewById(R.id.wednesdayTxtBtn);
        wednesdayStart1 = findViewById(R.id.wednesdayStart);
        wednesdayEnd1 = findViewById(R.id.wednesdayEnd);
        wednesdayStart2 = findViewById(R.id.wednesdayStart2);
        wednesdayEnd2 = findViewById(R.id.wednesdayEnd2);

        thursdayTxtBtn = findViewById(R.id.thursdayTxtBtn);
        thursdayStart1 = findViewById(R.id.thursdayStart);
        thursdayEnd1 = findViewById(R.id.thursdayEnd);
        thursdayStart2 = findViewById(R.id.thursdayStart2);
        thursdayEnd2 = findViewById(R.id.thursdayEnd2);

        fridayTxtBtn = findViewById(R.id.fridayTxtBtn);
        fridayStart1 = findViewById(R.id.fridayStart);
        fridayEnd1 = findViewById(R.id.fridayEnd);
        fridayStart2 = findViewById(R.id.fridayStart2);
        fridayEnd2 = findViewById(R.id.fridayEnd2);

        saturdayTxtBtn = findViewById(R.id.saturdayTxtBtn);
        saturdayStart1 = findViewById(R.id.saturdayStart);
        saturdayEnd1 = findViewById(R.id.saturdayEnd);
        saturdayStart2 = findViewById(R.id.saturdayStart2);
        saturdayEnd2 = findViewById(R.id.saturdayEnd2);

        sundayTxtBtn = findViewById(R.id.sundayTxtBtn);
        sundayStart1 = findViewById(R.id.sundayStart);
        sundayEnd1 = findViewById(R.id.sundayEnd);
        sundayStart2 = findViewById(R.id.sundayStart2);
        sundayEnd2 = findViewById(R.id.sundayEnd2);

        restore();

    }

    public void Back(View view) {
        finish();
    }

    public void monday(View view) {
        mondayTxtBtn.setVisibility(View.GONE);
        mondayConstraint.setVisibility(View.VISIBLE);
    }

    public void mondayStart1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            MS1H = hourOfDay;
            MS1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            mondayStart1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(MS1H, MS1M);
        timePickerDialog.show();
    }

    public void mondayEnd1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            ME1H = hourOfDay;
            ME1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            mondayEnd1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(ME1H, ME1M);
        timePickerDialog.show();
    }

    public void mondayEnd2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            ME2H = hourOfDay;
            ME2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            mondayEnd2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(ME2H, ME2M);
        timePickerDialog.show();
    }

    public void mondayStart2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            MS2H = hourOfDay;
            MS2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            mondayStart2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(MS2H, MS2M);
        timePickerDialog.show();
    }

    public void tuesdayStart1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            TS1H = hourOfDay;
            TS1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            tuesdayStart1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(TS1H, TS1M);
        timePickerDialog.show();
    }

    public void tuesdayEnd1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            TE1H = hourOfDay;
            TE1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            tuesdayEnd1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(TE1H, TE1M);
        timePickerDialog.show();
    }

    public void tuesday(View view) {
        tuesdayTxtBtn.setVisibility(View.GONE);
        tuesdayConstraint.setVisibility(View.VISIBLE);
    }

    public void tuesdayStart2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            TS2H = hourOfDay;
            TS2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            tuesdayStart2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(TS2H, TS2M);
        timePickerDialog.show();
    }

    public void tuesdayEnd2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            TE2H = hourOfDay;
            TE2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            tuesdayEnd2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(TE2H, TE2M);
        timePickerDialog.show();
    }

    public void wednesdayStart1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            WS1H = hourOfDay;
            WS1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            wednesdayStart1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(WS1H, WS1M);
        timePickerDialog.show();
    }

    public void wednesdayEnd1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            WE1H = hourOfDay;
            WE1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            wednesdayEnd1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(WE1H, WE1M);
        timePickerDialog.show();
    }

    public void wednesday(View view) {
        wednesdayTxtBtn.setVisibility(View.GONE);
        wednesdayConstraint.setVisibility(View.VISIBLE);
    }

    public void wednesdayStart2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            WS2H = hourOfDay;
            WS2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            wednesdayStart2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(WS2H, WS2M);
        timePickerDialog.show();
    }

    public void wednesdayEnd2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            WE2H = hourOfDay;
            WE2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            wednesdayEnd2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(WE2H, WE2M);
        timePickerDialog.show();
    }

    public void thursdayStart1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            THS1H = hourOfDay;
            THS1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            thursdayStart1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(THS1H, THS1M);
        timePickerDialog.show();
    }

    public void thursdayEnd1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            THE1H = hourOfDay;
            THE1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            thursdayEnd1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(THE1H, THE1M);
        timePickerDialog.show();
    }

    public void thursday(View view) {
        thursdayTxtBtn.setVisibility(View.GONE);
        thursdayConstraint.setVisibility(View.VISIBLE);
    }

    public void thursdayStart2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            THS2H = hourOfDay;
            THS2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            thursdayStart2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(THS2H, THS2M);
        timePickerDialog.show();
    }

    public void thursdayEnd2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            THE2H = hourOfDay;
            THE2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            thursdayEnd2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(THE2H, THE2M);
        timePickerDialog.show();
    }

    public void fridayStart1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            FS1H = hourOfDay;
            FS1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            fridayStart1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(FS1H, FS1M);
        timePickerDialog.show();
    }

    public void fridayEnd1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            FE1H = hourOfDay;
            FE1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            fridayEnd1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(FE1H, FE1M);
        timePickerDialog.show();
    }

    public void friday(View view) {
        fridayTxtBtn.setVisibility(View.GONE);
        fridayConstraint.setVisibility(View.VISIBLE);
    }

    public void fridayStart2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            FS2H = hourOfDay;
            FS2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            fridayStart2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(FS2H, FS2M);
        timePickerDialog.show();
    }

    public void fridayEnd2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            FE2H = hourOfDay;
            FE2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            fridayEnd2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(FE2H, FE2M);
        timePickerDialog.show();
    }

    public void saturdayStart1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            SS1H = hourOfDay;
            SS1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            saturdayStart1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(SS1H, SS1M);
        timePickerDialog.show();
    }

    public void saturdayEnd1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            SE1H = hourOfDay;
            SE1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            saturdayEnd1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(SE1H, SE1M);
        timePickerDialog.show();
    }

    public void saturday(View view) {
        saturdayTxtBtn.setVisibility(View.GONE);
        saturdayConstraint.setVisibility(View.VISIBLE);
    }

    public void saturdayStart2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            SS2H = hourOfDay;
            SS2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            saturdayStart2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(SS2H, SS2M);
        timePickerDialog.show();
    }

    public void saturdayEnd2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            SE2H = hourOfDay;
            SE2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            saturdayEnd2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(SE2H, SE2M);
        timePickerDialog.show();
    }

    public void sundayStart1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            SUS1H = hourOfDay;
            SUS1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            sundayStart1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(SUS1H, SUS1M);
        timePickerDialog.show();
    }

    public void sundayEnd1(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            SUE1H = hourOfDay;
            SUE1M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            sundayEnd1.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(SUE1H, SUE1M);
        timePickerDialog.show();
    }

    public void sunday(View view) {
        sundayTxtBtn.setVisibility(View.GONE);
        sundayConstraint.setVisibility(View.VISIBLE);
    }

    public void sundayStart2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            SUS2H = hourOfDay;
            SUS2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            sundayStart2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(SUS2H, SUS2M);
        timePickerDialog.show();
    }

    public void sundayEnd2(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            SUE2H = hourOfDay;
            SUE2M = minute;
            Calendar calendar = Calendar.getInstance();
            calendar.set(0, 0, 0, hourOfDay, minute);
            sundayEnd2.setText(DateFormat.format("hh:mm aa", calendar));
        }, 12, 0, false);
        timePickerDialog.updateTime(SUE2H, SUE2M);
        timePickerDialog.show();
    }

    public void restore() {
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("monday", false)) {
            constraintLayout8.setVisibility(View.VISIBLE);
            monday.setChecked(true);
            MS1H = sharedPreferences.getInt("mondayStartHour", 48);
            MS1M = sharedPreferences.getInt("mondayStartMin", 0);
            ME1H = sharedPreferences.getInt("mondayEndHour", 48);
            ME1M = sharedPreferences.getInt("mondayEndMin", 0);
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.set(0, 0, 0, MS1H, MS1M);
            calendar2.set(0, 0, 0, ME1H, ME1M);
            mondayStart1.setText(DateFormat.format("hh:mm aa", calendar1));
            mondayEnd1.setText(DateFormat.format("hh:mm aa", calendar2));
            if (sharedPreferences.getBoolean("mondaySlot2", false)) {
                MS2H = sharedPreferences.getInt("mondayStart2Hour", 48);
                MS2M = sharedPreferences.getInt("mondayStart2Min", 0);
                ME2H = sharedPreferences.getInt("mondayEnd2Hour", 48);
                ME2M = sharedPreferences.getInt("mondayEnd2Min", 0);
                Calendar calendar3 = Calendar.getInstance();
                Calendar calendar4 = Calendar.getInstance();
                calendar3.set(0, 0, 0, MS2H, MS2M);
                calendar4.set(0, 0, 0, ME2H, ME2M);
                mondayStart2.setText(DateFormat.format("hh:mm aa", calendar3));
                mondayEnd2.setText(DateFormat.format("hh:mm aa", calendar4));
            }
        } else {
            monday.setChecked(false);
        }

        if (sharedPreferences.getBoolean("tuesday", false)) {
            constraintLayout9.setVisibility(View.VISIBLE);
            tuesday.setChecked(true);
            TS1H = sharedPreferences.getInt("tuesdayStartHour", 48);
            TS1M = sharedPreferences.getInt("tuesdayStartMin", 0);
            TE1H = sharedPreferences.getInt("tuesdayEndHour", 48);
            TE1M = sharedPreferences.getInt("tuesdayEndMin", 0);
            Calendar calendar5 = Calendar.getInstance();
            Calendar calendar6 = Calendar.getInstance();
            calendar5.set(0, 0, 0, TS1H, TS1M);
            calendar6.set(0, 0, 0, TE1H, TE1M);
            tuesdayStart1.setText(DateFormat.format("hh:mm aa", calendar5));
            tuesdayEnd1.setText(DateFormat.format("hh:mm aa", calendar6));
            if (sharedPreferences.getBoolean("tuesdaySlot2", false)) {
                TS2H = sharedPreferences.getInt("tuesdayStart2Hour", 48);
                TS2M = sharedPreferences.getInt("tuesdayStart2Min", 0);
                TE2H = sharedPreferences.getInt("tuesdayEnd2Hour", 48);
                TE2M = sharedPreferences.getInt("tuesdayEnd2Min", 0);
                Calendar calendar7 = Calendar.getInstance();
                Calendar calendar8 = Calendar.getInstance();
                calendar7.set(0, 0, 0, TS2H, TS2M);
                calendar8.set(0, 0, 0, TE2H, TE2M);
                tuesdayStart2.setText(DateFormat.format("hh:mm aa", calendar7));
                tuesdayEnd2.setText(DateFormat.format("hh:mm aa", calendar8));
            }
        } else {
            tuesday.setChecked(false);
        }

        if (sharedPreferences.getBoolean("wednesday", false)) {
            constraintLayout10.setVisibility(View.VISIBLE);
            wednesday.setChecked(true);
            WS1H = sharedPreferences.getInt("wednesdayStartHour", 48);
            WS1M = sharedPreferences.getInt("wednesdayStartMin", 0);
            WE1H = sharedPreferences.getInt("wednesdayEndHour", 48);
            WE1M = sharedPreferences.getInt("wednesdayEndMin", 0);
            Calendar calendar9 = Calendar.getInstance();
            Calendar calendar10 = Calendar.getInstance();
            calendar9.set(0, 0, 0, WS1H, WS1M);
            calendar10.set(0, 0, 0, WE1H, WE1M);
            wednesdayStart1.setText(DateFormat.format("hh:mm aa", calendar9));
            wednesdayEnd1.setText(DateFormat.format("hh:mm aa", calendar10));
            if (sharedPreferences.getBoolean("wednesdaySlot2", false)) {
                WS2H = sharedPreferences.getInt("wednesdayStart2Hour", 48);
                WS2M = sharedPreferences.getInt("wednesdayStart2Min", 0);
                WE2H = sharedPreferences.getInt("wednesdayEnd2Hour", 48);
                WE2M = sharedPreferences.getInt("wednesdayEnd2Min", 0);
                Calendar calendar11 = Calendar.getInstance();
                Calendar calendar12 = Calendar.getInstance();
                calendar11.set(0, 0, 0, WS2H, WS2M);
                calendar12.set(0, 0, 0, WE2H, WE2M);
                wednesdayStart2.setText(DateFormat.format("hh:mm aa", calendar11));
                wednesdayEnd2.setText(DateFormat.format("hh:mm aa", calendar12));
            }
        } else {
            wednesday.setChecked(false);
        }

        if (sharedPreferences.getBoolean("thursday", false)) {
            constraintLayout11.setVisibility(View.VISIBLE);
            thursday.setChecked(true);
            THS1H = sharedPreferences.getInt("thursdayStartHour", 48);
            THS1M = sharedPreferences.getInt("thursdayStartMin", 0);
            THE1H = sharedPreferences.getInt("thursdayEndHour", 48);
            THE1M = sharedPreferences.getInt("thursdayEndMin", 0);
            Calendar calendar13 = Calendar.getInstance();
            Calendar calendar14 = Calendar.getInstance();
            calendar13.set(0, 0, 0, THS1H, THS1M);
            calendar14.set(0, 0, 0, THE1H, THE1M);
            thursdayStart1.setText(DateFormat.format("hh:mm aa", calendar13));
            thursdayEnd1.setText(DateFormat.format("hh:mm aa", calendar14));
            if (sharedPreferences.getBoolean("thursdaySlot2", false)) {
                THS2H = sharedPreferences.getInt("thursdayStart2Hour", 48);
                THS2M = sharedPreferences.getInt("thursdayStart2Min", 0);
                THE2H = sharedPreferences.getInt("thursdayEnd2Hour", 48);
                THE2M = sharedPreferences.getInt("thursdayEnd2Min", 0);
                Calendar calendar15 = Calendar.getInstance();
                Calendar calendar16 = Calendar.getInstance();
                calendar15.set(0, 0, 0, THS2H, THS2M);
                calendar16.set(0, 0, 0, THE2H, THE2M);
                thursdayStart2.setText(DateFormat.format("hh:mm aa", calendar15));
                thursdayEnd2.setText(DateFormat.format("hh:mm aa", calendar16));
            }
        } else {
            thursday.setChecked(false);
        }

        if (sharedPreferences.getBoolean("friday", false)) {
            constraintLayout12.setVisibility(View.VISIBLE);
            friday.setChecked(true);
            FS1H = sharedPreferences.getInt("fridayStartHour", 48);
            FS1M = sharedPreferences.getInt("fridayStartMin", 0);
            FE1H = sharedPreferences.getInt("fridayEndHour", 48);
            FE1M = sharedPreferences.getInt("fridayEndMin", 0);
            Calendar calendar17 = Calendar.getInstance();
            Calendar calendar18 = Calendar.getInstance();
            calendar17.set(0, 0, 0, FS1H, FS1M);
            calendar18.set(0, 0, 0, FE1H, FE1M);
            fridayStart1.setText(DateFormat.format("hh:mm aa", calendar17));
            fridayEnd1.setText(DateFormat.format("hh:mm aa", calendar18));
            if (sharedPreferences.getBoolean("fridaySlot2", false)) {
                FS2H = sharedPreferences.getInt("fridayStart2Hour", 48);
                FS2M = sharedPreferences.getInt("fridayStart2Min", 0);
                FE2H = sharedPreferences.getInt("fridayEnd2Hour", 48);
                FE2M = sharedPreferences.getInt("fridayEnd2Min", 0);
                Calendar calendar19 = Calendar.getInstance();
                Calendar calendar20 = Calendar.getInstance();
                calendar19.set(0, 0, 0, FS2H, FS2M);
                calendar20.set(0, 0, 0, FE2H, FE2M);
                fridayStart2.setText(DateFormat.format("hh:mm aa", calendar19));
                fridayEnd2.setText(DateFormat.format("hh:mm aa", calendar20));
            }
        } else {
            friday.setChecked(false);
        }

        if (sharedPreferences.getBoolean("saturday", false)) {
            constraintLayout13.setVisibility(View.VISIBLE);
            saturday.setChecked(true);
            SS1H = sharedPreferences.getInt("saturdayStartHour", 48);
            SS1M = sharedPreferences.getInt("saturdayStartMin", 0);
            SE1H = sharedPreferences.getInt("saturdayEndHour", 48);
            SE1M = sharedPreferences.getInt("saturdayEndMin", 0);
            Calendar calendar21 = Calendar.getInstance();
            Calendar calendar22 = Calendar.getInstance();
            calendar21.set(0, 0, 0, SS1H, SS1M);
            calendar22.set(0, 0, 0, SE1H, SE1M);
            saturdayStart1.setText(DateFormat.format("hh:mm aa", calendar21));
            saturdayEnd1.setText(DateFormat.format("hh:mm aa", calendar22));
            if (sharedPreferences.getBoolean("saturdaySlot2", false)) {
                SS2H = sharedPreferences.getInt("saturdayStart2Hour", 48);
                SS2M = sharedPreferences.getInt("saturdayStart2Min", 0);
                SE2H = sharedPreferences.getInt("saturdayEnd2Hour", 48);
                SE2M = sharedPreferences.getInt("saturdayEnd2Min", 0);
                Calendar calendar23 = Calendar.getInstance();
                Calendar calendar24 = Calendar.getInstance();
                calendar23.set(0, 0, 0, SS2H, SS2M);
                calendar24.set(0, 0, 0, SE2H, SE2M);
                saturdayStart2.setText(DateFormat.format("hh:mm aa", calendar23));
                saturdayEnd2.setText(DateFormat.format("hh:mm aa", calendar24));
            }
        } else {
            saturday.setChecked(false);
        }

        if (sharedPreferences.getBoolean("sunday", false)) {
            constraintLayout14.setVisibility(View.VISIBLE);
            sunday.setChecked(true);
            SUS1H = sharedPreferences.getInt("sundayStartHour", 48);
            SUS1M = sharedPreferences.getInt("sundayStartMin", 0);
            SUE1H = sharedPreferences.getInt("sundayEndHour", 48);
            SUE1M = sharedPreferences.getInt("sundayEndMin", 0);
            Calendar calendar23 = Calendar.getInstance();
            Calendar calendar24 = Calendar.getInstance();
            calendar23.set(0, 0, 0, SUS1H, SUS1M);
            calendar24.set(0, 0, 0, SUE1H, SUE1M);
            sundayStart1.setText(DateFormat.format("hh:mm aa", calendar23));
            sundayEnd1.setText(DateFormat.format("hh:mm aa", calendar24));
            if (sharedPreferences.getBoolean("sundaySlot2", false)) {
                SUS2H = sharedPreferences.getInt("sundayStart2Hour", 48);
                SUS2M = sharedPreferences.getInt("sundayStart2Min", 0);
                SUE2H = sharedPreferences.getInt("sundayEnd2Hour", 48);
                SUE2M = sharedPreferences.getInt("sundayEnd2Min", 0);
                Calendar calendar25 = Calendar.getInstance();
                Calendar calendar26 = Calendar.getInstance();
                calendar25.set(0, 0, 0, SUS2H, SUS2M);
                calendar26.set(0, 0, 0, SUE2H, SUE2M);
                sundayStart2.setText(DateFormat.format("hh:mm aa", calendar25));
                sundayEnd2.setText(DateFormat.format("hh:mm aa", calendar26));
            }
        } else {
            sunday.setChecked(false);
        }

    }

    public void save(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!mondayCheck && !tuesdayCheck && !wednesdayCheck && !thursdayCheck && !fridayCheck &&
                !saturdayCheck && !sundayCheck) {
            Toast.makeText(this, "Please Select Atleast One Day For Online", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mondayCheck) {
            editor.putBoolean("monday", true);
            editor.apply();
            if (MS1H == 25) {
                Toast.makeText(this, "please select start time for monday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("mondayStartHour", MS1H);
                editor.putInt("mondayStartMin", MS1M);
                editor.apply();
            }
            if (ME1H == 25) {
                Toast.makeText(this, "please select end time for monday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("mondayEndHour", ME1H);
                editor.putInt("mondayEndMin", ME1M);
                editor.apply();
            }
            if (mondayTxtBtn.getVisibility() == View.GONE) {
                editor.putBoolean("mondaySlot2", true);
                editor.apply();
                if (MS2H == 25) {
                    Toast.makeText(this, "please select start time for monday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("mondayStart2Hour", MS2H);
                    editor.putInt("mondayStart2Min", MS2M);
                    editor.apply();
                }
                if (ME2H == 25) {
                    Toast.makeText(this, "please select end time for monday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("mondayEnd2Hour", MS2H);
                    editor.putInt("mondayEnd2Min", MS2M);
                    editor.apply();
                }
            }
        } else {
            editor.putBoolean("monday", false);
            editor.apply();
        }
        if (tuesdayCheck) {
            editor.putBoolean("tuesday", true);
            editor.apply();
            if (TS1H == 25) {
                Toast.makeText(this, "please select start time for tuesday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("tuesdayStartHour", TS1H);
                editor.putInt("tuesdayStartMin", TS1M);
                editor.apply();
            }
            if (TE1H == 25) {
                Toast.makeText(this, "please select end time for tuesday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("tuesdayEndHour", TE1H);
                editor.putInt("tuesdayEndMin", TE1M);
                editor.apply();
            }
            if (tuesdayTxtBtn.getVisibility() == View.GONE) {
                editor.putBoolean("tuesdaySlot2", true);
                editor.apply();
                if (TS2H == 25) {
                    Toast.makeText(this, "please select start time for tuesday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("tuesdayStart2Hour", TS2H);
                    editor.putInt("tuesdayStart2Min", TS2M);
                    editor.apply();
                }
                if (TE2H == 25) {
                    Toast.makeText(this, "please select end time for tuesday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("tuesdayEnd2Hour", TS2H);
                    editor.putInt("tuesdayEnd2Min", TS2M);
                    editor.apply();
                }
            }
        } else {
            editor.putBoolean("tuesday", false);
            editor.apply();
        }
        if (wednesdayCheck) {
            editor.putBoolean("wednesday", true);
            editor.apply();
            if (WS1H == 25) {
                Toast.makeText(this, "please select start time for wednesday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("wednesdayStartHour", WS1H);
                editor.putInt("wednesdayStartMin", WS1M);
                editor.apply();
            }
            if (WE1H == 25) {
                Toast.makeText(this, "please select end time for wednesday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("wednesdayEndHour", WE1H);
                editor.putInt("wednesdayEndMin", WE1M);
                editor.apply();
            }
            if (wednesdayTxtBtn.getVisibility() == View.GONE) {
                editor.putBoolean("wednesdaySlot2", true);
                editor.apply();
                if (WS2H == 25) {
                    Toast.makeText(this, "please select start time for wednesday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("wednesdayStart2Hour", WS2H);
                    editor.putInt("wednesdayStart2Min", WS2M);
                    editor.apply();
                }
                if (WE2H == 25) {
                    Toast.makeText(this, "please select end time for wednesday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("wednesdayEnd2Hour", WS2H);
                    editor.putInt("wednesdayEnd2Min", WS2M);
                    editor.apply();
                }
            }
        } else {
            editor.putBoolean("wednesday", false);
            editor.apply();
        }
        if (thursdayCheck) {
            editor.putBoolean("thursday", true);
            editor.apply();
            if (THS1H == 25) {
                Toast.makeText(this, "please select start time for thursday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("thursdayStartHour", THS1H);
                editor.putInt("thursdayStartMin", THS1M);
                editor.apply();
            }
            if (THE1H == 25) {
                Toast.makeText(this, "please select end time for thursday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("thursdayEndHour", THE1H);
                editor.putInt("thursdayEndMin", THE1M);
                editor.apply();
            }
            if (thursdayTxtBtn.getVisibility() == View.GONE) {
                editor.putBoolean("thursdaySlot2", true);
                editor.apply();
                if (THS2H == 25) {
                    Toast.makeText(this, "please select start time for thursday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("thursdayStart2Hour", THS2H);
                    editor.putInt("thursdayStart2Min", THS2M);
                    editor.apply();
                }
                if (THE2H == 25) {
                    Toast.makeText(this, "please select end time for thursday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("thursdayEnd2Hour", THS2H);
                    editor.putInt("thursdayEnd2Min", THS2M);
                    editor.apply();
                }
            }
        } else {
            editor.putBoolean("thursday", false);
            editor.apply();
        }
        if (fridayCheck) {
            editor.putBoolean("friday", true);
            editor.apply();
            if (FS1H == 25) {
                Toast.makeText(this, "please select start time for friday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("fridayStartHour", FS1H);
                editor.putInt("fridayStartMin", FS1M);
                editor.apply();
            }
            if (FE1H == 25) {
                Toast.makeText(this, "please select end time for friday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("fridayEndHour", FE1H);
                editor.putInt("fridayEndMin", FE1M);
                editor.apply();
            }
            if (fridayTxtBtn.getVisibility() == View.GONE) {
                editor.putBoolean("fridaySlot2", true);
                editor.apply();
                if (FS2H == 25) {
                    Toast.makeText(this, "please select start time for friday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("fridayStart2Hour", FS2H);
                    editor.putInt("fridayStart2Min", FS2M);
                    editor.apply();
                }
                if (FE2H == 25) {
                    Toast.makeText(this, "please select end time for friday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("fridayEnd2Hour", FS2H);
                    editor.putInt("fridayEnd2Min", FS2M);
                    editor.apply();
                }
            }
        } else {
            editor.putBoolean("friday", false);
            editor.apply();
        }
        if (saturdayCheck) {
            editor.putBoolean("saturday", true);
            editor.apply();
            if (SS1H == 25) {
                Toast.makeText(this, "please select start time for saturday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("saturdayStartHour", SS1H);
                editor.putInt("saturdayStartMin", SS1M);
                editor.apply();
            }
            if (SE1H == 25) {
                Toast.makeText(this, "please select end time for saturday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("saturdayEndHour", SE1H);
                editor.putInt("saturdayEndMin", SE1M);
                editor.apply();
            }
            if (saturdayTxtBtn.getVisibility() == View.GONE) {
                editor.putBoolean("saturdaySlot2", true);
                editor.apply();
                if (SS2H == 25) {
                    Toast.makeText(this, "please select start time for saturday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("saturdayStart2Hour", SS2H);
                    editor.putInt("saturdayStart2Min", SS2M);
                    editor.apply();
                }
                if (SE2H == 25) {
                    Toast.makeText(this, "please select end time for saturday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("saturdayEnd2Hour", SS2H);
                    editor.putInt("saturdayEnd2Min", SS2M);
                    editor.apply();
                }
            }
        } else {
            editor.putBoolean("saturday", false);
            editor.apply();
        }
        if (sundayCheck) {
            editor.putBoolean("sunday", true);
            editor.apply();
            if (SUS1H == 25) {
                Toast.makeText(this, "please select start time for sunday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("sundayStartHour", SUS1H);
                editor.putInt("sundayStartMin", SUS1M);
                editor.apply();
            }
            if (SUE1H == 25) {
                Toast.makeText(this, "please select end time for sunday", Toast.LENGTH_SHORT).show();
                return;
            } else {
                editor.putInt("sundayEndHour", SUE1H);
                editor.putInt("sundayEndMin", SUE1M);
                editor.apply();
            }
            if (sundayTxtBtn.getVisibility() == View.GONE) {
                editor.putBoolean("sundaySlot2", true);
                editor.apply();
                if (SUS2H == 25) {
                    Toast.makeText(this, "please select start time for sunday slot 2", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editor.putInt("sundayStart2Hour", SUS2H);
                    editor.putInt("sundayStart2Min", SUS2M);
                    editor.apply();
                }
                if (SUE2H == 25) {
                    Toast.makeText(this, "please select end time for sunday slot 2", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putInt("sundayEnd2Hour", SUS2H);
                    editor.putInt("sundayEnd2Min", SUS2M);
                    editor.apply();
                }
            }
        } else {
            editor.putBoolean("sunday", false);
            editor.apply();
        }
        happyDestroy = true;
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        editor.putBoolean("allTimeSet", true);
        editor.apply();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (!happyDestroy) {
            SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("allTimeSet", false);
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