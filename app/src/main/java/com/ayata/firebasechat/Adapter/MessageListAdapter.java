package com.ayata.firebasechat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayata.firebasechat.Model.Chat;
import com.ayata.firebasechat.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends GenericAdapter<Chat> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    FirebaseUser firebaseUser;
    String imageUrl;

    public MessageListAdapter(Context context, OnViewHolderClick<Chat> listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == MSG_TYPE_RIGHT) {
            View view = inflater.inflate(R.layout.chat_item_right, viewGroup, false);
            return view;
        } else {
            View view = inflater.inflate(R.layout.chat_item_left, viewGroup, false);
            return view;
        }

    }

    @Override
    protected void bindView(Chat item, GenericAdapter.myViewHolder viewHolder) {
        if (item != null) {
            TextView message = viewHolder.itemView.findViewById(R.id.txt_showMessage);
            CircleImageView image = viewHolder.itemView.findViewById(R.id.img_profile);

            //seen status
            TextView seenStatus = viewHolder.itemView.findViewById(R.id.text_isSeen);
            message.setText(item.getMessage());
            if (imageUrl.equals("default")) {
                image.setImageResource(R.drawable.ic_launcher_background);
            } else {
                //glide
                Glide.with(viewHolder.itemView.getContext()).load(imageUrl).into(image);
            }
            if (viewHolder.getAdapterPosition() == (getList().size() - 1)) {
                seenStatus.setVisibility(View.VISIBLE);
                if (item.getSeen()) {
                    seenStatus.setText("Seen");
                } else {
                    seenStatus.setText("Delivered");

                }
            } else {
                seenStatus.setVisibility(View.GONE);
            }
        }

    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (getList().get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
