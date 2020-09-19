package com.ayata.firebasechat.data.firebase;

import com.ayata.firebasechat.ui.auth.AuthListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class FirebaseSource {
    private FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth().getCurrentUser();
    }

    public void logout() {
        firebaseAuth().signOut();
    }

    public void login(String email, String password, AuthListener authListener) {
        firebaseAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                authListener.onComplete();
                if (task.isSuccessful()) {
                    authListener.onSuccess(task);
                } else {
                    authListener.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public void register(final String email, final String password,AuthListener authListener) {

        firebaseAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authListener.onSuccess(task);
                } else {
                    authListener.onFailure(task.getException().getMessage());
                }
            }
        });
    }

}
