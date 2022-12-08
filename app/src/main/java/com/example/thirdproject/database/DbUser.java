package com.example.thirdproject.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DbUser {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String login;
    public String email;
    public String password;
}
