package com.heckteck.birthy.Adapters;

import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class BirthdayAdapter extends ListAdapter<Birthday, BirthdayAdapter.BirthdayHolder> {


    public BirthdayAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Birthday> DIFF_CALLBACK = new DiffUtil.ItemCallback<Birthday>() {
        @Override
        public boolean areItemsTheSame(@NonNull Birthday oldItem, @NonNull Birthday newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Birthday oldItem, @NonNull Birthday newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getBirthDate().equals(newItem.getBirthDate()) &&
                    oldItem.getPhoneNumber().equals(newItem.getPhoneNumber()) &&
                    oldItem.getCurrentDateTime().equals(newItem.getCurrentDateTime()) &&
                    oldItem.getUserImg().equals(newItem.getUserImg()) &&
                    oldItem.getTimeToWish().equals(newItem.getTimeToWish()) &&
                    oldItem.getNotes().equals(newItem.getNotes());
        }


    };

    @NonNull
    @Override
    public BirthdayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bday, parent, false);
        return new BirthdayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthdayHolder holder, int position) {
        Birthday birthday = getItem(position);
        holder.tv_name.setText(birthday.getName());
        holder.tv_birthDate.setText(birthday.getBirthDate());

        String currentDateTime = birthday.getCurrentDateTime();
        String birthDate = birthday.getBirthDate();
        DateTime dateTime = convertToDateTime(currentDateTime);
        DateTime dateTime2 = convertToDateTime(birthDate);
        calculateNextBirthday(dateTime, dateTime2, holder.timeRemaining, holder.tv_timeline);
        calculateCurrentAge(dateTime, dateTime2, holder.tv_turns);

        if (birthday.getUserImg().equals("null")) {
            holder.profileImg.setAnimation(R.raw.cake_placeholder);
        } else {
            holder.profileImg.setImageURI(Uri.parse(birthday.getUserImg()));
        }

        if (position % 4 == 0) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg1);
        } else if (position % 4 == 1) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg2);
        } else if (position % 4 == 2) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg3);
        } else if (position % 4 == 3) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg4);
        }
    }

    private void calculateNextBirthday(DateTime todayDateTime, DateTime birthdayDateTime, TextView daysRemaining, TextView timeline) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        DateTime nextBirthday = birthdayDateTime.withYear(year);
        if (nextBirthday.isBefore(todayDateTime) || nextBirthday == todayDateTime) {
            nextBirthday = birthdayDateTime.withYear(year + 1);
        }
        Period dateDifferencePeriod = displayBirthdayResult(nextBirthday, todayDateTime);
        int getDateInDays = dateDifferencePeriod.getDays();
        int getDateInMonths = dateDifferencePeriod.getMonths();
        int getDateInYears = dateDifferencePeriod.getYears();

//      TODO: change days remaining on basis of time left
        if (getDateInMonths < 1) {
            daysRemaining.setText(Html.fromHtml("" + getDateInDays));
            timeline.setText("Days");
        } else if (getDateInYears < 1) {
            daysRemaining.setText(Html.fromHtml("" + getDateInMonths));
            timeline.setText("Months");
        }
    }

    private void calculateCurrentAge(DateTime dateToday, DateTime birthdayDate, TextView turns) {
        Period dateDifferencePeriod = displayBirthdayResult(dateToday, birthdayDate);
        int getDateInDays = dateDifferencePeriod.getDays();
        int getDateInMonths = dateDifferencePeriod.getMonths();
        int getDateInYears = dateDifferencePeriod.getYears() + 1;
        turns.setText(Html.fromHtml("" + "Turns " + getDateInYears));
    }

    private Period displayBirthdayResult(DateTime dateToday, DateTime birthdayDate) {
        return new Period(birthdayDate, dateToday, PeriodType.yearMonthDayTime());
    }

    private DateTime convertToDateTime(String stringToConvert) {
        String[] newStringArray = convertStingToArray(stringToConvert);
        int year = Integer.parseInt(newStringArray[2].trim());
        int month = Integer.parseInt(newStringArray[1].trim());
        int day = Integer.parseInt(newStringArray[0].trim());
        LocalDate mLocalDate = new LocalDate(year, month, day);
        return mLocalDate.toDateTime(LocalTime.fromDateFields(mLocalDate.toDate()));
    }


    private String[] convertStingToArray(String stringToConvert) {
        return stringToConvert.split("/");
    }

    public Birthday getBirthdayAt(int position) {
        return getItem(position);
    }

    class BirthdayHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_birthDate;
        TextView tv_turns;
        TextView timeRemaining;
        TextView tv_timeline;
        LottieAnimationView profileImg;
        RelativeLayout relativeLayout;

        public BirthdayHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_birthDate = itemView.findViewById(R.id.tv_birthDate);
            tv_turns = itemView.findViewById(R.id.tv_turns);
            timeRemaining = itemView.findViewById(R.id.tv_daysLeft);
            tv_timeline = itemView.findViewById(R.id.tv_timeline);
            profileImg = itemView.findViewById(R.id.profileImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

        }
    }

}
