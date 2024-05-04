package com.example.workcareer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton, btnRegister;
    private TextView forgotPasswordText;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login );

        TextInputLayout emailInputLayout = findViewById(R.id.email_input_layout);
        TextInputLayout passwordInputLayout = findViewById(R.id.password_input_layout);

        emailEditText = (TextInputEditText) emailInputLayout.getEditText();
        passwordEditText = (TextInputEditText) passwordInputLayout.getEditText();

        loginButton = findViewById(R.id.login_button);
        btnRegister = findViewById(R.id.btnRegister);
        forgotPasswordText = findViewById(R.id.forgot_password_text);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( LoginActivity.this, RegisterActivity.class));
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Введите адрес электронной почты");
            passwordEditText.setError("Введите пароль");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Введите пароль");
            emailEditText.setError(null);
            return;
        } else {

            progressDialog.setMessage("Пожалуйста подождите, идёт авторизация");
            progressDialog.setTitle("Авторизация");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText( LoginActivity.this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent( LoginActivity.this, HubActivity.class));
                            } else {
                                progressDialog.dismiss();
                                String errorMessage = task.getException().getMessage();
                                if (errorMessage != null && errorMessage.contains("no user record")) {
                                    Toast.makeText( LoginActivity.this, "Пользователь не зарегистрирован", Toast.LENGTH_SHORT).show();
                                    emailEditText.setError("Пользователь не зарегистрирован");
                                    passwordEditText.setError(null);
                                } else {
                                    emailEditText.setError(null);
                                    passwordEditText.setError("Неверный пароль");
                                }
                            }
                        }
                    });
        }
    }
}