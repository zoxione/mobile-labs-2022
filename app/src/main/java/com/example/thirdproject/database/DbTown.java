package com.example.thirdproject.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DbTown {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String country;
    public String name;
    public String language;
    public Integer population;
    public Integer square;
}
