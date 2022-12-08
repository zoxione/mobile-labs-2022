package com.example.thirdproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DbTownDao {
    @Query("DELETE FROM dbtown")
    void deleteTowns();

    @Query("SELECT * FROM dbtown")
    List<DbTown> getTowns();

    @Query("SELECT * FROM dbtown WHERE id = :id")
    DbTown getTownById(long id);

    @Query("SELECT * FROM dbtown WHERE name = :name")
    DbTown getTownByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTown(DbTown town);
}
