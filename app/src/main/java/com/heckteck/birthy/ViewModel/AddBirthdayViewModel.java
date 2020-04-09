package com.heckteck.birthy.ViewModel;

import android.app.Application;

import com.heckteck.birthy.Adapters.BirthdayAdapter;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.DatabaseHelpers.BirthdayRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddBirthdayViewModel extends AndroidViewModel {
    private BirthdayRepository birthdayRepository;

    public AddBirthdayViewModel(@NonNull Application application) {
        super(application);
        birthdayRepository = new BirthdayRepository(application);
    }

    public void insert(Birthday birthday){
        birthdayRepository.insert(birthday);
    }
}
