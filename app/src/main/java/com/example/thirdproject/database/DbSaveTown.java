package com.example.thirdproject.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
//        foreignKeys = {
//    @ForeignKey(entity = DbTown.class, parentColumns = "id", childColumns = "town_id", onDelete = CASCADE),
//    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = CASCADE)
//}
)
public class DbSaveTown {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long town_id;
    public long user_id;
}
