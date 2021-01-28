package com.anrifo.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Settings extends Fragment {

    TextView users, consultants;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.settings, container, false);

        users = root.findViewById(R.id.users);
        consultants = root.findViewById(R.id.consultants);

        Query reference = FirebaseDatabase.getInstance().getReference("Users").orderByChild("isConsultant").equalTo(false);
        Query reference2 = FirebaseDatabase.getInstance().getReference("Users").orderByChild("isConsultant").equalTo(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    users.setText(String.valueOf(snapshot.getChildrenCount()));
                } else {
                    users.setText("No Users");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    consultants.setText(String.valueOf(snapshot.getChildrenCount()));
                } else {
                    consultants.setText("No Consultants");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;
    }


}
