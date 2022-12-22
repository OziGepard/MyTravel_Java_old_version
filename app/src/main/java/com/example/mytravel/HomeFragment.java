package com.example.mytravel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    View view;
    private EditText searchLocation;
    private Button dateButtonIn;
    private Button dateButtonOut;
    private Button datePickerOutButton, datePickerInButton;
    private TextView setRoomsTxt, setAdultsTxt, setChildTxt;
    private LayoutInflater layoutInflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        datePickerInButton = view.findViewById(R.id.datePickerInButton);
        datePickerOutButton = view.findViewById(R.id.datePickerOutButton);
        searchLocation = view.findViewById(R.id.searchLocation);
        Button searchButton = view.findViewById(R.id.searchButton);
        dateButtonIn = view.findViewById(R.id.datePickerInButton);
        dateButtonOut = view.findViewById(R.id.datePickerOutButton);
        LinearLayout popupLayout = view.findViewById(R.id.popupLayout);
        setRoomsTxt = view.findViewById(R.id.setRoomsTxt);
        setAdultsTxt = view.findViewById(R.id.setAdultsTxt);
        setChildTxt = view.findViewById(R.id.setChildTxt);
        layoutInflater = getLayoutInflater();

        dateButtonOut.setOnClickListener(this);
        dateButtonIn.setOnClickListener(this);
        popupLayout.setOnClickListener(this);
        searchButton.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View view) {
        PickingDate pickingDate;
        switch (view.getId())
        {
            case R.id.datePickerInButton:
            case R.id.datePickerOutButton:
                pickingDate = new PickingDate(view, datePickerOutButton, datePickerInButton);
                DatePickerDialog datePickerDialog = pickingDate.initDatePicker();
                datePickerDialog.show();
                break;
            case R.id.popupLayout:
                DialogBuilder dialogBuilder = new DialogBuilder(layoutInflater, setRoomsTxt, setAdultsTxt, setChildTxt);
                dialogBuilder.createNewDialogContent();
                break;
            case R.id.searchButton:
                if(isEverythingUp())
                {
                    Intent intent = new Intent(view.getContext(), OffersActivity.class);
                    ArrayList<String> dataToSend = new ArrayList<>();
                    dataToSend.add(searchLocation.getText().toString());
                    dataToSend.add(dateButtonOut.getText().toString());
                    dataToSend.add(dateButtonIn.getText().toString());
                    dataToSend.add(setRoomsTxt.getText().toString());
                    dataToSend.add(setAdultsTxt.getText().toString());
                    dataToSend.add(setChildTxt.getText().toString());
                    intent.putStringArrayListExtra("offersData", dataToSend);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this.getContext(), "Wypełnij wszystkie wymagane pola", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
    private boolean isEverythingUp()
    {
        ArrayList<Boolean> checkList = new ArrayList<>();
        checkList.add(searchLocation.getText().toString().isEmpty());
        checkList.add(dateButtonOut.getText().toString().isEmpty());
        checkList.add(dateButtonIn.getText().toString().isEmpty());
        checkList.add(setRoomsTxt.getText().toString().isEmpty());
        checkList.add(setAdultsTxt.getText().toString().isEmpty());
        checkList.add(setChildTxt.getText().toString().isEmpty());
        for (Boolean item: checkList) {
            if(item)
            {
                return false;
            }
        }
        return true;
    }
}