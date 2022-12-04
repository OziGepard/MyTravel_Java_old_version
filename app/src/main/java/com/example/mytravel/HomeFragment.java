package com.example.mytravel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class HomeFragment extends Fragment implements View.OnClickListener {
    View view;
    private DatePickerDialog datePickerDialog;
    private EditText searchLocation;
    private Button dateButtonIn, dateButtonOut, searchButton;
    private Button datePickerOutButton, datePickerInButton, buttontest;
    private TextView setRoomsTxt, setAdultsTxt, setChildTxt;
    private LinearLayout popupLayout;
    private LayoutInflater layoutInflater;

    private ArrayList<String> dataToSend;


    private static String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        datePickerInButton = view.findViewById(R.id.datePickerInButton);
        datePickerOutButton = view.findViewById(R.id.datePickerOutButton);
        searchLocation = view.findViewById(R.id.searchLocation);
        searchButton = view.findViewById(R.id.searchButton);
        dateButtonIn = view.findViewById(R.id.datePickerInButton);
        dateButtonOut = view.findViewById(R.id.datePickerOutButton);
        popupLayout = view.findViewById(R.id.popupLayout);
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
                datePickerDialog = pickingDate.initDatePicker();
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
                    dataToSend = new ArrayList<>();
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