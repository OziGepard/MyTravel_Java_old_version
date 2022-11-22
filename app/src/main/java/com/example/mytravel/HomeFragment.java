package com.example.mytravel;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    View view;
    private DatePickerDialog datePickerDialog;
    private Button dateButtonIn, dateButtonOut, searchButton;
    private Button datePickerOutButton, datePickerInButton, buttontest;
    private TextView setRoomsTxt, setAdultsTxt, setChildTxt;
    private LinearLayout popupLayout;
    public LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        datePickerInButton = view.findViewById(R.id.datePickerInButton);
        datePickerOutButton = view.findViewById(R.id.datePickerOutButton);
        searchButton = view.findViewById(R.id.searchButton);
        dateButtonIn = view.findViewById(R.id.datePickerInButton);
        dateButtonOut = view.findViewById(R.id.datePickerOutButton);
        popupLayout = view.findViewById(R.id.popupLayout);
        setRoomsTxt = view.findViewById(R.id.setRoomsTxt);
        setAdultsTxt = view.findViewById(R.id.setAdultsTxt);
        setChildTxt = view.findViewById(R.id.setChildTxt);
        layoutInflater = getLayoutInflater();

        dateButtonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickingDate pickingDate = new PickingDate(view, datePickerOutButton, datePickerInButton);
                datePickerDialog = pickingDate.initDatePicker();
                datePickerDialog.show();
            }
        });
        dateButtonIn.setOnClickListener(view -> {
            PickingDate pickingDate = new PickingDate(view, datePickerOutButton, datePickerInButton);
            datePickerDialog = pickingDate.initDatePicker();
            datePickerDialog.show();
        });
        popupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBuilder dialogBuilder = new DialogBuilder(layoutInflater, setRoomsTxt, setAdultsTxt, setChildTxt);
                System.out.println(setRoomsTxt.getText());
                dialogBuilder.createNewDialogContent();
                System.out.println(setRoomsTxt.getText());

            }
        });
        return view;
    }
}