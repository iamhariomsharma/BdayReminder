package com.heckteck.birthy.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        return new DatePickerDialog(getActivity(),this, year, month + 1, day);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                this, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        return datePickerDialog;
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;

        Calendar calendar = Calendar.getInstance();
        int newYear = calendar.get(Calendar.YEAR);

        String selectedDate = dayOfMonth + "/" + month + "/" + year;
        String selectedDateNoYear = dayOfMonth + "/" + month + "/" + newYear;
        String showDobNoYear = dayOfMonth + "/" + month;
        Toast.makeText(getActivity(), "Date is: " + selectedDate, Toast.LENGTH_SHORT).show();
        Intent dateIntent = new Intent();
        dateIntent.putExtra("RETURNED_DATE_YEAR", selectedDate);
        dateIntent.putExtra("RETURNED_DATE_NO_YEAR", selectedDateNoYear);
        dateIntent.putExtra("SHOW_DOB", showDobNoYear);
        dateIntent.putExtra("YEAR", year);
        dateIntent.putExtra("MONTH", monthOfYear);
        dateIntent.putExtra("DAY", dayOfMonth);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, dateIntent);
    }
}
