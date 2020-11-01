package com.ayata.firebasechat.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ayata.firebasechat.Model.User;
import com.ayata.firebasechat.R;
import com.ayata.firebasechat.ui.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterFragment extends Fragment {
    AuthViewModel authViewModel;
    private static final String TAG = "RegisterActivity";
    //layout
    CircularProgressButton cir_btn_register;
    TextInputLayout layout_username, layout_email, layout_password, layout_mobileNo;
    //core components
    String userName, email, password, mobileNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        changeStatusBarColor();
        initView(view);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
//        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));

        }
    }

    private void initView(View view) {
        layout_email = view.findViewById(R.id.layout_email);
        layout_password = view.findViewById(R.id.layout_password);
        layout_username = view.findViewById(R.id.layout_username);
        layout_mobileNo = view.findViewById(R.id.layout_mobileNo);
        cir_btn_register = view.findViewById(R.id.cirRegisterButton);
        cir_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = layout_email.getEditText().getText().toString();
                password = layout_password.getEditText().getText().toString();
                userName = layout_username.getEditText().getText().toString();
                mobileNo = layout_mobileNo.getEditText().getText().toString();
//                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
//                    cir_btn_register.startAnimation();
//                    firebaseRegister(email, password);
//                } else {
//                    Toast.makeText(getContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
//                }
                if (!validateEmail(layout_email) | !validatePassword(layout_password) | !validateUsername(layout_username)) {
                    return;
                }
                cir_btn_register.startAnimation();
                firebaseRegister(email, password);
            }
        });
    }

    private void firebaseRegister(final String email, final String password) {
        authViewModel.register(email, password, new AuthListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onSuccess(Task<AuthResult> authResponse) {
                User user = new User(authViewModel.currentUser().getUid(), userName, password, mobileNo, email, "default", false);
                //Realtime database
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(authViewModel.currentUser().getUid())
                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cir_btn_register.revertAnimation();
                            Toast.makeText(getContext(), "Register Successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Register Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {
            }

            @Override
            public void onComplete() {

            }
        });
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

    public static boolean validateUsername(TextInputLayout textInputUsername) {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputUsername.setError("Username too long");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    public static boolean validatePassword(TextInputLayout textInputPassword) {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (textInputPassword.getEditText().getText().length()<6) {
            textInputPassword.setError("Password must contain atleast 6 characters");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }
}