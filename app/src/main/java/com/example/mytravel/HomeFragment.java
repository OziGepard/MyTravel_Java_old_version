package com.example.mytravel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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

        isUpdated();
        return view;
    }

    private void isUpdated() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference updateRef = db.collection("update").document("dateOfUpdate");

        updateRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot documentSnapshot = task.getResult();

            String date = documentSnapshot.get("date").toString();
            LocalDate todaysDate = LocalDate.now();

            CollectionReference resRef = db.collection("reservations");
            CollectionReference offerRef = db.collection("offers");

            //Jeżeli jest cośinnego niżdzisiejsza data
            if(!date.equals(todaysDate.toString()))
            {
                //Zaktualizuj date w update
                updateRef.update("date", todaysDate.toString());


                //Sprawdzanie wszystkich rezerwacji w bazie
                resRef
                        .get()
                        .addOnCompleteListener(task1 -> {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                           if(task1.isSuccessful())
                           {
                               for(QueryDocumentSnapshot document : task1.getResult())
                               {
                                   String dateIn = document.get("date_in").toString();
                                   try {
                                       //Pobranie daty z bazy i zapisanie do Date
                                       Date travelEnded = formatter.parse(dateIn);
                                       //Zapisanie do Date dzisiejszej daty
                                       Date convertedDate = Date.from(todaysDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                                       //Jeżeli data wyjazdu jest wcześniej niż dzisiejsza data to usuń rezerwację
                                       //i zwróć zarezerwowane miejsca
                                       if(travelEnded.compareTo(convertedDate) < 0)
                                       {
                                           //Aktualizowanie ilości wolnych pokoi
                                           int reservedRooms = Integer.parseInt(document.get("reserved_rooms").toString());
                                           String ID = document.get("offer_ID").toString();
                                           offerRef.document(ID).get().addOnCompleteListener(task2 -> {
                                               DocumentSnapshot documentInfo = task2.getResult();

                                               int availableRooms = Integer.parseInt(documentInfo.get("available_rooms").toString());
                                               offerRef.document(ID).update("available_rooms", availableRooms + reservedRooms);
                                           });

                                           resRef.document(document.getId()).delete();
                                       }
                                   } catch (ParseException e) {
                                       e.printStackTrace();
                                   }
                               }
                           }
                        });
            }
        });
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