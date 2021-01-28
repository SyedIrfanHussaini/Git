package com.osdifa.anrifo;

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
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.anubhav.android.customdialog.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.osdifa.anrifo.Helper.InternetService;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class BecomeConsultant extends AppCompatActivity {

    ImageButton back;
    TextView title;
    CardView imageView;
    Button addButton;
    ImageView profileImage;
    EditText about, experience, country, YTC;
    Spinner language;
    Uri uri = null;
    Button profession, schedule, finish;
    ProgressBar progressBar;
    String Language;
    FirebaseUser user;
    boolean isImageAdded = false, ytr;
    ChoosePhotoHelper choosePhotoHelper;
    StorageReference folder;
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
        setContentView(R.layout.activity_become_consultant);

        dialog = new CustomDialog.Builder(BecomeConsultant.this)
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

        back = findViewById(R.id.backBtn1);
        title = findViewById(R.id.title1);
        imageView = findViewById(R.id.cardView);
        profileImage = findViewById(R.id.consultantImg);
        addButton = findViewById(R.id.addButton);
        about = findViewById(R.id.about);
        experience = findViewById(R.id.experience);
        country = findViewById(R.id.country);
        YTC = findViewById(R.id.YTC);
        language = findViewById(R.id.language);
        profession = findViewById(R.id.profession);
        schedule = findViewById(R.id.schedule);
        finish = findViewById(R.id.finish);
        progressBar = findViewById(R.id.progressBar9);

        user = FirebaseAuth.getInstance().getCurrentUser();

        folder = FirebaseStorage.getInstance().getReference().child("ConsultantsProfile");

        choosePhotoHelper = ChoosePhotoHelper.with(BecomeConsultant.this)
                .asUri()
                .build(uri1 -> {
                    uri = uri1;
                    if (!(uri == null)) {
                        isImageAdded = true;
                    }
                    assert uri1 != null;
                    Picasso.get()
                            .load("file:" + uri1.getPath())
                            .resize(600, 800)
                            .centerCrop()
                            .into(profileImage);
                });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter);
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Language = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate5", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);
        if (isAnimate) {
            if (animate) {
                back.setTranslationY(-300);
                title.setTranslationY(-300);
                imageView.setTranslationX(-300);
                addButton.setTranslationX(300);
                about.setTranslationX(300);
                experience.setTranslationX(300);
                country.setTranslationX(300);
                YTC.setTranslationX(300);
                language.setTranslationX(300);
                profession.setTranslationX(300);
                schedule.setTranslationX(300);
                finish.setTranslationX(300);

                title.setAlpha(0);
                imageView.setAlpha(0);
                addButton.setAlpha(0);
                experience.setAlpha(0);
                country.setAlpha(0);
                YTC.setAlpha(0);
                language.setAlpha(0);
                profession.setAlpha(0);
                schedule.setAlpha(0);
                finish.setAlpha(0);

                back.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(200).start();
                title.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(200).start();
                imageView.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(200).start();
                addButton.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(200).start();
                about.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(400).start();
                experience.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(600).start();
                country.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(800).start();
                YTC.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1000).start();
                language.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1200).start();
                profession.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1400).start();
                schedule.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(1800).start();
                finish.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(2000).start();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("animate5", false);
                editor.apply();
            }
        }
    }

    public void Back(View view) {
        finish();
    }

    public void profession(View view) {
        startActivity(new Intent(getApplicationContext(), Profession.class));
    }

    public void schedule(View view) {
        startActivity(new Intent(getApplicationContext(), Schedule.class));
    }

    public void finish(View view) {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        boolean professionSelected = sharedPreferences.getBoolean("professionSelected", false);
        boolean allTimeSet = sharedPreferences.getBoolean("allTimeSet", false);
        if (!aboutCheck() | !experienceCheck() | !languageCheck()) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (!professionSelected) {
            Toast.makeText(this, "Please Select Profession", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (!allTimeSet) {
            Toast.makeText(this, "Please Schedule Your Timing", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (!isImageAdded) {
            Toast.makeText(this, "Please Add Your Image", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (ytr) {
            if (!youtubeCheck()) {
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
        }

        String About = about.getText().toString();
        String Experience = experience.getText().toString();
        String Country = country.getText().toString();
        String ytc = YTC.getText().toString();
        String profession = sharedPreferences.getString("profession", null);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        StorageReference storageReference = folder.child(user.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("about", About);
        hashMap.put("experience", Experience);
        hashMap.put("country", Country);
        hashMap.put("price", "null");
        if (!ytc.isEmpty()) {
            hashMap.put("youTube", ytc);
        } else {
            hashMap.put("youTube", "false");
        }
        hashMap.put("language", Language);
        hashMap.put("profession", profession);
        hashMap.put("isConsultant", "pending");
        storageReference.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    hashMap.put("consultantImage", String.valueOf(uri));
                    reference.updateChildren(hashMap);
                });
            } else {
                Toast.makeText(BecomeConsultant.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });

        addToMaps("monday");
        addToMaps("tuesday");
        addToMaps("wednesday");
        addToMaps("thursday");
        addToMaps("friday");
        addToMaps("saturday");
        addToMaps("sunday");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("underVerification", true);
        editor.apply();
        Toast.makeText(this, "We Will Verify Your Details And Contact You Soon. It Will Take UpTo 24 Hours", Toast.LENGTH_LONG).show();
        finish();
    }

    private void addToMaps(String name) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        boolean day = sharedPreferences.getBoolean(name, false);
        HashMap<String, Object> hashMap = new HashMap<>();
        if (day) {
            hashMap.put(name, true);
            int startHour1 = sharedPreferences.getInt(name+"StartHour", 404);
            int startMin1 = sharedPreferences.getInt(name+"StartMin", 404);
            int endHour1 = sharedPreferences.getInt(name+"EndHour", 404);
            int endMin1 = sharedPreferences.getInt(name+"EndMin", 404);
            hashMap.put(name+"StartHour1", startHour1);
            hashMap.put(name+"StartMin1", startMin1);
            hashMap.put(name+"EndHour1", endHour1);
            hashMap.put(name+"EndMin1", endMin1);
            boolean Slot2 = sharedPreferences.getBoolean(name+"Slot2", false);
            if (Slot2) {
                hashMap.put(name+"Slot2", true);
                int startHour2 = sharedPreferences.getInt(name+"Start2Hour", 404);
                int startMin2 = sharedPreferences.getInt(name+"Start2Min", 404);
                int endHour2 = sharedPreferences.getInt(name+"End2Hour", 404);
                int endMin2 = sharedPreferences.getInt(name+"End2Min", 404);
                hashMap.put(name+"StartHour2", startHour2);
                hashMap.put(name+"StartMin2", startMin2);
                hashMap.put(name+"EndHour2", endHour2);
                hashMap.put(name+"EndMin2", endMin2);
            } else {
                hashMap.put(name+"Slot2", false);
            }
        } else {
            hashMap.put(name, false);
        }
        reference.child("Schedule").child(name).updateChildren(hashMap);
    }

    public boolean aboutCheck() {
        String About = about.getText().toString();
        if (About.isEmpty()) {
            about.setError("Please Write About YourSelf");
            return false;
        } else if (About.length() < 80) {
            about.setError("Please Write More About YourSelf");
            return false;
        } else {
            about.setError(null);
            return true;
        }
    }

    public boolean experienceCheck() {
        String Experience = experience.getText().toString();
        if (Experience.isEmpty()) {
            experience.setError("Please Write Your Experience");
            return false;
        } else {
            experience.setError(null);
            return true;
        }
    }

    public boolean languageCheck() {
        if (Language.equals("Select Language")) {
            Toast.makeText(this, "Please Select Your Language", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean youtubeCheck() {
        String ytc = YTC.getText().toString();
        if (ytc.isEmpty()) {
            YTC.setError("Please Enter Youtube Channel Link");
            return false;
        } else {
            YTC.setError(null);
            return true;
        }
    }

    public void AddImage(View view) {
        choosePhotoHelper.showChooser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        choosePhotoHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle
            outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        choosePhotoHelper.onSaveInstanceState(outState);
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
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
        String yt = sharedPreferences.getString("profession", null);
        if (yt != null) {
            if (yt.equals("Youtuber")) {
                YTC.setHint("Youtube Channel Link");
                ytr = true;
            } else {
                YTC.setHint("Youtube Channel Link (Optional)");
                ytr = false;
            }
        }
    }
}