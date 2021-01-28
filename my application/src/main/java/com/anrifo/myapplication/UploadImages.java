package com.anrifo.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UploadImages extends AppCompatActivity {

    Uri uri = null, uri2 = null, uri3 = null, uri4 = null, uri5 = null;
    boolean isImageAdded = false, isImageAdded2 = false, isImageAdded3 = false, isImageAdded4 = false, isImageAdded5 = false;
    ChoosePhotoHelper choosePhotoHelper, choosePhotoHelper2, choosePhotoHelper3, choosePhotoHelper4, choosePhotoHelper5;
    StorageReference folder;
    Button upload;
    boolean img1 = false, img2 = false, img3 = false, img4 = false, img5 = false;
    ImageView imageView, imageView2, imageView3, imageView4, imageView5;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);

        imageView = findViewById(R.id.preview1);
        imageView2 = findViewById(R.id.preview2);
        imageView3 = findViewById(R.id.preview3);
        imageView4 = findViewById(R.id.preview4);
        imageView5 = findViewById(R.id.preview5);
        upload = findViewById(R.id.upload);

        folder = FirebaseStorage.getInstance().getReference().child("HomeImages");
        reference = FirebaseDatabase.getInstance().getReference("Images");
        choosePhotoHelper = ChoosePhotoHelper.with(UploadImages.this)
                .asUri()
                .build(uri1 -> {
                    uri = uri1;
                    isImageAdded = !(uri == null);
                    imageView.setImageURI(uri1);
                });
        choosePhotoHelper2 = ChoosePhotoHelper.with(UploadImages.this)
                .asUri()
                .build(uri1 -> {
                    uri2 = uri1;
                    isImageAdded2 = !(uri2 == null);
                    imageView2.setImageURI(uri1);
                });
        choosePhotoHelper3 = ChoosePhotoHelper.with(UploadImages.this)
                .asUri()
                .build(uri1 -> {
                    uri3 = uri1;
                    isImageAdded3 = !(uri3 == null);
                    imageView3.setImageURI(uri1);
                });
        choosePhotoHelper4 = ChoosePhotoHelper.with(UploadImages.this)
                .asUri()
                .build(uri1 -> {
                    uri4 = uri1;
                    isImageAdded4 = !(uri4 == null);
                    imageView4.setImageURI(uri1);
                });
        choosePhotoHelper5 = ChoosePhotoHelper.with(UploadImages.this)
                .asUri()
                .build(uri1 -> {
                    uri5 = uri1;
                    isImageAdded5 = !(uri5 == null);
                    imageView5.setImageURI(uri1);
                });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("image1").getValue().toString()).into(imageView);
                Picasso.get().load(snapshot.child("image2").getValue().toString()).into(imageView2);
                Picasso.get().load(snapshot.child("image3").getValue().toString()).into(imageView3);
                Picasso.get().load(snapshot.child("image4").getValue().toString()).into(imageView4);
                Picasso.get().load(snapshot.child("image5").getValue().toString()).into(imageView5);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView.setOnClickListener(v -> {
            choosePhotoHelper.showChooser();
            img1 = true;
        });
        imageView2.setOnClickListener(v -> {
            choosePhotoHelper2.showChooser();
            img2 = true;
        });
        imageView3.setOnClickListener(v -> {
            choosePhotoHelper3.showChooser();
            img3 = true;
        });
        imageView4.setOnClickListener(v -> {
            choosePhotoHelper4.showChooser();
            img4 = true;
        });
        imageView5.setOnClickListener(v -> {
            choosePhotoHelper5.showChooser();
            img5 = true;
        });

        upload.setOnClickListener(v -> {
            if (!isImageAdded && !isImageAdded2 && !isImageAdded3 && !isImageAdded4 && !isImageAdded5) {
                Toast.makeText(UploadImages.this, "Please Update Atleast One Image", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isImageAdded) {
                upload(uri, "image1");
            }
            if (isImageAdded2) {
                upload(uri2, "image2");
            }
            if (isImageAdded3) {
                upload(uri3, "image3");
            }
            if (isImageAdded4) {
                upload(uri4, "image4");
            }
            if (isImageAdded5) {
                upload(uri5, "image5");
            }
        });

    }

    private void upload(Uri uri, String name) {
        StorageReference storageReference = folder.child(name);
        storageReference.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageReference.getDownloadUrl().addOnSuccessListener(url -> {
                    String ul = url.toString();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(name, ul);
                    reference.updateChildren(hashMap);
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(UploadImages.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (img1) {

            img1 = false;
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            choosePhotoHelper.onActivityResult(requestCode, resultCode, data);
        } else if (img2) {
            img2 = false;
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            choosePhotoHelper2.onActivityResult(requestCode, resultCode, data);
        } else if (img3) {
            img3 = false;
            Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
            choosePhotoHelper3.onActivityResult(requestCode, resultCode, data);
        } else if (img4) {
            img4 = false;
            Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
            choosePhotoHelper4.onActivityResult(requestCode, resultCode, data);
        } else if (img5){
            img5 = false;
            Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
            choosePhotoHelper5.onActivityResult(requestCode, resultCode, data);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper2.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper3.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper4.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper5.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        choosePhotoHelper.onSaveInstanceState(outState);
        choosePhotoHelper2.onSaveInstanceState(outState);
        choosePhotoHelper3.onSaveInstanceState(outState);
        choosePhotoHelper4.onSaveInstanceState(outState);
        choosePhotoHelper5.onSaveInstanceState(outState);
    }

}