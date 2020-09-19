package com.ayata.firebasechat.data.repositories;

import com.ayata.firebasechat.data.firebase.FirebaseSource;
import com.ayata.firebasechat.ui.auth.AuthListener;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {
    FirebaseSource firebaseSource;

    public UserRepository(FirebaseSource firebaseSource) {
        this.firebaseSource = firebaseSource;
    }

    public void login(String email, String password, AuthListener authListener
    ) {
        firebaseSource.login(email, password
                , authListener);
    }

    public void register(String email, String password,
                         AuthListener authListener
    ) {
        firebaseSource.register(email, password, authListener);
    }

    public FirebaseUser currentUser() {
        return firebaseSource.getCurrentUser();
    }

    public void logout() {
        firebaseSource.logout();
    }
}
