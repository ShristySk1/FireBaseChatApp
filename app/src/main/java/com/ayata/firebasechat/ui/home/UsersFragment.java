package com.ayata.firebasechat.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayata.firebasechat.Adapter.GenericAdapter;
import com.ayata.firebasechat.Adapter.UserListAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "usersfragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //Firebase
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    //layout components
    RecyclerView recyclerView;
    //core component
    List<User> users;
    UserListAdapter userListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.lyt_userRecycle);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        userListAdapter = new UserListAdapter(getContext(), new GenericAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position, Object item) {
//                Intent intent = new Intent(getContext(), MessageActivity.class);
//                intent.putExtra("userid", users.get(position).getUserId());
//                startActivity(intent);
////                 action = LoginFragmentDirections.actionLoginFragmentToMainFragment(name)
//                Bundle bundle = new Bundle();
//                bundle.putString("userid", users.get(position).getUserId());
//                ChatUserFragment parentFrag = ((ChatUserFragment) UsersFragment.this.getParentFragment());
//                parentFrag.senddata(users.get(position).getUserId());
                ChatUserFragmentDirections.ActionChatUserFragment2ToMessageFragment2 action = ChatUserFragmentDirections
                        .actionChatUserFragment2ToMessageFragment2()
                        .setUserId(users.get(position).getUserId());

                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_home_host);
                navController.navigate(action);

            }
        });
        users = new ArrayList<>();
        getUsers();
        return view;
    }

    private void getUsers() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    Log.d(TAG, "onDataChange: " + user.getUserId());
                    if (!user.getUserId().equals(firebaseUser.getUid())) {
                        users.add(user);
                        Log.d(TAG, "onDataChange added : " + user.getUserId());
                        Log.d("checkitemstatus", "onDataChange: "+user.toString());
                    }
                }
                userListAdapter.setList(users, true);
                recyclerView.setAdapter(userListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}