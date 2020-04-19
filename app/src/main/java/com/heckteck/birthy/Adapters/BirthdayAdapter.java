package com.heckteck.birthy.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;
import com.heckteck.birthy.Utils.BirthdayItemClickInterface;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.BirthdayHolder> implements Filterable {

    private List<Birthday> birthdays;
    private List<Birthday> birthdaysFiltered;
    private Context context;
    private BirthdayItemClickInterface birthdayItemClickInterface;

    public BirthdayAdapter(Context context, List<Birthday> birthdays, BirthdayItemClickInterface clickInterface) {
        this.context = context;
        this.birthdays = birthdays;
        this.birthdaysFiltered = birthdays;
        this.birthdayItemClickInterface = clickInterface;
    }

    @NonNull
    @Override
    public BirthdayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_birthday, parent, false);
        return new BirthdayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthdayHolder holder, int position) {
        Birthday birthday = birthdaysFiltered.get(position);
        holder.tv_name.setText(birthday.getName());

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int newMonth = month + 1;
        String currentDateTime = day + "/" + newMonth + "/" + year;
        Log.d("DATE", currentDateTime);

//        String currentDateTime = birthday.getCurrentDateTime();
        String birthDate = birthday.getBirthDate();
        DateTime dateTime = convertToDateTime(currentDateTime);
        DateTime dateTime2 = convertToDateTime(birthDate);
        calculateNextBirthday(dateTime, dateTime2, holder.timeRemaining, holder.tv_timeline);
        calculateCurrentAge(dateTime, dateTime2, holder.tv_turns);

        Date dateToShow = dateTime2.toDate();

        if (birthday.isYearKnow()) {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
            String birthdayToShow = format.format(dateToShow);
            holder.tv_birthDate.setText(birthdayToShow);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM");
            String birthdayToShow = format.format(dateToShow);
            holder.tv_birthDate.setText(birthdayToShow);
            holder.tv_turns.setVisibility(View.GONE);
            holder.tv_name.setPadding(0, 16, 0, 0);
        }


        if (birthday.getUserImg().equals("null")) {
//            Glide.with(context).load(R.raw.cake_placeholder).into(holder.profileImg);
            if (!holder.profileImg.isAnimating()) {
                holder.profileImg.setAnimation(R.raw.cake_placeholder);
            }
        } else {
            holder.circleProfile.setVisibility(View.VISIBLE);
            holder.profileImg.setVisibility(View.INVISIBLE);
            Glide.with(context).load(Uri.parse(birthday.getUserImg())).into(holder.circleProfile);
//            holder.circleProfile.setImageURI(Uri.parse(birthday.getUserImg()));
        }

        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        if (holder.getAdapterPosition() % 4 == 0) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg1);
        } else if (holder.getAdapterPosition() % 4 == 1) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg2);
        } else if (holder.getAdapterPosition() % 4 == 2) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg3);
        } else if (holder.getAdapterPosition() % 4 == 3) {
            holder.relativeLayout.setBackgroundResource(R.drawable.card_bg4);
        }
    }

    @Override
    public long getItemId(int position) {
        Birthday birthday = birthdaysFiltered.get(position);
        return birthday.getId();
    }

    @Override
    public int getItemCount() {
        return birthdaysFiltered.size();
    }

    private void calculateNextBirthday(DateTime todayDateTime, DateTime birthdayDateTime, TextView daysRemaining, TextView timeline) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        DateTime nextBirthday = birthdayDateTime.withYear(year);
        if (nextBirthday.isBefore(todayDateTime) || nextBirthday == todayDateTime) {
            nextBirthday = birthdayDateTime.withYear(year + 1);
        }
        Period dateDifferencePeriod = displayBirthdayResult(nextBirthday, todayDateTime);
        Period daysDifference = displayDaysResult(nextBirthday, todayDateTime);
        int totalDaysLeft = daysDifference.getDays();
        int getDateInDays = dateDifferencePeriod.getDays();
        int getDateInMonths = dateDifferencePeriod.getMonths();
        int getDateInYears = dateDifferencePeriod.getYears();

//        if (totalDaysLeft == 0) {
//            daysRemaining.setText("Today");
//        } else if (totalDaysLeft < 2) {
//            daysRemaining.setText("Tomorrow");
//        } else {
//            daysRemaining.setText(Html.fromHtml("" + totalDaysLeft));
//            timeline.setText("Days");
//        }

        if (getDateInMonths < 1) {
            if (getDateInDays <= 0) {
                daysRemaining.setText("Today");
                timeline.setVisibility(View.GONE);
                daysRemaining.setBackgroundResource(R.drawable.ic_today_txt_bg);
                daysRemaining.setTextColor(Color.WHITE);
                daysRemaining.setTextSize(18);
                daysRemaining.setPadding(20, 8, 20, 10);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) daysRemaining.getLayoutParams();
                layoutParams.topMargin = 55;
                layoutParams.rightMargin = 24;
                daysRemaining.setLayoutParams(layoutParams);

            } else if (getDateInDays < 2) {
                daysRemaining.setText("Tomorrow");
                timeline.setVisibility(View.GONE);
                daysRemaining.setBackgroundResource(R.drawable.ic_tomorrow_txt_bg);
                daysRemaining.setTextColor(Color.WHITE);
                daysRemaining.setPadding(16, 10, 16, 13);
                daysRemaining.setTextSize(16);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) daysRemaining.getLayoutParams();
                layoutParams.topMargin = 55;
                layoutParams.rightMargin = 24;
                daysRemaining.setLayoutParams(layoutParams);
            } else {
                int days = getDateInDays - 1;
                if (days == 1) {
                    daysRemaining.setText(Html.fromHtml("" + days));
                    timeline.setText("Day");
                } else {
                    daysRemaining.setText(Html.fromHtml("" + days));
                    timeline.setText("Days");
                }
            }

        } else if (getDateInYears < 1) {
            if (getDateInMonths == 1) {
                daysRemaining.setText(Html.fromHtml("" + getDateInMonths));
                timeline.setText("Month");
            } else {
                daysRemaining.setText(Html.fromHtml("" + getDateInMonths));
                timeline.setText("Months");
            }
        }
    }

    private Period displayDaysResult(DateTime nextBirthday, DateTime todayDateTime) {
        return new Period(todayDateTime, nextBirthday, PeriodType.days());
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    birthdaysFiltered = birthdays;
                } else {
                    List<Birthday> filteredList = new ArrayList<>();
                    for (Birthday row : birthdays) {
                        if (row.getName().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    birthdaysFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = birthdaysFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                birthdaysFiltered = (ArrayList<Birthday>) results.values;
                notifyDataSetChanged();
            }
        };
    }

//    public Birthday getBirthdayAt(int position) {
//        return getItem(position);
//    }

    class BirthdayHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_birthDate;
        TextView tv_turns;
        TextView timeRemaining;
        TextView tv_timeline;
        LottieAnimationView profileImg;
        CircleImageView circleProfile;
        ConstraintLayout relativeLayout;

        public BirthdayHolder(@NonNull final View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_birthDate = itemView.findViewById(R.id.tv_birthDate);
            tv_turns = itemView.findViewById(R.id.tv_turns);
            timeRemaining = itemView.findViewById(R.id.tv_daysLeft);
            tv_timeline = itemView.findViewById(R.id.tv_timeline);
            profileImg = itemView.findViewById(R.id.profileImg);
            circleProfile = itemView.findViewById(R.id.circleProfile);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    birthdayItemClickInterface.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    birthdayItemClickInterface.onItemLongClick(getAdapterPosition(), itemView);
                    return true;
                }
            });

        }
    }

}
