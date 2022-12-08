package com.example.wear;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.wear.data.Session;
import com.example.wear.databinding.ActivityLoginBinding;
import java.util.Objects;

public class LoginActivity extends Activity {
    private ActivityLoginBinding binding;
    LoginActivity context;

    String isValid(String email, String password) {
        // Получение ресурсов
        String[] emails = getResources().getStringArray(R.array.Emails);
        String[] passwords = getResources().getStringArray(R.array.Passwords);

        for (int i = 0; i < emails.length; i++) {
            if (Objects.equals(email, emails[i])) {
                if (Objects.equals(password, passwords[i])) {
                    Session.getInstance().setEmail(email);
                    return "true";
                } else {
                    return "password";
                }
            }
        }

        return "email";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация
        context = this;
        EditText editTextEmail = binding.loginEditTextEmail;
        EditText editTextPassword = binding.loginEditTextPassword;
        Button buttonLogin = binding.loginButtonLogin;

        // Обработка клика по кнопке
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Функция проверки
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String resultValid = isValid(email, password);

                if (Objects.equals(resultValid, "true")) {
                    // Переход на другой активити
                    Intent myIntent = new Intent(context, MainActivity.class);
                    startActivity(myIntent);
                }
                else if (Objects.equals(resultValid, "email")) {
                    editTextEmail.setError("Incorrect email");
                    editTextEmail.setTextColor(Color.parseColor("#FF0000"));
                }
                else if (Objects.equals(resultValid, "password")) {
                    editTextPassword.setError("Incorrect password");
                    editTextPassword.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });
    }
}