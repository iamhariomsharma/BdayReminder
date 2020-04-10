package com.heckteck.birthy.DatabaseHelpers;

import android.app.Application;
import android.os.AsyncTask;

import com.heckteck.birthy.Activities.MainActivity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BirthdayRepository {
    private BirthdayDao birthdayDao;
    private LiveData<List<Birthday>> allBirthdays;

    private MutableLiveData<List<Birthday>> searchResults = new MutableLiveData<>();

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

    public void searchBirthday(String name){
        QueryAsyncTask task = new QueryAsyncTask(birthdayDao);
        task.delegate = this;
        task.execute(name);
    }

    public MutableLiveData<List<Birthday>> getSearchResults(){
        return searchResults;
    }

    private static class QueryAsyncTask extends AsyncTask<String, Void, List<Birthday>>{

        private BirthdayDao birthdayDao;
        private BirthdayRepository delegate = null;

        QueryAsyncTask(BirthdayDao dao){
            birthdayDao = dao;
        }


        @Override
        protected List<Birthday> doInBackground(String... strings) {
            return birthdayDao.getSearchBirthdays(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Birthday> birthdays) {
            delegate.asyncFinished(birthdays);
        }
    }

    private void asyncFinished(List<Birthday> birthdays) {
        searchResults.setValue(birthdays);
    }

//    public LiveData<List<Birthday>> getSearchBirthday(String query){
//        return birthdayDao.getSearchBirthdays(query);
//    }


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
