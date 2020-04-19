package com.heckteck.birthy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import cn.iwgang.countdownview.CountdownView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;
import com.heckteck.birthy.ViewModels.BirthdayViewModel;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BirthdayDetailActivity extends AppCompatActivity {

    private CircleImageView userProfile;
    private TextView userName, tv_birthDate, tv_daysLeft, tv_turnedAge, tv_notes;
    Toolbar toolbar;
    private BirthdayViewModel birthdayViewModel;
    private Birthday birthday;
    private CountdownView countdownView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_detail);

        toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        birthdayViewModel = ViewModelProviders.of(this).get(BirthdayViewModel.class);

        userProfile = findViewById(R.id.iv_userProfile);
        userName = findViewById(R.id.tv_username);
        tv_birthDate = findViewById(R.id.tv_birthDate);
        tv_daysLeft = findViewById(R.id.tv_daysLeft);
        tv_turnedAge = findViewById(R.id.tv_turnedAge);
        tv_notes = findViewById(R.id.tv_notesExtra);
        countdownView = findViewById(R.id.birthCountdown);

        Date currentDate = new Date();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            birthday = bundle.getParcelable("birthdayDetail");
            String name = birthday.getName();
            String imgUri = birthday.getUserImg();
            boolean isKnowYear = birthday.isYearKnow();
            Date birthDate = birthday.getTimeLeft();
            Date realBirthDate = birthday.getRealBirthDate();

            if (imgUri.equals("null")) {
                userProfile.setImageResource(R.drawable.ic_userimg);
            } else {
                userProfile.setImageURI(Uri.parse(imgUri));
            }
            userName.setText(name);

            if (isKnowYear) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                String birthday = dateFormat.format(realBirthDate);
                tv_birthDate.setText(birthday);
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM");
                String birthday = dateFormat.format(realBirthDate);
                tv_birthDate.setText(birthday);
            }
            long currentDateTime = currentDate.getTime();
            long birthDateTime = birthDate.getTime();
            long countDownToBirthday = birthDateTime - currentDateTime;
            countdownView.start(countDownToBirthday);

            long daysLeft = countDownToBirthday / (1000 * 3600 * 24);
//            long diffHours = countDownToBirthday / (60 * 60 * 1000);
            long diffInHours = TimeUnit.MILLISECONDS.toHours(countDownToBirthday);
//            Period period = displayBirthdayResult(birthDate, currentDate);
//            int daysLeft = period.getDays();

            if (daysLeft <= 0 && diffInHours <= 0) {
                tv_daysLeft.setText("Today");
            } else if (daysLeft <= 0 && diffInHours < 24) {
                tv_daysLeft.setText("Tomorrow");
            } else {
                if (daysLeft == 1) {
                    tv_daysLeft.setText(daysLeft + " day");
                } else {
                    tv_daysLeft.setText(daysLeft + " days");

                }
            }


        }
    }

    private Period displayBirthdayResult(Date dateToday, Date birthdayDate) {
//        return new Period(birthdayDate, dateToday, PeriodType.days());
        return new Period(birthdayDate.getTime(), dateToday.getTime(), PeriodType.days());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.birthday_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editBtn:
                startEditActivity();
                break;

            case R.id.menu_deleteBtn:
                birthdayViewModel.deleteBirthday(birthday);
                Toast.makeText(this, "Birthday Deleted", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;
    }

    private void startEditActivity() {
        Intent editIntent = new Intent(BirthdayDetailActivity.this, AddBirthdayActivity.class);
        startActivity(editIntent);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
}
