package com.example.thirdproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DbUserDao {
    @Query("DELETE FROM DbUser")
    void deleteUsers();

    @Query("SELECT * FROM DbUser")
    List<DbUser> getUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(DbUser dbUser);
}
