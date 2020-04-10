package com.heckteck.birthy.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.heckteck.birthy.Adapters.BirthdayAdapter;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;
import com.heckteck.birthy.Utils.Constants;
import com.heckteck.birthy.ViewModel.SearchViewModel;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

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
    SearchView searchView;
    SearchViewModel searchViewModel;
    RecyclerView recyclerView;
    BirthdayAdapter birthdayAdapter;


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
        recyclerView = findViewById(R.id.birthdayRecycler);
        birthdayAdapter = new BirthdayAdapter();

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

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
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };
        menu.findItem(R.id.menuBtn_search).setOnActionExpandListener(onActionExpandListener);
        searchView = (SearchView) menu.findItem(R.id.menuBtn_search).getActionView();
        searchView.setQueryHint("Search Birthdays");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.searchBirthday(query);
                observerSetUp();
                searchView.clearFocus();
                menu.findItem(R.id.menuBtn_search).collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchViewModel.searchBirthday(newText);
                observerSetUp();
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(MainActivity.this, "Search view closed", Toast.LENGTH_SHORT).show();
                menu.findItem(R.id.menuBtn_search).collapseActionView();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuBtn_sort:
                showSortDialog();
                break;
//            case R.id.menuBtn_search:
//                searchView = (SearchView) item.getActionView();
//                searchView.setSubmitButtonEnabled(true);
//                searchView.setOnQueryTextListener(onQueryTextListener);
//                break;
        }
        return true;
    }

    private void observerSetUp(){
        searchViewModel.getSearchResults().observe(this, new Observer<List<Birthday>>() {
            @Override
            public void onChanged(List<Birthday> birthdays) {
                if (birthdays.size() > 0){
                    Log.d("SearchList", birthdays.get(0).getName());
                    recyclerView = findViewById(R.id.birthdayRecycler);
                    birthdayAdapter.submitList(birthdays);
                    recyclerView.setAdapter(birthdayAdapter);
                }else {
                    Toast.makeText(MainActivity.this, "No Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSortDialog() {
        Dialog sortDialog = new Dialog(this);
        sortDialog.setContentView(R.layout.sort_dialog);
        sortDialog.show();
    }
}
