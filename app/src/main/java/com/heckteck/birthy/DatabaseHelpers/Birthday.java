package com.heckteck.birthy.DatabaseHelpers;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "birthdays_table")
public class Birthday {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String birthDate;
    private String date;
    private String phoneNumber;
    private String notes;
    private String timeToWish;
    private String currentDateTime;
    private String userImg;


    public Birthday(String name, String birthDate, String date, String phoneNumber, String notes, String timeToWish, String currentDateTime, String userImg) {
        this.name = name;
        this.birthDate = birthDate;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
        this.timeToWish = timeToWish;
        this.currentDateTime = currentDateTime;
        this.userImg = userImg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTimeToWish() {
        return timeToWish;
    }

    public void setTimeToWish(String timeToWish) {
        this.timeToWish = timeToWish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

}
