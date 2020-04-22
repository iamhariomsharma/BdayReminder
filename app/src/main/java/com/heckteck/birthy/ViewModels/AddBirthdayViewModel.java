package com.heckteck.birthy.ViewModels;

import android.app.Application;

import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.DatabaseHelpers.BirthdayRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AddBirthdayViewModel extends AndroidViewModel {
    private BirthdayRepository birthdayRepository;

    public AddBirthdayViewModel(@NonNull Application application) {
        super(application);
        birthdayRepository = new BirthdayRepository(application);
    }

    public void insert(Birthday birthday){
        birthdayRepository.insert(birthday);
    }

    public LiveData<Birthday> getBirthday(int id){
        return birthdayRepository.getBirthday(id);
    }

    public void updateBirthday(Birthday birthday){
        birthdayRepository.updateBirthday(birthday);
    }
}
