package com.osdifa.anrifo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class SignupTabFragment extends Fragment {

    EditText name, email, password, confirmPassword, number;
    Button signUp;
    String phoneNumber;
    CountryCodePicker countryCodePicker;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.SEmail);
        password = root.findViewById(R.id.SPassword);
        progressBar = root.findViewById(R.id.progressBar3);
        confirmPassword = root.findViewById(R.id.confirmPassword);
        number = root.findViewById(R.id.number);
        countryCodePicker = root.findViewById(R.id.countryCodeHolder);
        signUp = root.findViewById(R.id.SignupBtn);

        signUp.setOnClickListener(this::VerifyNumber);
        return root;
    }

    public void SignUp() {
        progressBar.setVisibility(View.VISIBLE);
        if (!checkName() | !checkEmail() | !checkPassword() | !checkConfirmPassword() | !checkNumber()) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (!isConnected(Objects.requireNonNull(getActivity()))) {
            showNetworkDialog();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        signUp.setOnClickListener(null);
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Number = "+" + countryCodePicker.getFullNumber() + number.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("email").equalTo(Email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    email.setError("This Email Already Exist");
                    progressBar.setVisibility(View.INVISIBLE);
                    signUp.setOnClickListener(v -> VerifyNumber(null));
                } else {
                    email.setError(null);
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(getActivity(), Verification.class);
                    intent.putExtra("name", Name);
                    intent.putExtra("email", Email);
                    intent.putExtra("password", Password);
                    intent.putExtra("number", Number);
                    startActivity(intent);
                    signUp.setOnClickListener(v -> VerifyNumber(null));
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                signUp.setOnClickListener(v -> SignUp());
            }
        });
    }

    public boolean checkName() {
        String var = name.getText().toString().trim();
        if (var.isEmpty()) {
            name.setError("Please Enter Your Name");
            return false;
        } else if (var.length() > 25) {
            name.setError("Name Is Too Long");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    public boolean checkPassword() {
        String var = password.getText().toString().trim();
        if (var.isEmpty()) {
            password.setError("Please Enter Your Password");
            return false;
        } else if (var.contains(" ")) {
            password.setError("Password Should Contain No White Spaces");
            return false;
        } else if (!var.matches(".*[a-zA-Z]+.*")) {
            password.setError("Password Should Contain AtLeast 1 Character");
            return false;
        } else if (!var.matches(".*\\d.*")) {
            password.setError("Password Should Contain AtLeast 1 Digit");
            return false;
        } else if (var.length() <= 7) {
            password.setError("Password Is Too Short");
            return false;
        } else if (var.length() >= 30) {
            password.setError("Password Is Too Long");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public boolean checkNumber() {
        String var = number.getText().toString().trim();

        if (var.isEmpty()) {
            number.setError("Please Enter Your Mobile Number");
            return false;
        } else if (var.length() < 4 | var.length() > 14) {
            number.setError("Invalid Mobile Number");
            return false;
        } else {
            number.setError(null);
            return true;
        }
    }

    public boolean checkConfirmPassword() {
        String var = password.getText().toString().trim();
        String var2 = confirmPassword.getText().toString().trim();
        if (var2.isEmpty()) {
            confirmPassword.setError("Please Enter Your Password");
            return false;
        } else if (!var2.equals(var)) {
            confirmPassword.setError("Password Doesn't Match");
            return false;
        } else {
            confirmPassword.setError(null);
            return true;
        }
    }

    public boolean checkEmail() {
        String var = email.getText().toString().trim();
        String emailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

        if (var.isEmpty()) {
            email.setError("Please Enter Your Email!");
            return false;
        } else if (!var.matches(emailPattern)) {
            email.setError("Invalid Email Address!");
            return false;
        } else {
            email.setError(null);
            return true;
        }
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

    public void VerifyNumber(View view) {
        progressBar.setVisibility(View.VISIBLE);
        phoneNumber = "+" + countryCodePicker.getFullNumber() + number.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("number").equalTo(phoneNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    number.setError("This Phone Number Already Exist");
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    SignUp();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
