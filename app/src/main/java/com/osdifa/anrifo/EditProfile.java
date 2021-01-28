package com.osdifa.anrifo;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.anubhav.android.customdialog.CustomDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.osdifa.anrifo.Helper.InternetService;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class  EditProfile extends AppCompatActivity {

    CircleImageView imageView;
    EditText editText;
    String image;
    ChoosePhotoHelper choosePhotoHelper;
    ProgressBar progressBar;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    DatabaseReference reference;
    ImageButton back;
    Button button, changeBtn;
    Uri uri = null;
    byte[] final_image1;
    ByteArrayOutputStream byteArrayOutputStream;
    boolean isImageAdded = false;
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
        setContentView(R.layout.activity_edit_profile);

        dialog = new CustomDialog.Builder(EditProfile.this)
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

        imageView = findViewById(R.id.editImage);
        editText = findViewById(R.id.editName);
        progressBar = findViewById(R.id.progressBar7);
        back = findViewById(R.id.backBtn);
        button = findViewById(R.id.button2);
        changeBtn = findViewById(R.id.changeBtn);
        byteArrayOutputStream = new ByteArrayOutputStream();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String Name = snapshot.child("name").getValue().toString();
                image = snapshot.child("image").getValue().toString();

                if (!image.equals("Default")) {
                    Picasso.get().load(image).into(imageView);
                }
                editText.setText(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate2", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);

        if (isAnimate) {
            if (animate) {
                back.setTranslationY(-300);
                imageView.setTranslationX(300);
                editText.setTranslationX(300);
                button.setTranslationX(300);
                changeBtn.setTranslationX(300);

                imageView.setAlpha(0);
                editText.setAlpha(0);
                button.setAlpha(0);
                changeBtn.setAlpha(0);

                back.animate().alpha(1).translationY(0).setDuration(500).setStartDelay(400).start();
                imageView.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(200).start();
                button.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(400).start();
                editText.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(600).start();
                changeBtn.animate().alpha(1).translationX(0).setDuration(500).setStartDelay(800).start();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("animate2", false);
                editor.apply();
            }
        }

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");

        choosePhotoHelper = ChoosePhotoHelper.with(EditProfile.this)
                .asUri()
                .build(uri1 -> {
                    uri = uri1;
                    if (!(uri == null)) {
                        isImageAdded = true;
                    }
                    imageView.setImageURI(uri);
                });

    }

    public void changeImage(View view) {
        choosePhotoHelper.showChooser();
    }

    public Boolean checkName() {
        String value = editText.getText().toString().trim();

        if (value.isEmpty()) {
            editText.setError("Please Enter Your Name!");
            return false;
        } else if (value.length() > 23) {
            editText.setError("Name Is Too Long");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public void Back(View view) {
        finish();
    }

    public void change(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (!checkName()) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        String name = editText.getText().toString();
        final String key = reference.push().getKey();

        if (!isImageAdded) {
            reference.child("name").setValue(name);
            Toast.makeText(EditProfile.this, "Details Changed", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Bitmap compress1 = null;
            try {
                compress1 = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(300)
                        .setQuality(50)
                        .compressToBitmap(new File(uri.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            compress1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            final_image1 = byteArrayOutputStream.toByteArray();
            SharedPreferences sharedPreferences = getSharedPreferences("Userdata", MODE_PRIVATE);
            boolean isGoogle = sharedPreferences.getBoolean("isGoogle", false);
            boolean isGoogleImage = sharedPreferences.getBoolean("isGoogleImage", isGoogle);

            if (image.equals("Default") | isGoogleImage) {
                UploadTask upload1 = storageReference.child(key + ".jpg").putBytes(final_image1);
                upload1.addOnSuccessListener(taskSnapshot -> storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> reference
                        .child("image")
                        .setValue(uri.toString())
                        .addOnSuccessListener(
                                aVoid -> {
                                    reference.child("name").setValue(name);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isGoogleImage", false);
                                    editor.apply();
                                    Toast.makeText(EditProfile.this, "Details Changed", Toast.LENGTH_SHORT).show();
                                    finish();
                                })));
            } else if (!isGoogleImage) {
                StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(image);
                storageReference1.delete().addOnSuccessListener(aVoid -> {
                    UploadTask upload1 = storageReference.child(key + ".jpg").putBytes(final_image1);
                    upload1.addOnSuccessListener(taskSnapshot -> storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> reference
                            .child("image")
                            .setValue(uri.toString())
                            .addOnSuccessListener(
                                    aVoid1 -> {
                                        reference.child("name").setValue(name);
                                        Toast.makeText(EditProfile.this, "Details Changed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })));
                });
            }
        }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        choosePhotoHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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