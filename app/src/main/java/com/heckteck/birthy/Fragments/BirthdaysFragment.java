package com.heckteck.birthy.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.heckteck.birthy.Activities.AddBirthdayActivity;
import com.heckteck.birthy.Activities.BirthdayDetailActivity;
import com.heckteck.birthy.Activities.MainActivity;
import com.heckteck.birthy.Adapters.BirthdayAdapter;
import com.heckteck.birthy.DatabaseHelpers.Birthday;
import com.heckteck.birthy.R;
import com.heckteck.birthy.Utils.BirthdayItemClickInterface;
import com.heckteck.birthy.ViewModels.BirthdayViewModel;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BirthdaysFragment extends Fragment implements BirthdayItemClickInterface {

    private List<Birthday> birthdayList = new ArrayList<>();
    private BirthdayViewModel birthdayViewModel;
    private RecyclerView birthday_rv;
    private BirthdayAdapter birthdayAdapter;
    private Dialog sortDialog;
    private LinearLayout emptyView;
//    private BirthdayFragmentListener listener;
//
//    public interface BirthdayFragmentListener{
//        void onBirthdaySent(int id);
//    }


    public BirthdaysFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_birthdays, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        getActivity().setTitle("Birthy");
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab_add = view.findViewById(R.id.fab_addBday);
        birthdayViewModel = ViewModelProviders.of(getActivity()).get(BirthdayViewModel.class);
        emptyView = view.findViewById(R.id.emptyView);
        birthday_rv = view.findViewById(R.id.birthdayRecycler);

        birthday_rv.setHasFixedSize(true);
        birthday_rv.setItemViewCacheSize(20);
        birthday_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        Observer<List<Birthday>> birthdayObserver = new Observer<List<Birthday>>() {
            @Override
            public void onChanged(List<Birthday> birthdays) {
                birthdayList.clear();
                birthdayList.addAll(birthdays);

                if (birthdayList.isEmpty()) {
                    birthday_rv.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    birthday_rv.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                birthdayAdapter = new BirthdayAdapter(getActivity(), birthdayList, BirthdaysFragment.this);
                birthdayAdapter.setHasStableIds(true);
                birthday_rv.setAdapter(birthdayAdapter);
                birthdayAdapter.notifyDataSetChanged();
            }
        };
        birthdayViewModel.getAllBirthdays().observe(getActivity(), birthdayObserver);


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBirthdayIntent = new Intent(getActivity(), AddBirthdayActivity.class);
                addBirthdayIntent.putExtra("ADD_MODE", "addMode");
                addBirthdayIntent.putExtra("isEditMode", false);
                startActivity(addBirthdayIntent);
            }
        });
    }

    private void initViewModel() {
        Observer<List<Birthday>> birthdayObserver = new Observer<List<Birthday>>() {
            @Override
            public void onChanged(List<Birthday> birthdays) {
                birthdayList.clear();
                birthdayList.addAll(birthdays);

                if (birthdayList.isEmpty()) {
                    birthday_rv.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    birthday_rv.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

                if (birthdayAdapter == null) {
                    birthdayAdapter = new BirthdayAdapter(getActivity(), birthdayList, BirthdaysFragment.this);
                    birthdayAdapter.setHasStableIds(true);
                    birthday_rv.setAdapter(birthdayAdapter);
                } else {
                    birthdayAdapter.notifyDataSetChanged();
                }
            }
        };
        birthdayViewModel = ViewModelProviders.of(getActivity()).get(BirthdayViewModel.class);
        birthdayViewModel.getAllBirthdays().observe(getActivity(), birthdayObserver);
    }

    private void initRecyclerView() {
        birthday_rv.setHasFixedSize(true);
        birthday_rv.setItemViewCacheSize(20);
        birthday_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void sortBy(List<Birthday> birthdays) {
        initRecyclerView();
        birthdayList.clear();
        birthdayList.addAll(birthdays);
        birthdayAdapter = new BirthdayAdapter(getActivity(), birthdayList, this);
        birthdayAdapter.setHasStableIds(true);
        birthday_rv.setAdapter(birthdayAdapter);
        birthdayAdapter.notifyDataSetChanged();
        sortDialog.dismiss();
    }

    private void showSortDialog() {
        sortDialog = new Dialog(getActivity());
        sortDialog.setContentView(R.layout.sort_dialog);
        TextView sortByDateNewest = sortDialog.findViewById(R.id.sortByBirthdayNewest);
        TextView sortByDateOldest = sortDialog.findViewById(R.id.sortByBirthdayOldest);
        TextView sortByNameAZ = sortDialog.findViewById(R.id.sortByNameAZ);
        TextView sortByNameZA = sortDialog.findViewById(R.id.sortByNameZA);

        sortByDateNewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecyclerView();
                initViewModel();
                sortDialog.dismiss();
            }
        });

        sortByDateOldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayViewModel.getBirthdaysByDateAsc().observe(getActivity(), new Observer<List<Birthday>>() {
                    @Override
                    public void onChanged(List<Birthday> birthdays) {
                        sortBy(birthdays);
                    }
                });
            }
        });

        sortByNameAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayViewModel.getBirthdaysByNameAsc().observe(getActivity(), new Observer<List<Birthday>>() {
                    @Override
                    public void onChanged(List<Birthday> birthdays) {
                        sortBy(birthdays);
                    }
                });
            }
        });

        sortByNameZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayViewModel.getBirthdaysByNameDesc().observe(getActivity(), new Observer<List<Birthday>>() {
                    @Override
                    public void onChanged(List<Birthday> birthdays) {
                        sortBy(birthdays);
                    }
                });
            }
        });


        sortDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuBtn_sort) {
            showSortDialog();
        }
        return true;
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), BirthdayDetailActivity.class);
        Birthday birthday = birthdayList.get(position);
        detailIntent.putExtra("birthdayDetail", birthday);
        startActivity(detailIntent);
    }

    @Override
    public void onItemLongClick(final int position, View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.inflate(R.menu.item_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.popupEdit) {

                    Intent updateIntent = new Intent(getActivity(), AddBirthdayActivity.class);
                    updateIntent.putExtra("isEditMode", true);
                    updateIntent.putExtra("UPDATE_MODE", "updateMode");
                    updateIntent.putExtra("BIRTHDAY_ID", birthdayList.get(position).getId());
                    startActivity(updateIntent);

                } else if (item.getItemId() == R.id.popupDelete) {
                    birthdayViewModel.deleteBirthday(birthdayList.get(position));
                    birthdayAdapter.notifyItemRemoved(position);
                    Toast.makeText(getActivity(), birthdayList.get(position).getName() + " Deleted", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        popupMenu.setGravity(Gravity.END);
        popupMenu.show();
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof BirthdayFragmentListener){
//            listener = (BirthdayFragmentListener) context;
//        }else {
//            throw new RuntimeException(context.toString()
//            + " must implement interface");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }
}
