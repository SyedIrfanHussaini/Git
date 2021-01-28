package com.osdifa.anrifo.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.osdifa.anrifo.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    Context context;
    ArrayList<String> nameList;
    ArrayList<String> startsList;
    ArrayList<String> imagesList;
    ArrayList<String> commentList;

    public ReviewAdapter(Context context, ArrayList<String> nameList, ArrayList<String> startsList, ArrayList<String> imagesList, ArrayList<String> commentList) {
        this.context = context;
        this.nameList = nameList;
        this.startsList = startsList;
        this.imagesList = imagesList;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_card, parent, false);
        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.name.setText(nameList.get(position));
        holder.comment.setText(commentList.get(position));
        holder.ratingBar.setRating(Float.parseFloat(startsList.get(position)));
        if (imagesList.get(position).equals("Default")) {
            holder.imageView.setImageResource(R.drawable.profile_image);
        } else {
            Glide.with(context).load(imagesList.get(position)).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, comment;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.reviewerImg);
            name = itemView.findViewById(R.id.reviewerName);
            comment = itemView.findViewById(R.id.reviewerComment);
            ratingBar = itemView.findViewById(R.id.ratingBar2);

        }
    }

}
