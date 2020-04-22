package com.heckteck.birthy.Activities;

import android.os.Bundle;

import com.heckteck.birthy.Fragments.AddBirthdayFragment;
import com.heckteck.birthy.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddBirthdayActivity extends AppCompatActivity {

    Toolbar toolbar;
    String updateMode;
    String addMode;
    int birthdayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_birthday);

        toolbar = findViewById(R.id.addBirthdayToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Birthday");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            birthdayId = bundle.getInt("BIRTHDAY_ID");
            boolean isEditMode = bundle.getBoolean("isEditMode");
            addMode = bundle.getString("ADD_MODE");
            updateMode = bundle.getString("UPDATE_MODE");

            String checkMode = addMode;
            if (checkMode != null){
                if (checkMode.equals("addMode")){
                    AddBirthdayFragment addBirthdayFragment = new AddBirthdayFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.addBirthdayContainer, addBirthdayFragment)
                            .commit();
                }
            }else {
                checkMode = updateMode;
                if (checkMode.equals("updateMode")){
                    Bundle birthdayBundle = new Bundle();
                    birthdayBundle.putInt("BIRTH_ID", birthdayId);
                    AddBirthdayFragment fragment = new AddBirthdayFragment();
                    fragment.setArguments(birthdayBundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.addBirthdayContainer, fragment)
                            .commit();
                }
            }

//            if (isEditMode) {
//                setTitle("Edit Birthday");
//
//
//                birthdayBundle.putBoolean("isEditMode", isEditMode);
//                AddBirthdayFragment fragment = new AddBirthdayFragment();
//                fragment.setArguments(birthdayBundle);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.addBirthdayFragment, fragment)
//                        .commit();
//            } else {
//                setTitle("Add New Birthday");
//            }

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
