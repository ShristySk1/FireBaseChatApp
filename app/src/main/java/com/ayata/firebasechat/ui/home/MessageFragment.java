package com.ayata.firebasechat.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayata.firebasechat.Adapter.GenericAdapter;
import com.ayata.firebasechat.Adapter.MessageListAdapter;
import com.ayata.firebasechat.Model.Chat;
import com.ayata.firebasechat.Model.RecentChat;
import com.ayata.firebasechat.Model.User;
import com.ayata.firebasechat.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessageFragment extends Fragment {
    ImageButton back;
    ImageButton send;
    CircleImageView img_receiver;
    TextView txt_receivername;
    RecyclerView recyclerView;
    MessageListAdapter messageListAdapter;
    List<Chat> chatList;
    List<RecentChat> recentChatLists;
    String userId;
    //Firebase
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ValueEventListener valueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        img_receiver = view.findViewById(R.id.img_receiver);
        txt_receivername = view.findViewById(R.id.txt_receiver);
        recyclerView = view.findViewById(R.id.lyt_messageRecycle);
        back = view.findViewById(R.id.imgbtn_back);
        send = view.findViewById(R.id.imgbtn_send);
        setRecyclerMessage();
        TextInputLayout layout_message = view.findViewById(R.id.lyt_writeMessage);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        userId = MessageFragmentArgs.fromBundle(getArguments()).getUserId();

        if (userId != null) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    txt_receivername.setText(user.getUserName());
                    if (user.getImgUrl().equals("default")) {
                        img_receiver.setImageResource(R.drawable.ic_launcher_background);
                    } else {
                        //Glide
                        Glide.with(getContext()).load(user.getImgUrl()).into(img_receiver);

                    }
                    readMessage(firebaseUser.getUid(), userId, user.getImgUrl());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = layout_message.getEditText().getText().toString();
                layout_message.getEditText().setText("");
                sendMessage(firebaseUser.getUid(), userId, message);

            }
        });
        return view;
    }

    private void setUpSeenListener() {
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Chats");
        valueEventListener = databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("seen", true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void postToRecentChatList() {
        recentChatLists = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RecentChatList")
                .child(firebaseUser.getUid())
                .child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    databaseReference.setValue(new RecentChat(userId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(String sender, String receiver, String message) {
        Chat chat = new Chat(sender, receiver, message, false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Chats").push().setValue(chat);
        postToRecentChatList();

    }

    private void setRecyclerMessage() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        messageListAdapter = new MessageListAdapter(getContext(), new GenericAdapter.OnViewHolderClick<Chat>() {
            @Override
            public void onClick(View view, int position, Chat item) {

            }
        });

    }

    public void readMessage(String sender, String receiver, String imageUrl) {
        chatList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getSender().equals(sender) && chat.getReceiver().equals(receiver) ||
                            chat.getSender().equals(receiver) && chat.getReceiver().equals(sender)) {
                        chatList.add(chat);
                    }
                    messageListAdapter.setList(chatList);
                    messageListAdapter.setImageUrl(imageUrl);
                    recyclerView.setAdapter(messageListAdapter);
                    recyclerView.scrollToPosition(chatList.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        setUpSeenListener();

    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onpASEofmessagecalled", "onPause: ");
        databaseReference2.removeEventListener(valueEventListener);
    }
}