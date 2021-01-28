package com.anrifo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Reject extends AppCompatActivity {

    EditText editText;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject);

        editText = findViewById(R.id.editText);
        UID = getIntent().getStringExtra("UID");

    }

    public void RejectFinish(View view) {
        if (!editText.getText().toString().isEmpty()) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(UID);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("RejectReason", editText.getText().toString());
            hashMap.put("isConsultant", "Rejected");
            reference.updateChildren(hashMap).addOnCompleteListener(task -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(Reject.this, "Rejected", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Please Enter Reason", Toast.LENGTH_SHORT).show();
        }
    }
}