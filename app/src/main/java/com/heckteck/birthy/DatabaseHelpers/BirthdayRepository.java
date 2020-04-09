package com.heckteck.birthy.DatabaseHelpers;

import android.app.Application;
import android.os.AsyncTask;

import com.heckteck.birthy.Activities.MainActivity;

import java.util.List;

import androidx.lifecycle.LiveData;

public class BirthdayRepository {
    private BirthdayDao birthdayDao;
    private LiveData<List<Birthday>> allBirthdays;

    public BirthdayRepository(Application application) {
        BirthdayDatabase birthdayDatabase = BirthdayDatabase.getInstance(application);
        birthdayDao = birthdayDatabase.birthdayDao();
        allBirthdays = birthdayDao.getAllBirthdays();
    }

    public void insert(Birthday birthday){
        new InsertBirthdayAsyncTask(birthdayDao).execute(birthday);
    }

    public LiveData<List<Birthday>> getAllBirthdays(){
        return allBirthdays;
    }


    private static class InsertBirthdayAsyncTask extends AsyncTask<Birthday, Void, Void>{

        private BirthdayDao birthdayDao;

        public InsertBirthdayAsyncTask(BirthdayDao birthdayDao) {
            this.birthdayDao = birthdayDao;
        }

        @Override
        protected Void doInBackground(Birthday... birthdays) {
            birthdayDao.insert(birthdays[0]);
            return null;
        }
    }

}
