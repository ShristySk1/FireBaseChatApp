package com.ayata.firebasechat.ui.auth;

import android.app.Application;

import com.ayata.firebasechat.data.firebase.FirebaseSource;
import com.ayata.firebasechat.data.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AuthViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(new FirebaseSource());
    }

    public void login(String email, String password, AuthListener authListener
    ) {
        userRepository.login(email, password
                , authListener);
    }

    public void register(String email, String password,
                         AuthListener authListener
    ) {
        userRepository.register(email, password, authListener);
    }
    public FirebaseUser currentUser() {
       return userRepository.currentUser();
    }

    public void logout() {
        userRepository.logout();
    }
}
