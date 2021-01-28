package com.osdifa.anrifo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.osdifa.anrifo.Helper.ChatAdapter;
import com.osdifa.anrifo.Helper.ChatModel;
import com.osdifa.anrifo.Notification.Token;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    private List<ChatModel> mChats;
    FirebaseUser user;
    ChatAdapter chatAdapter;
    View makeInvisibleView;
    ProgressBar progressBar;
    TextView noChats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.chat_fragment, container, false);

        recyclerView = root.findViewById(R.id.chatRecycelr);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = root.findViewById(R.id.progressBar15);
        noChats = root.findViewById(R.id.noChat);
        makeInvisibleView = root.findViewById(R.id.makeInvisibleView3);

        mChats = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        readChats();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        boolean progress = sharedPreferences.getBoolean("progress3", true);
        if (progress) {
            makeInvisibleView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                makeInvisibleView.setVisibility(View.INVISIBLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("progress3", false);
                editor.apply();
            }, 2500);
        }

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return root;
    }

    private void readChats() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChats.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    mChats.add(chatModel);
                }
                chatAdapter = new ChatAdapter(getContext(), mChats);
                recyclerView.setAdapter(chatAdapter);
                if (chatAdapter.getItemCount() == 0){
                    noChats.setVisibility(View.VISIBLE);
                } else {
                    noChats.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(user.getUid()).setValue(token1);
    }

}

