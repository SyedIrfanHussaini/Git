package com.anrifo.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {

    ImageView imageView;
    TextView name, profession, about, experience, language, ytc, monday, tuesday, wednesday, thursday, friday, saturday, sunday, number, email, country;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView = findViewById(R.id.consultant_imageView);
        name = findViewById(R.id.consultant_nameView);
        profession = findViewById(R.id.consultant_professionView);
        about = findViewById(R.id.consultant_aboutView);
        experience = findViewById(R.id.consultant_experienceView);
        language = findViewById(R.id.consultant_languageView);
        ytc = findViewById(R.id.consultant_YTCView);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        number = findViewById(R.id.consultant_number);
        email = findViewById(R.id.consultant_email);
        country = findViewById(R.id.consultant_country);
        UID = getIntent().getStringExtra("UID");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("consultantImage").getValue().toString())
                        .resize(600, 800)
                        .centerCrop()
                        .into(imageView);
                name.setText(snapshot.child("name").getValue().toString());
                profession.setText(snapshot.child("profession").getValue().toString());
                about.setText(snapshot.child("about").getValue().toString());
                experience.setText(snapshot.child("experience").getValue().toString());
                language.setText(snapshot.child("language").getValue().toString());
                number.setText(snapshot.child("number").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
                country.setText(snapshot.child("country").getValue().toString());
                String YtC = snapshot.child("youTube").getValue().toString();
                boolean mon2 = false, tue2 = false, wed2 = false, thu2 = false, fri2 = false, sat2 = false, sun2 = false;
                if (!YtC.equals("false")) {
                    ytc.setText(snapshot.child("youtube").getValue().toString());
                } else {
                    ytc.setText("No Youtube");
                }
                boolean mon = (boolean) snapshot.child("Schedule").child("monday").child("monday").getValue();
                if (mon) {
                    mon2 = (boolean) snapshot.child("Schedule").child("monday").child("mondaySlot2").getValue();
                }
                boolean tue = (boolean) snapshot.child("Schedule").child("tuesday").child("tuesday").getValue();
                if (tue) {
                    tue2 = (boolean) snapshot.child("Schedule").child("tuesday").child("tuesdaySlot2").getValue();
                }
                boolean wed = (boolean) snapshot.child("Schedule").child("wednesday").child("wednesday").getValue();
                if (wed) {
                    wed2 = (boolean) snapshot.child("Schedule").child("wednesday").child("wednesdaySlot2").getValue();
                }
                boolean thu = (boolean) snapshot.child("Schedule").child("thursday").child("thursday").getValue();
                if (thu) {
                    thu2 = (boolean) snapshot.child("Schedule").child("thursday").child("thursdaySlot2").getValue();
                }
                boolean fri = (boolean) snapshot.child("Schedule").child("thursday").child("thursday").getValue();
                if (fri) {
                    fri2 = (boolean) snapshot.child("Schedule").child("thursday").child("thursdaySlot2").getValue();
                }
                boolean sat = (boolean) snapshot.child("Schedule").child("saturday").child("saturday").getValue();
                if (sat) {
                    sat2 = (boolean) snapshot.child("Schedule").child("saturday").child("saturdaySlot2").getValue();
                }
                boolean sun = (boolean) snapshot.child("Schedule").child("sunday").child("sunday").getValue();
                if (sun) {
                    sun2 = (boolean) snapshot.child("Schedule").child("sunday").child("sundaySlot2").getValue();
                }
                if (mon) {
                    Long Shour1 = (Long) snapshot.child("Schedule").child("monday").child("mondayStartHour1").getValue();
                    Long Smin1 = (Long) snapshot.child("Schedule").child("monday").child("mondayStartMin1").getValue();
                    Long Ehour1 = (Long) snapshot.child("Schedule").child("monday").child("mondayEndHour1").getValue();
                    Long Emin1 = (Long) snapshot.child("Schedule").child("monday").child("mondayEndMin1").getValue();
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    int S1 = Integer.parseInt(String.valueOf(Shour1));
                    int S2 = Integer.parseInt(String.valueOf(Smin1));
                    int S3 = Integer.parseInt(String.valueOf(Ehour1));
                    int S4 = Integer.parseInt(String.valueOf(Emin1));
                    calendar1.set(0, 0, 0, S1, S2);
                    calendar2.set(0, 0, 0, S3, S4);
                    if (mon2) {
                        Long Shour2 = (Long) snapshot.child("Schedule").child("monday").child("mondayStartHour2").getValue();
                        Long Smin2 = (Long) snapshot.child("Schedule").child("monday").child("mondayStartMin2").getValue();
                        Long Ehour2 = (Long) snapshot.child("Schedule").child("monday").child("mondayEndHour2").getValue();
                        Long Emin2 = (Long) snapshot.child("Schedule").child("monday").child("mondayEndMin2").getValue();
                        Calendar calendar3 = Calendar.getInstance();
                        Calendar calendar4 = Calendar.getInstance();
                        int S5 = Integer.parseInt(String.valueOf(Shour2));
                        int S6 = Integer.parseInt(String.valueOf(Smin2));
                        int S7 = Integer.parseInt(String.valueOf(Ehour2));
                        int S8 = Integer.parseInt(String.valueOf(Emin2));
                        calendar3.set(0, 0, 0, S5, S6);
                        calendar4.set(0, 0, 0, S7, S8);
                        monday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " +
                                DateFormat.format("hh:mm aa", calendar2) + "\n" +
                                DateFormat.format("hh:mm aa", calendar3) + " to "
                                + DateFormat.format("hh:mm aa", calendar4));
                    } else {
                        monday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " + DateFormat.format("hh:mm aa", calendar2));
                    }
                } else {
                    monday.setText("Holiday");
                }
                if (tue) {
                    Long Shour1 = (Long) snapshot.child("Schedule").child("tuesday").child("tuesdayStartHour1").getValue();
                    Long Smin1 = (Long) snapshot.child("Schedule").child("tuesday").child("tuesdayStartMin1").getValue();
                    Long Ehour1 = (Long) snapshot.child("Schedule").child("tuesday").child("tuesdayEndHour1").getValue();
                    Long Emin1 = (Long) snapshot.child("Schedule").child("tuesday").child("tuesdayEndMin1").getValue();
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    int S1 = Integer.parseInt(String.valueOf(Shour1));
                    int S2 = Integer.parseInt(String.valueOf(Smin1));
                    int S3 = Integer.parseInt(String.valueOf(Ehour1));
                    int S4 = Integer.parseInt(String.valueOf(Emin1));
                    calendar1.set(0, 0, 0, S1, S2);
                    calendar2.set(0, 0, 0, S3, S4);
                    if (tue2) {
                        Long Shour2 = (Long) snapshot.child("Schedule").child("tuesday").child("tuesdayStartHour2").getValue();
                        Long Smin2 = (Long) snapshot.child("Schedule").child("tuesday").child("tuesdayStartMin2").getValue();
                        Long Ehour2 = (Long) snapshot.child("Schedule").child("tuesday").child("tuesdayEndHour2").getValue();
                        Long Emin2 = (Long) snapshot.child("Schedule").child("tuesday").child("tuesdayEndMin2").getValue();
                        Calendar calendar3 = Calendar.getInstance();
                        Calendar calendar4 = Calendar.getInstance();
                        int S5 = Integer.parseInt(String.valueOf(Shour2));
                        int S6 = Integer.parseInt(String.valueOf(Smin2));
                        int S7 = Integer.parseInt(String.valueOf(Ehour2));
                        int S8 = Integer.parseInt(String.valueOf(Emin2));
                        calendar3.set(0, 0, 0, S5, S6);
                        calendar4.set(0, 0, 0, S7, S8);
                        tuesday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " +
                                DateFormat.format("hh:mm aa", calendar2) + "\n" +
                                DateFormat.format("hh:mm aa", calendar3) + " to "
                                + DateFormat.format("hh:mm aa", calendar4));
                    } else {
                        tuesday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " + DateFormat.format("hh:mm aa", calendar2));
                    }
                } else {
                    tuesday.setText("Holiday");
                }
                if (wed) {
                    Long Shour1 = (Long) snapshot.child("Schedule").child("wednesday").child("wednesdayStartHour1").getValue();
                    Long Smin1 = (Long) snapshot.child("Schedule").child("wednesday").child("wednesdayStartMin1").getValue();
                    Long Ehour1 = (Long) snapshot.child("Schedule").child("wednesday").child("wednesdayEndHour1").getValue();
                    Long Emin1 = (Long) snapshot.child("Schedule").child("wednesday").child("wednesdayEndMin1").getValue();
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    int S1 = Integer.parseInt(String.valueOf(Shour1));
                    int S2 = Integer.parseInt(String.valueOf(Smin1));
                    int S3 = Integer.parseInt(String.valueOf(Ehour1));
                    int S4 = Integer.parseInt(String.valueOf(Emin1));
                    calendar1.set(0, 0, 0, S1, S2);
                    calendar2.set(0, 0, 0, S3, S4);
                    if (wed2) {
                        Long Shour2 = (Long) snapshot.child("Schedule").child("wednesday").child("wednesdayStartHour2").getValue();
                        Long Smin2 = (Long) snapshot.child("Schedule").child("wednesday").child("wednesdayStartMin2").getValue();
                        Long Ehour2 = (Long) snapshot.child("Schedule").child("wednesday").child("wednesdayEndHour2").getValue();
                        Long Emin2 = (Long) snapshot.child("Schedule").child("wednesday").child("wednesdayEndMin2").getValue();
                        Calendar calendar3 = Calendar.getInstance();
                        Calendar calendar4 = Calendar.getInstance();
                        int S5 = Integer.parseInt(String.valueOf(Shour2));
                        int S6 = Integer.parseInt(String.valueOf(Smin2));
                        int S7 = Integer.parseInt(String.valueOf(Ehour2));
                        int S8 = Integer.parseInt(String.valueOf(Emin2));
                        calendar3.set(0, 0, 0, S5, S6);
                        calendar4.set(0, 0, 0, S7, S8);
                        wednesday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " +
                                DateFormat.format("hh:mm aa", calendar2) + "\n" +
                                DateFormat.format("hh:mm aa", calendar3) + " to "
                                + DateFormat.format("hh:mm aa", calendar4));
                    } else {
                        wednesday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " + DateFormat.format("hh:mm aa", calendar2));
                    }
                } else {
                    wednesday.setText("Holiday");
                }
                if (thu) {
                    Long Shour1 = (Long) snapshot.child("Schedule").child("thursday").child("thursdayStartHour1").getValue();
                    Long Smin1 = (Long) snapshot.child("Schedule").child("thursday").child("thursdayStartMin1").getValue();
                    Long Ehour1 = (Long) snapshot.child("Schedule").child("thursday").child("thursdayEndHour1").getValue();
                    Long Emin1 = (Long) snapshot.child("Schedule").child("thursday").child("thursdayEndMin1").getValue();
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    int S1 = Integer.parseInt(String.valueOf(Shour1));
                    int S2 = Integer.parseInt(String.valueOf(Smin1));
                    int S3 = Integer.parseInt(String.valueOf(Ehour1));
                    int S4 = Integer.parseInt(String.valueOf(Emin1));
                    calendar1.set(0, 0, 0, S1, S2);
                    calendar2.set(0, 0, 0, S3, S4);
                    if (thu2) {
                        Long Shour2 = (Long) snapshot.child("Schedule").child("thursday").child("thursdayStartHour2").getValue();
                        Long Smin2 = (Long) snapshot.child("Schedule").child("thursday").child("thursdayStartMin2").getValue();
                        Long Ehour2 = (Long) snapshot.child("Schedule").child("thursday").child("thursdayEndHour2").getValue();
                        Long Emin2 = (Long) snapshot.child("Schedule").child("thursday").child("thursdayEndMin2").getValue();
                        Calendar calendar3 = Calendar.getInstance();
                        Calendar calendar4 = Calendar.getInstance();
                        int S5 = Integer.parseInt(String.valueOf(Shour2));
                        int S6 = Integer.parseInt(String.valueOf(Smin2));
                        int S7 = Integer.parseInt(String.valueOf(Ehour2));
                        int S8 = Integer.parseInt(String.valueOf(Emin2));
                        calendar3.set(0, 0, 0, S5, S6);
                        calendar4.set(0, 0, 0, S7, S8);
                        thursday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " +
                                DateFormat.format("hh:mm aa", calendar2) + "\n" +
                                DateFormat.format("hh:mm aa", calendar3) + " to "
                                + DateFormat.format("hh:mm aa", calendar4));
                    } else {
                        thursday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " + DateFormat.format("hh:mm aa", calendar2));
                    }
                } else {
                    thursday.setText("Holiday");
                }
                if (fri) {
                    Long Shour1 = (Long) snapshot.child("Schedule").child("friday").child("fridayStartHour1").getValue();
                    Long Smin1 = (Long) snapshot.child("Schedule").child("friday").child("fridayStartMin1").getValue();
                    Long Ehour1 = (Long) snapshot.child("Schedule").child("friday").child("fridayEndHour1").getValue();
                    Long Emin1 = (Long) snapshot.child("Schedule").child("friday").child("fridayEndMin1").getValue();
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    int S1 = Integer.parseInt(String.valueOf(Shour1));
                    int S2 = Integer.parseInt(String.valueOf(Smin1));
                    int S3 = Integer.parseInt(String.valueOf(Ehour1));
                    int S4 = Integer.parseInt(String.valueOf(Emin1));
                    calendar1.set(0, 0, 0, S1, S2);
                    calendar2.set(0, 0, 0, S3, S4);
                    if (fri2) {
                        Long Shour2 = (Long) snapshot.child("Schedule").child("friday").child("fridayStartHour2").getValue();
                        Long Smin2 = (Long) snapshot.child("Schedule").child("friday").child("fridayStartMin2").getValue();
                        Long Ehour2 = (Long) snapshot.child("Schedule").child("friday").child("fridayEndHour2").getValue();
                        Long Emin2 = (Long) snapshot.child("Schedule").child("friday").child("fridayEndMin2").getValue();
                        Calendar calendar3 = Calendar.getInstance();
                        Calendar calendar4 = Calendar.getInstance();
                        int S5 = Integer.parseInt(String.valueOf(Shour2));
                        int S6 = Integer.parseInt(String.valueOf(Smin2));
                        int S7 = Integer.parseInt(String.valueOf(Ehour2));
                        int S8 = Integer.parseInt(String.valueOf(Emin2));
                        calendar3.set(0, 0, 0, S5, S6);
                        calendar4.set(0, 0, 0, S7, S8);
                        friday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " +
                                DateFormat.format("hh:mm aa", calendar2) + "\n" +
                                DateFormat.format("hh:mm aa", calendar3) + " to "
                                + DateFormat.format("hh:mm aa", calendar4));
                    } else {
                        friday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " + DateFormat.format("hh:mm aa", calendar2));
                    }
                } else {
                    friday.setText("Holiday");
                }
                if (sat) {
                    Long Shour1 = (Long) snapshot.child("Schedule").child("saturday").child("saturdayStartHour1").getValue();
                    Long Smin1 = (Long) snapshot.child("Schedule").child("saturday").child("saturdayStartMin1").getValue();
                    Long Ehour1 = (Long) snapshot.child("Schedule").child("saturday").child("saturdayEndHour1").getValue();
                    Long Emin1 = (Long) snapshot.child("Schedule").child("saturday").child("saturdayEndMin1").getValue();
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    int S1 = Integer.parseInt(String.valueOf(Shour1));
                    int S2 = Integer.parseInt(String.valueOf(Smin1));
                    int S3 = Integer.parseInt(String.valueOf(Ehour1));
                    int S4 = Integer.parseInt(String.valueOf(Emin1));
                    calendar1.set(0, 0, 0, S1, S2);
                    calendar2.set(0, 0, 0, S3, S4);
                    if (sat2) {
                        Long Shour2 = (Long) snapshot.child("Schedule").child("saturday").child("saturdayStartHour2").getValue();
                        Long Smin2 = (Long) snapshot.child("Schedule").child("saturday").child("saturdayStartMin2").getValue();
                        Long Ehour2 = (Long) snapshot.child("Schedule").child("saturday").child("saturdayEndHour2").getValue();
                        Long Emin2 = (Long) snapshot.child("Schedule").child("saturday").child("saturdayEndMin2").getValue();
                        Calendar calendar3 = Calendar.getInstance();
                        Calendar calendar4 = Calendar.getInstance();
                        int S5 = Integer.parseInt(String.valueOf(Shour2));
                        int S6 = Integer.parseInt(String.valueOf(Smin2));
                        int S7 = Integer.parseInt(String.valueOf(Ehour2));
                        int S8 = Integer.parseInt(String.valueOf(Emin2));
                        calendar3.set(0, 0, 0, S5, S6);
                        calendar4.set(0, 0, 0, S7, S8);
                        saturday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " +
                                DateFormat.format("hh:mm aa", calendar2) + "\n" +
                                DateFormat.format("hh:mm aa", calendar3) + " to "
                                + DateFormat.format("hh:mm aa", calendar4));
                    } else {
                        saturday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " + DateFormat.format("hh:mm aa", calendar2));
                    }
                } else {
                    saturday.setText("Holiday");
                }
                if (sun) {
                    Long Shour1 = (Long) snapshot.child("Schedule").child("sunday").child("sundayStartHour1").getValue();
                    Long Smin1 = (Long) snapshot.child("Schedule").child("sunday").child("sundayStartMin1").getValue();
                    Long Ehour1 = (Long) snapshot.child("Schedule").child("sunday").child("sundayEndHour1").getValue();
                    Long Emin1 = (Long) snapshot.child("Schedule").child("sunday").child("sundayEndMin1").getValue();
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    int S1 = Integer.parseInt(String.valueOf(Shour1));
                    int S2 = Integer.parseInt(String.valueOf(Smin1));
                    int S3 = Integer.parseInt(String.valueOf(Ehour1));
                    int S4 = Integer.parseInt(String.valueOf(Emin1));
                    calendar1.set(0, 0, 0, S1, S2);
                    calendar2.set(0, 0, 0, S3, S4);
                    if (sun2) {
                        Long Shour2 = (Long) snapshot.child("Schedule").child("sunday").child("sundayStartHour2").getValue();
                        Long Smin2 = (Long) snapshot.child("Schedule").child("sunday").child("sundayStartMin2").getValue();
                        Long Ehour2 = (Long) snapshot.child("Schedule").child("sunday").child("sundayEndHour2").getValue();
                        Long Emin2 = (Long) snapshot.child("Schedule").child("sunday").child("sundayEndMin2").getValue();
                        Calendar calendar3 = Calendar.getInstance();
                        Calendar calendar4 = Calendar.getInstance();
                        int S5 = Integer.parseInt(String.valueOf(Shour2));
                        int S6 = Integer.parseInt(String.valueOf(Smin2));
                        int S7 = Integer.parseInt(String.valueOf(Ehour2));
                        int S8 = Integer.parseInt(String.valueOf(Emin2));
                        calendar3.set(0, 0, 0, S5, S6);
                        calendar4.set(0, 0, 0, S7, S8);
                        sunday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " +
                                DateFormat.format("hh:mm aa", calendar2) + "\n" +
                                DateFormat.format("hh:mm aa", calendar3) + " to "
                                + DateFormat.format("hh:mm aa", calendar4));
                    } else {
                        sunday.setText(DateFormat.format("hh:mm aa", calendar1) + " to " + DateFormat.format("hh:mm aa", calendar2));
                    }
                } else {
                    sunday.setText("Holiday");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity2.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void reject(View view) {
        Intent intent = new Intent(this, Reject.class);
        intent.putExtra("UID", UID);
        startActivity(intent);
    }

    public void accept(View view) {
        Intent intent = new Intent(this, Accept.class);
        intent.putExtra("UID", UID);
        startActivity(intent);
    }
}