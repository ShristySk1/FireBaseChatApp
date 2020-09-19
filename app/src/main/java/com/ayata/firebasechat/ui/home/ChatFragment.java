package com.ayata.firebasechat.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayata.firebasechat.Adapter.GenericAdapter;
import com.ayata.firebasechat.Adapter.UserListAdapter;
import com.ayata.firebasechat.Model.RecentChat;
import com.ayata.firebasechat.Model.User;
import com.ayata.firebasechat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatFragment extends Fragment {


    //Firebase
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    //layout components
    RecyclerView recyclerView;
    //core component
    List<RecentChat> recentChatLists;
    List<User> users;
    UserListAdapter userListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.lyt_chatRecycle);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        userListAdapter = new UserListAdapter(getContext(), new GenericAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position, Object item) {
                ChatUserFragmentDirections.ActionChatUserFragment2ToMessageFragment2 action = ChatUserFragmentDirections
                        .actionChatUserFragment2ToMessageFragment2()
                        .setUserId(users.get(position).getUserId());

                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_home_host);
                navController.navigate(action);

            }
        });
        users = new ArrayList<>();
        getRecentChat();
        return view;
    }

    private void getRecentChat() {
        recentChatLists = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("RecentChatList").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recentChatLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RecentChat recentChatList = dataSnapshot.getValue(RecentChat.class);
                    recentChatLists.add(recentChatList);
                }
                displayUsers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayUsers() {
        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    for (RecentChat chatList : recentChatLists) {
                        if (chatList.getId().equals(user.getUserId())) {
                            users.add(user);
                        }
                    }
                }
                userListAdapter.setList(users,true);
                recyclerView.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}