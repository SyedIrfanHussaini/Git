package com.osdifa.anrifo.Helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.osdifa.anrifo.R;
import com.osdifa.anrifo.ShowImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Message> mMessage;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Message> mChats) {
        this.mContext = mContext;
        this.mMessage = mChats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mMessage.get(position);
        holder.show_message.setText(message.getMessage());
        if (message.getType().equals("message")) {
            holder.relativeLayout1.setVisibility(View.VISIBLE);
            holder.relativeLayout2.setVisibility(View.GONE);
            if (position == mMessage.size()-1) {
                if (message.isIsseen()) {
                    holder.imageView.setImageResource(R.drawable.checked);
                } else {
                    holder.imageView.setImageResource(R.drawable.check);
                }
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        } else {
            holder.relativeLayout1.setVisibility(View.GONE);
            holder.relativeLayout2.setVisibility(View.VISIBLE);
            Picasso.get().load(message.getImage()).into(holder.img);
        }
        holder.img.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ShowImage.class);
            intent.putExtra("url", message.getImage());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView show_message;
        ImageView imageView, img;
        RelativeLayout relativeLayout1, relativeLayout2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            imageView = itemView.findViewById(R.id.img_seen);
            img = itemView.findViewById(R.id.msg_img);
            relativeLayout1 = itemView.findViewById(R.id.relative1);
            relativeLayout2 = itemView.findViewById(R.id.relative2);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mMessage.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
