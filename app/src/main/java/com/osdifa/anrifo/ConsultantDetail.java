package com.osdifa.anrifo;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anubhav.android.customdialog.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osdifa.anrifo.Helper.InternetService;
import com.osdifa.anrifo.Helper.ReviewAdapter;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class ConsultantDetail extends AppCompatActivity {

    String uid, Name, Image, Name2, Image2;
    ConstraintLayout c1, c2, c3, c4, c5, c6;
    ProgressBar progressBar;
    View makeInvisibleView;
    ImageView imageView;
    TextView name, profession, about, experience, language, ytc, YTC, monday, tuesday, wednesday;
    TextView thursday, friday, saturday, sunday, rating, noReviews, sa1, sa2, sa3, sa4, sa5, noRating;
    RoundedHorizontalProgressBar r1, r2, r3, r4, r5;
    RecyclerView recyclerView;
    DatabaseReference reference, userR;
    ImageButton back, report, search;
    RatingBar ratingBar;
    ArrayList<String> nameList;
    ArrayList<String> startsList;
    ArrayList<String> imagesList;
    ArrayList<String> commentList;
    ReviewAdapter reviewAdapter;
    Button Chat;
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
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_detail);

        dialog = new CustomDialog.Builder(ConsultantDetail.this)
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

        uid = getIntent().getStringExtra("uid");
        report = findViewById(R.id.reportBtn);
        back = findViewById(R.id.backBtn13);
        search = findViewById(R.id.searchBtn);
        progressBar = findViewById(R.id.progressBar14);
        makeInvisibleView = findViewById(R.id.makeInvisibleView3);
        c1 = findViewById(R.id.constraintTop);
        c2 = findViewById(R.id.socialConstraint);
        c3 = findViewById(R.id.aboutConstraint);
        c4 = findViewById(R.id.timeConstraint);
        c5 = findViewById(R.id.ratingConstraint);
        c6 = findViewById(R.id.constraintEnd);
        imageView = findViewById(R.id.imageView5);
        name = findViewById(R.id.Name);
        profession = findViewById(R.id.Profession);
        about = findViewById(R.id.About);
        experience = findViewById(R.id.Experience);
        language = findViewById(R.id.Language);
        ytc = findViewById(R.id.YTC);
        YTC = findViewById(R.id.ytc);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        rating = findViewById(R.id.ratingPercent);
        noReviews = findViewById(R.id.noReviews);
        noRating = findViewById(R.id.noRating);
        ratingBar = findViewById(R.id.ratingBar);
        r1 = findViewById(R.id.progress1);
        r2 = findViewById(R.id.progress2);
        r3 = findViewById(R.id.progress3);
        r4 = findViewById(R.id.progress4);
        r5 = findViewById(R.id.progress5);
        sa1 = findViewById(R.id.s1);
        sa2 = findViewById(R.id.s2);
        sa3 = findViewById(R.id.s3);
        sa4 = findViewById(R.id.s4);
        sa5 = findViewById(R.id.s5);
        Chat = findViewById(R.id.chatBtn);
        recyclerView = findViewById(R.id.reviewsRecycler);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.getUid().equals(uid)) {
            c2.setVisibility(View.GONE);
            report.setVisibility(View.GONE);
        } else {
            c2.setVisibility(View.VISIBLE);
            report.setVisibility(View.VISIBLE);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        nameList = new ArrayList<>();
        commentList = new ArrayList<>();
        startsList = new ArrayList<>();
        imagesList = new ArrayList<>();

        userR = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        userR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Name2 = snapshot.child("name").getValue(String.class);
                Image2 = snapshot.child("image").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclerView.removeAllViews();

                Picasso.get().load(snapshot.child("consultantImage").getValue().toString())
                        .resize(600, 800)
                        .centerCrop()
                        .into(imageView);
                name.setText(snapshot.child("name").getValue(String.class));
                Name = snapshot.child("name").getValue(String.class);
                Image = snapshot.child("consultantImage").getValue(String.class);
                profession.setText(snapshot.child("profession").getValue(String.class));
                about.setText(snapshot.child("about").getValue(String.class));
                experience.setText(snapshot.child("experience").getValue(String.class));
                language.setText(snapshot.child("language").getValue(String.class));
                Long totalRating = snapshot.child("totalRating").getValue(Long.class);
                Long totalReviews = snapshot.child("totalReviews").getValue(Long.class);
                double star1 = (double) snapshot.child("1star").getValue();
                double star2 = (double) snapshot.child("2star").getValue();
                double star3 = (double) snapshot.child("3star").getValue();
                double star4 = (double) snapshot.child("4star").getValue();
                double star5 = (double) snapshot.child("5star").getValue();
                String yt = snapshot.child("youTube").getValue(String.class);
                if (yt.equals("false")) {
                    ytc.setVisibility(View.GONE);
                    YTC.setVisibility(View.GONE);
                } else {
                    ytc.setText(yt);
                }
                ytc.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(yt));
                    startActivity(intent);
                });

                setTimings(sunday, "sunday", snapshot);
                setTimings(monday, "monday", snapshot);
                setTimings(tuesday, "tuesday", snapshot);
                setTimings(wednesday, "wednesday", snapshot);
                setTimings(thursday, "thursday", snapshot);
                setTimings(friday, "friday", snapshot);
                setTimings(saturday, "saturday", snapshot);

                if (totalRating == 0) {
                    rating.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                    r1.setVisibility(View.GONE);
                    r2.setVisibility(View.GONE);
                    r3.setVisibility(View.GONE);
                    r4.setVisibility(View.GONE);
                    r5.setVisibility(View.GONE);
                    sa1.setVisibility(View.GONE);
                    sa2.setVisibility(View.GONE);
                    sa3.setVisibility(View.GONE);
                    sa4.setVisibility(View.GONE);
                    sa5.setVisibility(View.GONE);
                    noRating.setVisibility(View.VISIBLE);
                } else {
                    double Rating = (5 * star5 + 4 * star4 + 3 * star3 + 2 * star2 + 1 * star1) /
                            (star1 + star2 + star3 + star4 + star5);
                    rating.setText(new DecimalFormat("##.#").format(Rating));
                    ratingBar.setRating((float) Rating);
                    rating.setVisibility(View.VISIBLE);
                    double p1 = (star1 / totalRating) * 100;
                    double p2 = (star2 / totalRating) * 100;
                    double p3 = (star3 / totalRating) * 100;
                    double p4 = (star4 / totalRating) * 100;
                    double p5 = (star5 / totalRating) * 100;
                    r1.setProgress((int) p1);
                    r2.setProgress((int) p2);
                    r3.setProgress((int) p3);
                    r4.setProgress((int) p4);
                    r5.setProgress((int) p5);
                    ratingBar.setVisibility(View.VISIBLE);
                    r1.setVisibility(View.VISIBLE);
                    r2.setVisibility(View.VISIBLE);
                    r3.setVisibility(View.VISIBLE);
                    r4.setVisibility(View.VISIBLE);
                    r5.setVisibility(View.VISIBLE);
                    sa1.setVisibility(View.VISIBLE);
                    sa2.setVisibility(View.VISIBLE);
                    sa3.setVisibility(View.VISIBLE);
                    sa4.setVisibility(View.VISIBLE);
                    sa5.setVisibility(View.VISIBLE);
                    noRating.setVisibility(View.INVISIBLE);
                }

                if (totalReviews == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noReviews.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noReviews.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConsultantDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean progress = sharedPreferences.getBoolean("progressInfinite", true);
        if (progress) {
            makeInvisibleView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                makeInvisibleView.setVisibility(View.INVISIBLE);
                next();
            }, 2000);
        }

    }

    private void next() {
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animateInfinite", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate && isAnimate) {
            back.setTranslationY(-300);
            search.setTranslationY(-300);
            report.setTranslationY(-300);
            c1.setTranslationX(500);
            c2.setTranslationX(500);
            c3.setTranslationX(500);
            c4.setTranslationX(500);
            c5.setTranslationX(500);
            c6.setTranslationX(500);

            c1.setAlpha(0);
            c2.setAlpha(0);
            c3.setAlpha(0);
            c4.setAlpha(0);
            c5.setAlpha(0);
            c6.setAlpha(0);

            back.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(100).start();
            search.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(200).start();
            report.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(300).start();
            c1.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(400).start();
            c2.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(600).start();
            c3.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(800).start();
            c4.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(1000).start();
            c5.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(1200).start();
            c6.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(1400).start();
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Reviews");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameList.clear();
                commentList.clear();
                startsList.clear();
                imagesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String comment = dataSnapshot.child("review").getValue(String.class);
                    String stars = dataSnapshot.child("stars").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String image = dataSnapshot.child("image").getValue(String.class);
                    startsList.add(stars);
                    commentList.add(comment);
                    nameList.add(name);
                    imagesList.add(image);
                }
                reviewAdapter = new ReviewAdapter(getApplicationContext(), nameList, startsList, imagesList, commentList);
                recyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConsultantDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Back(View view) {
        finish();
    }

    private void setTimings(TextView textView, String day, DataSnapshot snapshot) {
        boolean d1 = (boolean) snapshot.child("Schedule").child(day).child(day).getValue();
        if (d1) {
            Long SHour1 = (Long) snapshot.child("Schedule").child(day).child(day + "StartHour1").getValue();
            Long SMin1 = (Long) snapshot.child("Schedule").child(day).child(day + "StartMin1").getValue();
            Long EHour1 = (Long) snapshot.child("Schedule").child(day).child(day + "EndHour1").getValue();
            Long EMin1 = (Long) snapshot.child("Schedule").child(day).child(day + "EndMin1").getValue();
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            int S1 = Integer.parseInt(String.valueOf(SHour1));
            int S2 = Integer.parseInt(String.valueOf(SMin1));
            int S3 = Integer.parseInt(String.valueOf(EHour1));
            int S4 = Integer.parseInt(String.valueOf(EMin1));
            calendar1.set(0, 0, 0, S1, S2);
            calendar2.set(0, 0, 0, S3, S4);
            boolean d2 = (boolean) snapshot.child("Schedule").child(day).child(day + "Slot2").getValue();
            if (d2) {
                Long SHour2 = (Long) snapshot.child("Schedule").child(day).child(day + "StartHour2").getValue();
                Long SMin2 = (Long) snapshot.child("Schedule").child(day).child(day + "StartMin2").getValue();
                Long EHour2 = (Long) snapshot.child("Schedule").child(day).child(day + "EndHour2").getValue();
                Long EMin2 = (Long) snapshot.child("Schedule").child(day).child(day + "EndMin2").getValue();
                Calendar calendar3 = Calendar.getInstance();
                Calendar calendar4 = Calendar.getInstance();
                int S5 = Integer.parseInt(String.valueOf(SHour2));
                int S6 = Integer.parseInt(String.valueOf(SMin2));
                int S7 = Integer.parseInt(String.valueOf(EHour2));
                int S8 = Integer.parseInt(String.valueOf(EMin2));
                calendar3.set(0, 0, 0, S5, S6);
                calendar4.set(0, 0, 0, S7, S8);
                textView.setText(DateFormat.format("hh:mm aa", calendar1) + " to " +
                        DateFormat.format("hh:mm aa", calendar2) + "\nand\n" +
                        DateFormat.format("hh:mm aa", calendar3) + " to "
                        + DateFormat.format("hh:mm aa", calendar4));
            } else {
                textView.setText(DateFormat.format("hh:mm aa", calendar1) + " to " + DateFormat.format("hh:mm aa", calendar2));
            }
        } else {
            textView.setText("Not Available");
        }
    }

    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, report);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.report) {
                Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popup.inflate(R.menu.report_menu);
        popup.show();
    }

    public void chat(View view) {
        Chat.setOnClickListener(null);
        HashMap<String, Object> hashMap1 = new HashMap<>();
        HashMap<String, Object> hashMap2 = new HashMap<>();

        hashMap1.put("name", Name);
        hashMap1.put("image", Image);
        hashMap1.put("UID", uid);

        String key = uid + user.getUid();
        userR.child("Chats").child(key).updateChildren(hashMap1);

        hashMap2.put("name", Name2);
        hashMap2.put("image", Image2);
        hashMap2.put("UID", user.getUid());
        reference.child("Chats").child(key).updateChildren(hashMap2);

        makeInvisibleView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), Message.class);
            intent.putExtra("uid", uid);
            makeInvisibleView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            startActivity(intent);
        }, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(MyReceiver, mIntentFilter);
        Chat.setOnClickListener(ConsultantDetail.this::chat);
    }

    public void schedule(View view) {
        makeInvisibleView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), Problem.class);
            intent.putExtra("uid", uid);
            makeInvisibleView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            startActivity(intent);
        }, 500);
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
}