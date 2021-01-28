package com.osdifa.anrifo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anubhav.android.customdialog.CustomDialog;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView profileImage;
    TextView profileName;
    DatabaseReference reference;
    String name, image, number;
    FirebaseUser user;
    FirebaseAuth auth;
    boolean pending;
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    ProgressBar progressBar;
    Button consultantBtn, editBtn, helpBtn, logoutBtn;
    View makeInvisibleView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container, false);

        profileImage = root.findViewById(R.id.profileImage);
        profileName = root.findViewById(R.id.profileName);
        consultantBtn = root.findViewById(R.id.consultantBtn);
        editBtn = root.findViewById(R.id.editBtn);
        helpBtn = root.findViewById(R.id.helpBtn);
        logoutBtn = root.findViewById(R.id.logoutBtn);
        progressBar = root.findViewById(R.id.progressBar6);
        makeInvisibleView = root.findViewById(R.id.makeInvisibleView1);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue().toString();
                image = snapshot.child("image").getValue().toString();
                number = snapshot.child("number").getValue().toString();
                String IsConsultant = snapshot.child("isConsultant").getValue().toString();
                switch (IsConsultant) {
                    case "true":
                        consultantBtn.setVisibility(View.GONE);
                        break;
                    case "False":
                        consultantBtn.setVisibility(View.VISIBLE);
                        break;
                    case "pending":
                        pending = true;
                        break;
                }
                if (!image.equals("Default")) {
                    Picasso.get().load(image).into(profileImage);
                }
                profileName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean progress = sharedPreferences.getBoolean("progress1", true);
        if (progress) {
            makeInvisibleView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                makeInvisibleView.setVisibility(View.INVISIBLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("progress1", false);
                editor.apply();
                next();
            }, 2000);
        } else {
            next();
        }

        return root;
    }

    private void next() {
        showAnimation();

        helpBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), Help.class)));
        editBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfile.class)));
        consultantBtn.setOnClickListener(v -> {
            if (pending) {
                Toast.makeText(getActivity(), "You have Already Applied For Become An Consultant", Toast.LENGTH_SHORT).show();
                return;
            }
            if (number.equals("null")) {
                new CustomDialog.Builder(getActivity())
                        .setTitle("Number Verification")
                        .setContent("Please verify your mobile number first.", 3)
                        .setBtnConfirmText("Verify")
                        .setBtnConfirmTextColor("#0C4C82")
                        .setBtnCancelText("Cancel")
                        .setBtnCancelTextColor("#0C4C82")
                        .setCancelable(true)
                        .onConfirm((dialog, which) -> startActivity(new Intent(getActivity(), NumberVerification.class)))
                        .onCancel((dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                Intent intent = new Intent(getActivity(), BecomeConsultant.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(v -> {
            SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
            boolean isGoogle = sharedPreferences1.getBoolean("isGoogle", false);
            new CustomDialog.Builder(getActivity())
                    .setTitle("Logout?")
                    .setContent("Do you really want to Logout?", 3)
                    .setBtnConfirmText("Yes")
                    .setBtnConfirmTextColor("#0C4C82")
                    .setBtnCancelText("No")
                    .setBtnCancelTextColor("#0C4C82")
                    .setCancelable(true)
                    .onConfirm((dialog, which) -> {
                        if (isGoogle) {
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                        } else {
                            LoginManager.getInstance().logOut();
                        }
                        auth.signOut();
                        Intent intent = new Intent(getActivity(), IntroductoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        dialog.dismiss();
                    })
                    .onCancel((dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void showAnimation() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("animate1", true);
        boolean isAnimate = sharedPreferences.getBoolean("showAnimation", false);
        if (isAnimate) {
            if (animate) {
                profileImage.setTranslationY(300);
                profileName.setTranslationY(300);
                consultantBtn.setTranslationX(300);
                editBtn.setTranslationX(300);
                helpBtn.setTranslationX(300);
                logoutBtn.setTranslationX(300);

                profileImage.setAlpha(0);
                profileName.setAlpha(0);
                consultantBtn.setAlpha(0);
                editBtn.setAlpha(0);
                helpBtn.setAlpha(0);
                logoutBtn.setAlpha(0);

                profileImage.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(200).start();
                profileName.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(400).start();
                consultantBtn.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
                editBtn.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(400).start();
                helpBtn.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(600).start();
                logoutBtn.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(800).start();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("animate1", false);
                editor.apply();
            }
        }
    }
}


