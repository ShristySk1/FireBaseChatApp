package com.ayata.firebasechat.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import com.ayata.firebasechat.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

    }
    public void onLoginClick(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_registerFragment_to_loginFragment);
//        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
    public void onRegisterClick(View view) {
//        AppUtils.nextActivity(getContext(), RegisterActivity.class);
//        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_loginFragment_to_registerFragment);
    }
}