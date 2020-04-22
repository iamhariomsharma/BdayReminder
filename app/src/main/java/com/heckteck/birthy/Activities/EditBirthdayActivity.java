package com.heckteck.birthy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.heckteck.birthy.Fragments.EditBirthdayFragment;
import com.heckteck.birthy.R;

public class EditBirthdayActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_birthday);

        toolbar = findViewById(R.id.editBirthdayToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Birthday");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
