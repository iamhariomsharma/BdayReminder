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
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;
import com.heckteck.birthy.ViewModels.BirthdayViewModel;

import java.util.Date;

public class BirthdayDetailActivity extends AppCompatActivity {

    private CircleImageView userProfile;
    private TextView userName;
    private Toolbar toolbar;
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

        birthdayViewModel = ViewModelProviders.of(this).get(BirthdayViewModel.class);

        userProfile = findViewById(R.id.iv_userProfile);
        userName = findViewById(R.id.tv_username);
        countdownView = findViewById(R.id.birthCountdown);

        Date currentDate = new Date();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            birthday = bundle.getParcelable("birthdayDetail");
            String name = birthday.getName();
            String imgUri = birthday.getUserImg();
            Date birthDate = birthday.getTimeLeft();

            if (imgUri.equals("null")) {
                userProfile.setImageResource(R.drawable.ic_userimg);
            } else {
                userProfile.setImageURI(Uri.parse(imgUri));
            }
            userName.setText(name);

            long currentDateTime = currentDate.getTime();
            long birthDateTime = birthDate.getTime();
            long countDownToBirthday = birthDateTime - currentDateTime;

            countdownView.start(countDownToBirthday);

        }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
