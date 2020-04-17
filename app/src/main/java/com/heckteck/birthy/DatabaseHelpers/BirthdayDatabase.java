package com.heckteck.birthy.DatabaseHelpers;

import android.content.Context;

import com.heckteck.birthy.Utils.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {Birthday.class}, version = 3)
@TypeConverters(DateConverter.class)
public abstract class BirthdayDatabase extends RoomDatabase {

    private static BirthdayDatabase instance;

    public abstract BirthdayDao birthdayDao();

    public static synchronized BirthdayDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BirthdayDatabase.class, "birthday_database")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(instance).execute();
//        }
//    };

//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
//
//        private BirthdayDao birthdayDao;
//
//        public PopulateDbAsyncTask(BirthdayDatabase db) {
//            birthdayDao = db.birthdayDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            birthdayDao.insert(new Birthday("Hariom", "20/1/2000", "7869647783", "Gifts", "12:00 AM", "12/1/2020", ""));
//            return null;
//        }
//    }
}
