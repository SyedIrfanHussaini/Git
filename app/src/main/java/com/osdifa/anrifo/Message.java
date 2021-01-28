package com.osdifa.anrifo;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.anubhav.android.customdialog.CustomDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.osdifa.anrifo.Helper.ChatModel;
import com.osdifa.anrifo.Helper.InternetService;
import com.osdifa.anrifo.Helper.MessageAdapter;
import com.osdifa.anrifo.Notification.Client;
import com.osdifa.anrifo.Notification.Data;
import com.osdifa.anrifo.Notification.MyResponse;
import com.osdifa.anrifo.Notification.Sender;
import com.osdifa.anrifo.Notification.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.osdifa.anrifo.MainActivity.BroadcastStringForAction;

public class Message extends AppCompatActivity {

    CircleImageView imageView;
    TextView name, status;
    EditText editText;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String uid, myName = null, key;
    MessageAdapter messageAdapter;
    List<com.osdifa.anrifo.Helper.Message> messages;
    ValueEventListener seenListener;
    RecyclerView recyclerView;
    ChoosePhotoHelper choosePhotoHelper;
    APIService apiService;
    boolean notify = false;
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
        setContentView(R.layout.activity_message);

        dialog = new CustomDialog.Builder(Message.this)
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

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        apiService = Client.getClint("https://fcm.googleapis.com/").create(APIService.class);
        createDialog();
        imageView = findViewById(R.id.profileImageMessage);
        name = findViewById(R.id.userNameChat);
        recyclerView = findViewById(R.id.messageRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        status = findViewById(R.id.status);
        editText = findViewById(R.id.text_send);
        uid = getIntent().getStringExtra("uid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        doThis();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatModel model = snapshot.getValue(ChatModel.class);
                name.setText(model.getName());
                status.setText(snapshot.child("status").getValue(String.class));
                if (model.getConsultantImage() == null) {
                    if (model.getImage().equals("Default")) {
                        imageView.setImageResource(R.drawable.profile_image);
                    } else {
                        Glide.with(getApplicationContext()).load(model.getImage()).into(imageView);
                    }
                } else {
                    Glide.with(getApplicationContext()).load(model.getConsultantImage()).into(imageView);
                }
                readMessages(firebaseUser.getUid(), uid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Handler().postDelayed(() -> seenMessage(uid),1500);

    }

    private void sendMessage(String sender, String receiver, String message) {
        doThis();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Chats").child(key);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Chats").child(key);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("type", "message");

        reference.child("Chats").push().setValue(hashMap);
        reference2.child("Chats").push().setValue(hashMap);

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myName = snapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (notify && myName != null) {
            sendNotification(receiver, myName, msg);
        }
        notify = false;

    }

    private void sendNotification(String receiver, String name, String msg) {
        DatabaseReference token = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = token.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Token token = dataSnapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.drawable.new_final_logo, name + ": " + msg,
                            "New Message", uid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(Message.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void send(View view) {
        notify = true;
        String msg = editText.getText().toString();
        if (msg.equals("")) {
            Toast.makeText(this, "Can't Send Empty Message", Toast.LENGTH_SHORT).show();
        } else {
            sendMessage(firebaseUser.getUid(), uid, msg);
            editText.setText("");
        }
    }

    private void readMessages(String myId, String userId) {
        messages = new ArrayList<>();

        doThis();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Chats").child(key).child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    com.osdifa.anrifo.Helper.Message chat = snapshot.getValue(com.osdifa.anrifo.Helper.Message.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                        messages.add(chat);
                    }

                    messageAdapter = new MessageAdapter(Message.this, messages);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("Offline");
        unregisterReceiver(MyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("Online");
        registerReceiver(MyReceiver, mIntentFilter);
        doThis();
    }

    private void status(String status) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        ref.updateChildren(hashMap);
    }

    private void seenMessage(String userId) {
        doThis();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Chats").child(key).child("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    com.osdifa.anrifo.Helper.Message chat = snapshot.getValue(com.osdifa.anrifo.Helper.Message.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addImage(View view) {
        notify = true;
        choosePhotoHelper.showChooser();
    }

    private void sendImage(String sender, String receiver, Uri uri) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Chats").child(key);
        StorageReference folder = FirebaseStorage.getInstance().getReference().child("ChatImages");
        StorageReference storageReference = folder.child(reference.push().getKey());
        storageReference.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender", sender);
                    hashMap.put("receiver", receiver);
                    hashMap.put("image", String.valueOf(uri1));
                    hashMap.put("isseen", false);
                    hashMap.put("type", "image");
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Chats").child(key);
                    reference1.child("Chats").push().setValue(hashMap);
                    reference2.child("Chats").push().setValue(hashMap);
                }).addOnFailureListener(e -> Toast.makeText(Message.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(Message.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
        final String msg = "Send image";
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myName = snapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (notify && myName != null) {
            sendNotification(receiver, myName, msg);
        }
        notify = false;
        new Handler().postDelayed(this::createDialog,1000);
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

    private void createDialog() {
        choosePhotoHelper = null;
        choosePhotoHelper = ChoosePhotoHelper.with(Message.this)
                .asUri()
                .build(uri -> {
                    if (uri != null) {
                        sendImage(firebaseUser.getUid(), uid, uri);
                    }
                });
    }

    private void doThis() {
        DatabaseReference c = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Chats");
        DatabaseReference u = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Chats");
        Query query = c.orderByKey().equalTo(uid + firebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    key = c.child(uid + firebaseUser.getUid()).getKey();
                } else {
                    key = c.child(firebaseUser.getUid() + uid).getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
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
}