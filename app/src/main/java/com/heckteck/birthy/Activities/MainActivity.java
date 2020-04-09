package com.heckteck.birthy.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.heckteck.birthy.R;
import com.heckteck.birthy.Utils.Constants;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavController navController;
    NavigationView navigationView;
    NotificationManagerCompat notificationManager;
    String id;
    String phone;
    String sendWishesDialog;
    String sendWishesNotification;
    Toolbar toolbar;

    public static String ORDER_BY = "name";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ORDER_BY = "date";

        notificationManager = NotificationManagerCompat.from(this);
        navController = Navigation.findNavController(this, R.id.navHostFragment);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            sendWishesNotification = intent.getStringExtra("SEND");
            sendWishesDialog = intent.getStringExtra("SEND_DIALOG");
            phone = intent.getStringExtra("PHONE");
            id = getIntent().getStringExtra("ID");
            Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
            String str = sendWishesNotification;
            if (str != null) {
                if (str.equals("sendWishesNotification")) {
                    notificationManager.cancel(Integer.parseInt(id));
                    Bundle bundle = new Bundle();
                    bundle.putString("PHONE", phone.trim());
                    bundle.putString("Activity1", "activity1");
                    navController.navigate(R.id.sendWishesFragment, bundle);
                }
            } else {
                str = sendWishesDialog;
                if (str != null && str.equals("sendWishesDialog")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("PHONE2", phone);
                    bundle.putString("Activity2", "activity2");
                    navController.navigate(R.id.sendWishesFragment, bundle);
                }
            }
        }

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.addBirthdayToolbar);
        navigationView.setItemIconTintList(null);
        NavigationUI.setupWithNavController(navigationView, navController);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerToggle.setDrawerIndicatorEnabled(false);
//        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerToggle.setHomeAsUpIndicator(R.drawable.ic_navigation);
        drawerToggle.syncState();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuBtn_sort:
                showSortDialog();
                break;
            case R.id.menuBtn_search:
                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void showSortDialog() {
        Dialog sortDialog = new Dialog(this);
        sortDialog.setContentView(R.layout.sort_dialog);
        sortDialog.show();
    }
}
