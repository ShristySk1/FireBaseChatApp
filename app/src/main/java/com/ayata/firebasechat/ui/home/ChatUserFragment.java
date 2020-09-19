package com.ayata.firebasechat.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayata.firebasechat.Adapter.ViewPagerAdapter;
import com.ayata.firebasechat.Model.User;
import com.ayata.firebasechat.R;
import com.ayata.firebasechat.utils.AppUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatUserFragment extends Fragment {

    private static final String TAG ="ChatUserFragment" ;
    //layout
    TabLayout tabLayout;
    ViewPager2 viewPager;

    //Firebase
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    // tab titles
    private String[] titles = new String[]{"Chats", "Users","Profile"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat_user, container, false);
        tabLayout = view.findViewById(R.id.tablayout);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);

        setViewPager(view);
        getSavedUser();
        return view;
    }
    private void setViewPager(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter2 = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(viewPagerAdapter2);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();
    }

    private void getSavedUser() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                AppUtils.toast(getContext(), "username: " + user.getUserName()+user.getEmail());
                Log.d(TAG, "onDataChange: "+user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}