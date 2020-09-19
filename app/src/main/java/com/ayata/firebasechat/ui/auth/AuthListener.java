package com.ayata.firebasechat.ui.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface AuthListener {
    void onStarted();

    void onSuccess(Task<AuthResult> authResponse);

    void onFailure(String message);
    void onComplete();
}
