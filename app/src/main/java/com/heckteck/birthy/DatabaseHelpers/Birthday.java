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
    private Date realBirthDate;
    private boolean isYearKnow;


    public Birthday(String name, String birthDate, int date, String phoneNumber, String notes, String timeToWish, String currentDateTime, String userImg, Date timeLeft, Date realBirthDate, boolean isYearKnow) {
        this.name = name;
        this.birthDate = birthDate;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
        this.timeToWish = timeToWish;
        this.currentDateTime = currentDateTime;
        this.userImg = userImg;
        this.timeLeft = timeLeft;
        this.realBirthDate = realBirthDate;
        this.isYearKnow = isYearKnow;
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Date getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Date timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Date getRealBirthDate() {
        return realBirthDate;
    }

    public void setRealBirthDate(Date realBirthDate) {
        this.realBirthDate = realBirthDate;
    }

    public boolean isYearKnow() {
        return isYearKnow;
    }

    public void setYearKnow(boolean yearKnow) {
        isYearKnow = yearKnow;
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
        dest.writeLong(this.timeLeft != null ? this.timeLeft.getTime() : -1);
        dest.writeLong(this.realBirthDate != null ? this.realBirthDate.getTime() : -1);
        dest.writeByte(this.isYearKnow ? (byte) 1 : (byte) 0);
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
        long tmpTimeLeft = in.readLong();
        this.timeLeft = tmpTimeLeft == -1 ? null : new Date(tmpTimeLeft);
        long tmpRealBirthDate = in.readLong();
        this.realBirthDate = tmpRealBirthDate == -1 ? null : new Date(tmpRealBirthDate);
        this.isYearKnow = in.readByte() != 0;
    }

    public static final Creator<Birthday> CREATOR = new Creator<Birthday>() {
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
