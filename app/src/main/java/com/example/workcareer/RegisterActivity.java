package com.example.workcareer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText inputEmail, inputPassword, inputConformpassword;
    Button btnRegister, btnEnter;
    String emailPattetn = "^[a-zA-Z0-9._-]+@gmail\\.com$"; // Обновленный шаблон для email с "@gmail.com"
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConformpassword = findViewById(R.id.inputConformpassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnEnter = findViewById(R.id.btnEnter);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (!input.endsWith("@gmail.com")) {
                    inputEmail.setText(input + "@gmail.com");
                    inputEmail.setSelection(input.length()); // Устанавливаем курсор в конец строки
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 8) {
                    inputPassword.setError("Пароль должен быть не короче 8 символов");
                } else {
                    inputPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputConformpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = inputPassword.getText().toString();
                if (s.length() == 0) {
                    inputConformpassword.setError("Подтвердите пароль");
                } else if (!s.toString().equals(password)) {
                    inputConformpassword.setError("Пароли не совпадают");
                } else {
                    inputConformpassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( RegisterActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforAuth();
            }
        });
    }

    private void PerforAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confrimPassword = inputConformpassword.getText().toString();

        if (email.isEmpty()) {
            inputEmail.setError("Введите email");
            if (password.isEmpty()) {
                inputPassword.setError("Введите пароль");
                if (confrimPassword.isEmpty()) {
                    inputConformpassword.setError("Подтвердите пароль");
                }
            }
        } else if (!email.matches(emailPattetn)) {
            inputEmail.setError("Извините, но допускаются только латинские буквы (a-z), цифры (0-9) и точки (.) перед @gmail.com");
        } else if (password.isEmpty()) {
            inputPassword.setError("Введите пароль");
            if (confrimPassword.isEmpty()) {
                inputConformpassword.setError("Подтвердите пароль");
            }
        } else if (password.length() < 8) {
            inputPassword.setError("Пароль должен быть не короче 8 символов");
        } else if (confrimPassword.isEmpty()) {
            inputConformpassword.setError("Подтвердите пароль");
        } else if (!password.equals(confrimPassword)) {
            inputConformpassword.setError("Пароль не совпадает");
        } else {
            progressDialog.setMessage("Пожалуйста подождите, идёт регистрация");
            progressDialog.setTitle("Регистрация");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                sendUserToNextActivity();
                                Toast.makeText( RegisterActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText( RegisterActivity.this, "Пользователь с данным email уже зарегистрирован", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Не выводим стандартное сообщение об ошибке регистрации
                                }
                            }
                        }
                    });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent( RegisterActivity.this, HubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}