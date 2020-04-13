package com.heckteck.birthy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;

public class BirthdayDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_detail);

        TextView tv_test = findViewById(R.id.tv_test);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Birthday birthday = bundle.getParcelable("birthdayDetail");
            tv_test.setText(String.format("%s\n%s\n%s", birthday.getName(), birthday.getBirthDate(), birthday.getTimeToWish()));
        }
    }
}
