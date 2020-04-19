package com.heckteck.birthy.DatabaseHelpers;

import android.content.Context;

import com.heckteck.birthy.Utils.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Birthday.class}, version = 5)
@TypeConverters(DateConverter.class)
public abstract class BirthdayDatabase extends RoomDatabase {

    private static BirthdayDatabase instance;

    public abstract BirthdayDao birthdayDao();

    static synchronized BirthdayDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BirthdayDatabase.class, "birthday_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
