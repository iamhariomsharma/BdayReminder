package com.heckteck.birthy.DatabaseHelpers;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BirthdayDao {

    @Insert
    void insert(Birthday birthday);

    @Query("SELECT * FROM birthdays_table ORDER BY date ASC")
    LiveData<List<Birthday>> getAllBirthdays();

    @Query("SELECT * FROM birthdays_table ORDER BY date DESC")
    LiveData<List<Birthday>> getBirthdayByDateAsc();

    @Query("SELECT * FROM birthdays_table ORDER BY name DESC")
    LiveData<List<Birthday>> getBirthdayByNameDesc();

    @Query("SELECT * FROM birthdays_table ORDER BY name ASC")
    LiveData<List<Birthday>> getBirthdayByNameAsc();

    @Query("SELECT * FROM birthdays_table WHERE id=:birthdayId")
    LiveData<Birthday> getBirthdayById(int birthdayId);

    @Update
    void updateBirthday(Birthday birthday);

    @Delete
    void deleteBirthday(Birthday birthday);


}
