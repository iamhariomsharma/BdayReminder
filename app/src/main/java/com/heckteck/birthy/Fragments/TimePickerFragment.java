package com.heckteck.birthy.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, 0, 0, DateFormat.is24HourFormat(getContext()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        CharSequence timeFormatted = DateFormat.format("hh:mm a", calendar);

        String selectedTime = (String) timeFormatted;
        Toast.makeText(getActivity(), "Time is: " + selectedTime, Toast.LENGTH_SHORT).show();

        Intent timeIntent = new Intent();
        timeIntent.putExtra("RETURNED TIME", selectedTime);
        timeIntent.putExtra("HOUR", hour);
        timeIntent.putExtra("MINUTE", minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, timeIntent);
    }
}
