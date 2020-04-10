package com.heckteck.birthy.DatabaseHelpers;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BirthdayDao {

    @Insert
    void insert(Birthday birthday);

    @Query("SELECT * FROM birthdays_table ORDER BY date ASC")
    LiveData<List<Birthday>> getAllBirthdays();

    @Query("SELECT * FROM birthdays_table WHERE name LIKE :searchQuery || '%'")
    List<Birthday> getSearchBirthdays(String searchQuery);
}
