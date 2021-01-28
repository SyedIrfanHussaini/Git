package com.osdifa.anrifo.Helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.osdifa.anrifo.Message;
import com.osdifa.anrifo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context mContext;
    private List<ChatModel> mChats;
    FirebaseUser firebaseUser;

    public ChatAdapter(Context mContext, List<ChatModel> mChats) {
        this.mContext = mContext;
        this.mChats = mChats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_card, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatModel chatModel = mChats.get(position);
        holder.name.setText(chatModel.getName());
        if (chatModel.getImage().equals("Default")) {
            holder.imageView.setImageResource(R.drawable.profile_image);
        } else {
            Glide.with(mContext).load(chatModel.getImage()).into(holder.imageView);
        }
        holder.v.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Message.class);
            intent.putExtra("uid", chatModel.getUID());
            mContext.startActivity(intent);
        });
        holder.delete.setOnClickListener(v -> {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            PopupMenu popup = new PopupMenu(mContext, holder.delete);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete) {
                    DatabaseReference c = FirebaseDatabase.getInstance().getReference().child("Users").child(chatModel.getUID()).child("Chats");
                    DatabaseReference u = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Chats");
                    Query query = c.orderByKey().equalTo(chatModel.getUID()+firebaseUser.getUid());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                c.child(chatModel.getUID()+firebaseUser.getUid()).removeValue();
                                u.child(chatModel.getUID()+firebaseUser.getUid()).removeValue();
                            } else {
                                c.child(firebaseUser.getUid()+chatModel.getUID()).removeValue();
                                u.child(firebaseUser.getUid()+chatModel.getUID()).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    
                    return true;
                }
                return false;
            });
            popup.inflate(R.menu.delete_menu);
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name;
        ImageButton delete;
        View v;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.chatImg);
            name = itemView.findViewById(R.id.chatName);
            delete = itemView.findViewById(R.id.deleteBtn);
            v = itemView;

        }
    }

}
