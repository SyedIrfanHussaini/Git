package com.anrifo.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.ViewHolder {

    TextView name, profession;
    CircleImageView imageView;
    View v;

    public RequestAdapter(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.consultant_name);
        profession = itemView.findViewById(R.id.consultant_profession);
        imageView = itemView.findViewById(R.id.consultant_image);
        v = itemView;

    }
}
