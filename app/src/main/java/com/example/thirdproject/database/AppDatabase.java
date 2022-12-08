package com.example.thirdproject.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DbTown.class, DbSaveTown.class, DbUser.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DbTownDao dbTownDao();
    public abstract DbSaveTownDao dbSaveTownDao();
    public abstract DbUserDao dbUserDao();
}
