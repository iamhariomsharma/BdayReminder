package com.heckteck.birthy.Activities;

import android.os.Bundle;

import com.heckteck.birthy.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddBirthdayActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_birthday);

        toolbar = findViewById(R.id.addBirthdayToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Birthday");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
