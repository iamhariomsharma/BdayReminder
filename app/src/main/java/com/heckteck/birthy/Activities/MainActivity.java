package com.heckteck.birthy.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.heckteck.birthy.Adapters.BirthdayAdapter;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;
import com.heckteck.birthy.ViewModel.BirthdayViewModel;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    BirthdayViewModel birthdayViewModel;
    RecyclerView recyclerView;
    List<Birthday> birthdayList = new ArrayList<>();
    BirthdayAdapter birthdayAdapter;
    MaterialSearchView searchView;


    public static String ORDER_BY = "name";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        searchView = findViewById(R.id.searchView);
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
        MenuItem item = menu.findItem(R.id.menuBtn_search);
        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                observerSetUp();
                birthdayAdapter.getFilter().filter(query);
                searchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                observerSetUp();
                birthdayAdapter.getFilter().filter(newText);
                return true;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                birthdayAdapter.notifyDataSetChanged();
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuBtn_sort) {
            showSortDialog();
        }
        return true;
    }

    private void observerSetUp() {

        Observer<List<Birthday>> birthdayObserver = new Observer<List<Birthday>>() {
            @Override
            public void onChanged(List<Birthday> birthdays) {
                if (birthdays.size() > 0) {
                    birthdayList.clear();
                    birthdayList.addAll(birthdays);
                    recyclerView = findViewById(R.id.birthdayRecycler);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    birthdayAdapter = new BirthdayAdapter(MainActivity.this, birthdayList);
                    recyclerView.setAdapter(birthdayAdapter);
                    birthdayAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "No Match", Toast.LENGTH_SHORT).show();
                }
            }
        };

        birthdayViewModel = ViewModelProviders.of(this).get(BirthdayViewModel.class);
        birthdayViewModel.getAllBirthdays().observe(MainActivity.this, birthdayObserver);

    }

    private void showSortDialog() {
        Dialog sortDialog = new Dialog(this);
        sortDialog.setContentView(R.layout.sort_dialog);
        sortDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
