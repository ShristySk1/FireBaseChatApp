package com.ayata.firebasechat.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ayata.firebasechat.ui.home.HomeActivity;
import com.ayata.firebasechat.R;
import com.ayata.firebasechat.utils.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    //layout
    CircularProgressButton cir_btn_login;
    TextInputLayout layout_email, layout_password;

    AuthViewModel authViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
           window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        }
        initView(view);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        return view;
    }
    private void initView(View view) {
        layout_email = view.findViewById(R.id.layout_email);
        layout_password = view.findViewById(R.id.layout_password);
        cir_btn_login = view.findViewById(R.id.cir_btn_login);
        cir_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = layout_email.getEditText().getText().toString();
                String password = layout_password.getEditText().getText().toString();
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
//                    layout_email.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(getContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
//                    layout_password.requestFocus();
//                    return;
//                }

                if (!validateEmail(layout_email) | !validatePassword(layout_password)) {
                    return;
                }
                cir_btn_login.startAnimation();
                login(email, password);

            }
        });
    }

    private void login(String email, String password) {
        authViewModel.login(email, password, new AuthListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onSuccess(Task<AuthResult> authResponse) {
                updateUI(authViewModel.currentUser());
            }

            @Override
            public void onFailure(String message) {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure"+ message);
                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }

            @Override
            public void onComplete() {
                cir_btn_login.revertAnimation();
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = authViewModel.currentUser();
        if (firebaseUser != null) {
            AppUtils.nextActivity(getContext(), HomeActivity.class);
            getActivity().finish();
        }
    }
    //run time error checker
    public static boolean validateEmail(TextInputLayout textInputEmail) {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }


    public static boolean validatePassword(TextInputLayout textInputPassword) {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (textInputPassword.getEditText().getText().length() <6) {
            textInputPassword.setError("Password must contain atleast 6 characters");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }
}