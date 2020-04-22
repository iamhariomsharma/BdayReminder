package com.heckteck.birthy.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.heckteck.birthy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditBirthdayFragment extends Fragment {
    EditText name;

    public EditBirthdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_birthday, container, false);

        name = view.findViewById(R.id.et_username);
        return view;
    }
}
