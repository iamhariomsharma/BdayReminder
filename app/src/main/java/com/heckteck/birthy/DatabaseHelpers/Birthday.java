package com.heckteck.birthy.DatabaseHelpers;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "birthdays_table")
public class Birthday implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String birthDate;
    private int date;
    private String phoneNumber;
    private String notes;
    private String timeToWish;
    private String currentDateTime;
    private String userImg;
    private Date timeLeft;


    public Birthday(String name, String birthDate, int date, String phoneNumber, String notes, String timeToWish, String currentDateTime, String userImg, Date timeLeft) {
        this.name = name;
        this.birthDate = birthDate;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
        this.timeToWish = timeToWish;
        this.currentDateTime = currentDateTime;
        this.userImg = userImg;
        this.timeLeft = timeLeft;
    }

    public Date getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Date timeLeft) {
        this.timeLeft = timeLeft;
    }

    public static Creator<Birthday> getCREATOR() {
        return CREATOR;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.birthDate);
        dest.writeInt(this.date);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.notes);
        dest.writeString(this.timeToWish);
        dest.writeString(this.currentDateTime);
        dest.writeString(this.userImg);
    }

    protected Birthday(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.birthDate = in.readString();
        this.date = in.readInt();
        this.phoneNumber = in.readString();
        this.notes = in.readString();
        this.timeToWish = in.readString();
        this.currentDateTime = in.readString();
        this.userImg = in.readString();
    }

    public static final Parcelable.Creator<Birthday> CREATOR = new Parcelable.Creator<Birthday>() {
        @Override
        public Birthday createFromParcel(Parcel source) {
            return new Birthday(source);
        }

        @Override
        public Birthday[] newArray(int size) {
            return new Birthday[size];
        }
    };
}
