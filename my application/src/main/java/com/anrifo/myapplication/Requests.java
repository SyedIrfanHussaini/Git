package com.anrifo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Requests extends Fragment {

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<RequestModel> options;
    FirebaseRecyclerAdapter<RequestModel, RequestAdapter> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.requests, container, false);

        recyclerView = root.findViewById(R.id.requestsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadRequests();

        return root;
    }

    private void loadRequests() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("isConsultant").equalTo("pending");
        options = new FirebaseRecyclerOptions
                .Builder<RequestModel>()
                .setQuery(query, RequestModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<RequestModel, RequestAdapter>(options) {
            @NonNull
            @Override
            public RequestAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_design, parent, false);
                return new RequestAdapter(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull RequestAdapter holder, final int position, @NonNull RequestModel model) {
                holder.name.setText(model.getName());
                holder.profession.setText(model.getProfession());
                if (model.getImage().equals("Default")) {
                    holder.imageView.setImageResource(R.drawable.profile_image);
                } else {
                    Picasso.get().load(model.getImage()).into(holder.imageView);
                }
                holder.v.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), MainActivity2.class);
                    intent.putExtra("UID", model.getUID());
                    startActivity(intent);
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


}
