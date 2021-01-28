package com.osdifa.anrifo.Helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.osdifa.anrifo.ConsultantDetail;
import com.osdifa.anrifo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    Context context;
    ArrayList<String> nameList;
    ArrayList<String> professionList;
    ArrayList<String> feesList;
    ArrayList<String> imagesList;
    ArrayList<String> uidList;
    int lastPosition = -1;

    public HomeAdapter(Context context, ArrayList<String> nameList, ArrayList<String> professionList, ArrayList<String> feesList, ArrayList<String> imagesList, ArrayList<String> uidList) {
        this.context = context;
        this.nameList = nameList;
        this.professionList = professionList;
        this.feesList = feesList;
        this.imagesList = imagesList;
        this.uidList = uidList;
    }

    @NonNull
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_card, parent, false);
        return new HomeAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.name.setText(nameList.get(position));
        holder.profession.setText(professionList.get(position));
        holder.fees.setText("Consulting Fees: "+feesList.get(position));
        setAnimation(holder.v, position);
        if (imagesList.get(position) != null) {
            Picasso.get().load(imagesList.get(position)).into(holder.imageView);
            // TODO: Change This
            // Glide.with(context).load(imagesList.get(position)).into(holder.imageView);
        }
        holder.v.setOnClickListener(v -> {
            Intent intent = new Intent(context, ConsultantDetail.class);
            intent.putExtra("uid", uidList.get(position));
            context.startActivity(intent);
        });
    }

    private void setAnimation(View v, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            v.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, profession, fees;
        View v;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.mainImage);
            name = itemView.findViewById(R.id.mainName);
            profession = itemView.findViewById(R.id.mainProfession);
            fees = itemView.findViewById(R.id.mainFees);
            v = itemView;

        }
    }
}
