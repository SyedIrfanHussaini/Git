package com.osdifa.anrifo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment {

    EditText email, password;
    Button login;
    TextView forgetPassword;
    ProgressBar progressBar;
    float v = 0;
    String error1, error2, error3;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        login = root.findViewById(R.id.loginBtn);
        forgetPassword = root.findViewById(R.id.forgetPassword);
        progressBar = root.findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();

        error1 = "The email address is badly formatted.";
        error2 = "There is no user record corresponding to this identifier. The user may have been deleted.";
        error3 = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.";

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean animate = sharedPreferences.getBoolean("showAnimation", false);

        if (animate) {
            email.setTranslationX(800);
            password.setTranslationX(800);
            forgetPassword.setTranslationX(800);
            login.setTranslationX(800);

            email.setAlpha(v);
            password.setAlpha(v);
            forgetPassword.setAlpha(v);
            login.setAlpha(v);

            email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
            password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
            forgetPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
            login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        }

        forgetPassword.setOnClickListener(v -> startActivity(new Intent(getActivity(), ForgetPassword.class)));

        login.setOnClickListener(v -> SignIn());

        return root;
    }

    public void SignIn() {
        progressBar.setVisibility(View.VISIBLE);
        if (!isConnected(getActivity())) {
            showNetworkDialog();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if (Email.isEmpty()) {
            email.setError("Please enter your email address");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        } else if (Password.isEmpty()) {
            password.setError("Please enter your password");
            progressBar.setVisibility(View.INVISIBLE);
            email.setError(null);
            return;
        }
        login.setOnClickListener(null);
        password.setError(null);
        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                password.setError(null);
                email.setError(null);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            } else if (task.getException().toString().equals(error3)) {
                password.setError("Password dose not match");
                login.setOnClickListener(v -> SignIn());
                progressBar.setVisibility(View.INVISIBLE);
            } else if (task.getException().getMessage().equals(error1)) {
                email.setError("Invalid email address");
                login.setOnClickListener(v -> SignIn());
                password.setError(null);
                progressBar.setVisibility(View.INVISIBLE);
            } else if (task.getException().getMessage().equals(error2)) {
                email.setError("No such user exist");
                login.setOnClickListener(v -> SignIn());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private boolean isConnected(Context login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifiConn != null && wifiConn.isConnected() || (mobileConn != null && mobileConn.isConnected());
    }

    private void showNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("No Connection")
                .setMessage("Please connect to internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }
}
