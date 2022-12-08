package com.example.thirdproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DbSaveTownDao {
    @Query("DELETE FROM DbSaveTown")
    void deleteSaveTowns();

    @Query("SELECT * FROM DbSaveTown")
    List<DbSaveTown> getSaveTowns();

    @Query("SELECT * FROM DbSaveTown WHERE user_id = :user_id")
    List<DbSaveTown> getSaveTownsByUserId(long user_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSaveTown(DbSaveTown dbSaveTown);
}
