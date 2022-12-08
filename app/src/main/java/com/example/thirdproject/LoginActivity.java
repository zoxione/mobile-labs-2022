package com.example.thirdproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.thirdproject.data.Session;
import com.example.thirdproject.database.AppDatabase;
import com.example.thirdproject.database.DbUser;
import com.example.thirdproject.database.DbUserDao;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    LoginActivity context;

    List<DbUser> InitUsers() {
        // Инициализация БД
        AppDatabase dbRoom = App.getInstance().getDatabase();
        DbUserDao dbUserDao = dbRoom.dbUserDao();
        List<DbUser> usersList = dbUserDao.getUsers();
//        userDao.deleteUsers();
//
        // Получение ресурсов
        String[] emails = getResources().getStringArray(R.array.Emails);
        String[] passwords = getResources().getStringArray(R.array.Passwords);

        // Вставка данных
        if (usersList.size() == 0) {
            for (int i = 0; i < emails.length; i++) {
                DbUser dbUser = new DbUser();
                dbUser.email = emails[i];
                dbUser.password = passwords[i];
                dbUserDao.insertUser(dbUser);
            }
        }
        else {
            for (int i = 0; i < emails.length; i++) {
                for (int j = 0; j < usersList.size(); j++) {
                    if (usersList.get(j).email.equals(emails[i])) {
                        break;
                    }
                    if (j == usersList.size() - 1) {
                        DbUser dbUser = new DbUser();
                        dbUser.email = emails[i];
                        dbUser.password = passwords[i];
                        dbUserDao.insertUser(dbUser);
                    }
                }
            }
        }


        for (int i = 0; i < usersList.size(); i++) {
            Log.d("InitUsers",  "id: " + usersList.get(i).id + " login: " + usersList.get(i).login + " email: " + usersList.get(i).email + " password: " + usersList.get(i).password);
        }
        return usersList;
    };

    String isValid(String email, String password) {
        List<DbUser> usersList = InitUsers();

        for (DbUser dbUser : usersList) {
            if (Objects.equals(email, dbUser.email)) {
                if (Objects.equals(password, dbUser.password)) {
                    Session.getInstance().setEmail(email);
                    Session.getInstance().setId(dbUser.id);
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
        setContentView(R.layout.activity_login);

        // Инициализация
        context = this;
        EditText editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        Button buttonSave = (Button)findViewById(R.id.buttonLogin);

        // Обработка клика по кнопке
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Функция проверки
                String resultValid = isValid(editTextEmail.getText().toString(), editTextPassword.getText().toString());

                if (Objects.equals(resultValid, "true")) {
                    // Переход на другой активити
                    Intent myIntent = new Intent(context, MainActivity.class);
                    startActivity(myIntent);
                }
                else if (Objects.equals(resultValid, "email")) {
                    editTextEmail.setError("Incorrect email");
                }
                else if (Objects.equals(resultValid, "password")) {
                    editTextPassword.setError("Incorrect password");
                }
            }
        });
    }
}