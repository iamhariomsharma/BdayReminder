package com.heckteck.birthy.ViewModel;

import android.app.Application;

import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.DatabaseHelpers.BirthdayRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BirthdayViewModel extends AndroidViewModel {

    private BirthdayRepository birthdayRepository;
    private LiveData<List<Birthday>> allBirthdays;

    public BirthdayViewModel(@NonNull Application application) {
        super(application);
        birthdayRepository = new BirthdayRepository(application);
        allBirthdays = birthdayRepository.getAllBirthdays();
    }


    public LiveData<List<Birthday>> getAllBirthdays(){
        return allBirthdays;
    }
}
