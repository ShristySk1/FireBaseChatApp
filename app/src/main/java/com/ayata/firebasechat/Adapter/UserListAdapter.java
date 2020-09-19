package com.ayata.firebasechat.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayata.firebasechat.Model.User;
import com.ayata.firebasechat.R;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends GenericAdapter<User> {
    public UserListAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_item, viewGroup, false);
        return view;
    }

    @Override
    protected void bindView(User item, GenericAdapter.myViewHolder viewHolder) {
        if (item != null) {
            TextView name = viewHolder.itemView.findViewById(R.id.txt_userName);
            CircleImageView image = viewHolder.itemView.findViewById(R.id.img_user);
            ImageView statusOnline = viewHolder.itemView.findViewById(R.id.image_userStatusOnline);
            ImageView statusOffline = viewHolder.itemView.findViewById(R.id.image_userStatusOffline);

            name.setText(item.getUserName());
            if (item.getImgUrl().equals("default")) {
                image.setImageResource(R.drawable.ic_launcher_background);
            } else {
                //glide
                Glide.with(viewHolder.itemView.getContext()).load(item.getImgUrl()).into(image);
            }
            if(getChat()){
                Log.d("checkitemstatus", "bindView: "+item.toString());
            if (item.getOnline()){
                statusOnline.setVisibility(View.VISIBLE);
                statusOffline.setVisibility(View.GONE);
            }else{
                statusOnline.setVisibility(View.GONE);
                statusOffline.setVisibility(View.VISIBLE);
            }
        }else{
                statusOnline.setVisibility(View.GONE);
                statusOffline.setVisibility(View.GONE);

            }
        }
    }

}
