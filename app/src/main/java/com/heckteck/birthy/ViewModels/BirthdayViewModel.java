package com.heckteck.birthy.ViewModels;

import android.app.Application;

import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.DatabaseHelpers.BirthdayRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BirthdayViewModel extends AndroidViewModel {

    private BirthdayRepository birthdayRepository;
    private LiveData<List<Birthday>> allBirthdays;
    private LiveData<List<Birthday>> birthdaysByNameDesc;
    private LiveData<List<Birthday>> birthdaysByNameAsc;
    private LiveData<List<Birthday>> birthdaysByDateAsc;

    public BirthdayViewModel(@NonNull Application application) {
        super(application);
        birthdayRepository = new BirthdayRepository(application);
        allBirthdays = birthdayRepository.getAllBirthdays();
        birthdaysByNameDesc = birthdayRepository.getBirthdaysByNameDesc();
        birthdaysByNameAsc = birthdayRepository.getBirthdaysByNameAsc();
        birthdaysByDateAsc = birthdayRepository.getBirthdayByDateAsc();
    }


    public LiveData<List<Birthday>> getAllBirthdays(){
        return allBirthdays;
    }

    public LiveData<List<Birthday>> getBirthdaysByNameDesc(){
        return birthdaysByNameDesc;
    }

    public LiveData<List<Birthday>> getBirthdaysByNameAsc(){
        return birthdaysByNameAsc;
    }

    public LiveData<List<Birthday>> getBirthdaysByDateAsc(){
        return birthdaysByDateAsc;
    }

}
