package com.heckteck.birthy.ViewModel;

import android.app.Application;

import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.DatabaseHelpers.BirthdayDatabase;
import com.heckteck.birthy.DatabaseHelpers.BirthdayRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class SearchViewModel extends AndroidViewModel {

    private BirthdayRepository birthdayRepository;
    private MutableLiveData<List<Birthday>> searchResults;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        birthdayRepository = new BirthdayRepository(application);
        searchResults = birthdayRepository.getSearchResults();
    }

    public MutableLiveData<List<Birthday>> getSearchResults(){
        return searchResults;
    }

    public void searchBirthday(String name){
        birthdayRepository.searchBirthday(name);
    }


}
