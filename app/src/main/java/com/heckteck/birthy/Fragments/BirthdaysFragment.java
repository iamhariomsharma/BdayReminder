package com.heckteck.birthy.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heckteck.birthy.Activities.AddBirthdayActivity;
import com.heckteck.birthy.Adapters.BirthdayAdapter;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;
import com.heckteck.birthy.ViewModel.BirthdayViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BirthdaysFragment extends Fragment {

    private List<Birthday> birthdayList = new ArrayList<>();
    private BirthdayViewModel birthdayViewModel;
    private RecyclerView birthday_rv;
    private BirthdayAdapter birthdayAdapter;
    private FloatingActionButton fab_add;


    public BirthdaysFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birthdays, container, false);

        fab_add = view.findViewById(R.id.fab_addBday);

        birthday_rv = view.findViewById(R.id.birthdayRecycler);
        initRecyclerView();
        initViewModel();

//        birthdayAdapter = new BirthdayAdapter();
//        birthday_rv.setAdapter(birthdayAdapter);

//        birthdayViewModel.getAllBirthdays().observe(getActivity(), new Observer<List<Birthday>>() {
//            @Override
//            public void onChanged(List<Birthday> birthdays) {
//                birthdayAdapter.submitList(birthdays);
//            }
//        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBirthdayIntent = new Intent(getActivity(), AddBirthdayActivity.class);
                startActivity(addBirthdayIntent);
            }
        });

        return view;
    }

    private void initViewModel(){
        Observer<List<Birthday>> birthdayObserver = new Observer<List<Birthday>>() {
            @Override
            public void onChanged(List<Birthday> birthdays) {
                birthdayList.clear();
                birthdayList.addAll(birthdays);

                if (birthdayAdapter == null){
                    birthdayAdapter = new BirthdayAdapter(getActivity(), birthdayList);
                    birthday_rv.setAdapter(birthdayAdapter);
                }else {
                    birthdayAdapter.notifyDataSetChanged();
                }
            }
        };
        birthdayViewModel = ViewModelProviders.of(getActivity()).get(BirthdayViewModel.class);
        birthdayViewModel.getAllBirthdays().observe(getActivity(), birthdayObserver);
    }

    private void initRecyclerView(){
        birthday_rv.setHasFixedSize(true);
        birthday_rv.setItemViewCacheSize(10);
        birthday_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
