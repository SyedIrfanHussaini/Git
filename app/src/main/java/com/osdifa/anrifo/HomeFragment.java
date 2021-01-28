package com.osdifa.anrifo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osdifa.anrifo.Helper.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ImageSlider slider;
    RecyclerView recyclerView;
    View makeInvisibleView;
    ScrollView scrollView;
    ProgressBar progressBar;
    DatabaseReference reference;
    FirebaseUser user;
    ArrayList<String> nameList;
    int a = 0;
    ArrayList<String> professionList;
    ArrayList<String> feesList;
    ArrayList<String> uidList;
    ArrayList<String> imagesList;
    HomeAdapter homeAdapter;
    ConstraintLayout search;
    TextView subTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment, container, false);

        slider = root.findViewById(R.id.imageSlider);
        scrollView = root.findViewById(R.id.scrollView);

        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));

        scrollView.scrollTo(0, 0);

        recyclerView = root.findViewById(R.id.mainRecyclerView);

        progressBar = root.findViewById(R.id.progressBar13);
        makeInvisibleView = root.findViewById(R.id.makeInvisibleView2);
        search = root.findViewById(R.id.search);
        subTitle = root.findViewById(R.id.subTitle6);

        nameList = new ArrayList<>();
        professionList = new ArrayList<>();
        feesList = new ArrayList<>();
        imagesList = new ArrayList<>();
        uidList = new ArrayList<>();

        search.setOnClickListener(v -> startActivity(new Intent(getActivity(), Search.class)));

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        final List<SlideModel> images = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        images.add(new SlideModel(snapshot.child("image1").getValue().toString(), "", ScaleTypes.CENTER_INSIDE));
                        images.add(new SlideModel(snapshot.child("image2").getValue().toString(), "", ScaleTypes.CENTER_INSIDE));
                        images.add(new SlideModel(snapshot.child("image3").getValue().toString(), "", ScaleTypes.CENTER_INSIDE));
                        images.add(new SlideModel(snapshot.child("image4").getValue().toString(), "", ScaleTypes.CENTER_INSIDE));
                        images.add(new SlideModel(snapshot.child("image5").getValue().toString(), "", ScaleTypes.CENTER_INSIDE));
                        slider.setImageList(images);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        LoadProfiles();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean progress = sharedPreferences.getBoolean("progress2", true);
        if (progress) {
            makeInvisibleView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                makeInvisibleView.setVisibility(View.INVISIBLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("progress2", false);
                editor.apply();
                next();
            }, 2500);
        } else {
            next();
        }

        return root;
    }

    private void LoadProfiles() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameList.clear();
                professionList.clear();
                feesList.clear();
                imagesList.clear();
                uidList.clear();
                recyclerView.removeAllViews();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.child("UID").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String isConsultant = dataSnapshot.child("isConsultant").getValue(String.class);
                    String profession = dataSnapshot.child("profession").getValue(String.class);
                    String fees = dataSnapshot.child("price").getValue(String.class);
                    String image = dataSnapshot.child("consultantImage").getValue(String.class);
                    if (isConsultant  != null && isConsultant.equals("true")) {
                        nameList.add(name);
                        professionList.add(profession);
                        feesList.add(fees);
                        uidList.add(uid);
                        imagesList.add(image);
                    }
                }
                homeAdapter = new HomeAdapter(getContext(), nameList, professionList, feesList, imagesList, uidList);
                recyclerView.setAdapter(homeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void next() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate16", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate && isAnimate) {
            search.setTranslationY(-300);
            slider.setTranslationX(500);
            recyclerView.setTranslationX(400);
            subTitle.setTranslationX(300);

            search.setAlpha(0);
            slider.setAlpha(0);
            recyclerView.setAlpha(0);
            subTitle.setAlpha(0);

            search.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(400).start();
            slider.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
            subTitle.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();
            recyclerView.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(800).start();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("animate16", false);
            editor.apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));
        scrollView.scrollTo(0, 0);
    }

}
