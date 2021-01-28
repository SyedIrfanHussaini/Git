package com.anrifo.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Accept extends AppCompatActivity {

    EditText from, to;
    String UID;
    TextView country;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        from = findViewById(R.id.editText2);
        to = findViewById(R.id.editText3);
        country = findViewById(R.id.country);
        UID = getIntent().getStringExtra("UID");

        reference = FirebaseDatabase.getInstance().getReference("Users").child(UID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                country.setText(snapshot.child("country").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void AcceptFinish(View view) {
        if (from.getText().toString().isEmpty() | to.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Fill Both the fields", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("isConsultant", "Accepted");
            hashMap.put("start", from.getText().toString());
            hashMap.put("end", to.getText().toString());
            reference.updateChildren(hashMap);
        }
    }
}