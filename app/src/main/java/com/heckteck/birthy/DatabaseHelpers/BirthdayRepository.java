package com.heckteck.birthy.DatabaseHelpers;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class BirthdayRepository {
    private BirthdayDao birthdayDao;
    private LiveData<List<Birthday>> allBirthdays;
    private LiveData<List<Birthday>> birthdaysByNameDesc;
    private LiveData<List<Birthday>> birthdaysByNameAsc;
    private LiveData<List<Birthday>> birthdayByDateAsc;


    public BirthdayRepository(Application application) {
        BirthdayDatabase birthdayDatabase = BirthdayDatabase.getInstance(application);
        birthdayDao = birthdayDatabase.birthdayDao();
        allBirthdays = birthdayDao.getAllBirthdays();
        birthdaysByNameDesc = birthdayDao.getBirthdayByNameDesc();
        birthdaysByNameAsc = birthdayDao.getBirthdayByNameAsc();
        birthdayByDateAsc = birthdayDao.getBirthdayByDateAsc();
    }

    public void insert(Birthday birthday) {
        new InsertBirthdayAsyncTask(birthdayDao).execute(birthday);
    }

    public void deleteBirthday(Birthday birthday){
        new DeleteBirthdayAsyncTask(birthdayDao).execute(birthday);
    }

    public void updateBirthday(Birthday birthday){
        new UpdateBirthdayAsyncTask(birthdayDao).execute(birthday);
    }


    public LiveData<Birthday> getBirthday(int id){
        return birthdayDao.getBirthdayById(id);
    }


    public LiveData<List<Birthday>> getAllBirthdays() {
        return allBirthdays;
    }

    public LiveData<List<Birthday>> getBirthdaysByNameDesc() {
        return birthdaysByNameDesc;
    }

    public LiveData<List<Birthday>> getBirthdaysByNameAsc() {
        return birthdaysByNameAsc;
    }

    public LiveData<List<Birthday>> getBirthdayByDateAsc() {
        return birthdayByDateAsc;
    }


    private static class InsertBirthdayAsyncTask extends AsyncTask<Birthday, Void, Void> {

        private BirthdayDao birthdayDao;

        InsertBirthdayAsyncTask(BirthdayDao birthdayDao) {
            this.birthdayDao = birthdayDao;
        }

        @Override
        protected Void doInBackground(Birthday... birthdays) {
            birthdayDao.insert(birthdays[0]);
            return null;
        }
    }

    private static class DeleteBirthdayAsyncTask extends AsyncTask<Birthday, Void, Void>{

        private BirthdayDao birthdayDao;

        DeleteBirthdayAsyncTask(BirthdayDao birthdayDao) {
            this.birthdayDao = birthdayDao;
        }

        @Override
        protected Void doInBackground(Birthday... birthdays) {
            birthdayDao.deleteBirthday(birthdays[0]);
            return null;
        }
    }

    private static class UpdateBirthdayAsyncTask extends AsyncTask<Birthday, Void, Void>{

        private BirthdayDao birthdayDao;

        public UpdateBirthdayAsyncTask(BirthdayDao birthdayDao) {
            this.birthdayDao = birthdayDao;
        }

        @Override
        protected Void doInBackground(Birthday... birthdays) {
            birthdayDao.updateBirthday(birthdays[0]);
            return null;
        }
    }

}
