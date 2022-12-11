package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OffersActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backToHomeFragment;

    private FirebaseFirestore db;
    private CollectionReference offersRef;
    private RecyclerView recyclerView;

    private ArrayList<String> dataObtained = new ArrayList<>();
    private String imageID;
    private String title;
    private String offerID;
    private int price;
    private int availableRooms;
    private ArrayList<DataClass> dataClasses;
    private MyAdapter myAdapter;

    private static final String TAG = "OffersActivity";

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        db = FirebaseFirestore.getInstance();
        offersRef = db.collection("offers");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Dane z HomeFragment
        dataObtained = getIntent().getExtras().getStringArrayList("offersData");

        backToHomeFragment = findViewById(R.id.backToHomeFragment);
        backToHomeFragment.setOnClickListener(this);

        String city = dataObtained.get(0);
        String dateOut = dataObtained.get(1);
        String dateIn = dataObtained.get(2);
        int days = (int) howManyDays(dateOut,dateIn) + 1;
        int rooms = Integer.parseInt(dataObtained.get(3).replaceAll("[\\D]", ""));


        dataClasses = new ArrayList<>();
        myAdapter = new MyAdapter(this,dataClasses);
        recyclerView.setAdapter(myAdapter);

        //Pobieranie danych z bazy

        offersRef
        .whereEqualTo("city", city.toLowerCase())
        .get()
        .addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                for(QueryDocumentSnapshot document : task.getResult())
                {
                    title = document.get("description").toString();
                    imageID = document.get("image").toString();
                    offerID = document.getId();
                    price = Integer.parseInt(document.get("price_per_day").toString()) * rooms * days;
                    availableRooms = Integer.parseInt(document.get("available_rooms").toString());
                    DataClass dataClass = new DataClass(title, imageID, price, rooms, dateOut, dateIn, offerID, availableRooms);
                    dataClasses.add(dataClass);

                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
                myAdapter.notifyItemInserted(dataClasses.size()-1);
            }
            else
            {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }

        });


    }

    private long howManyDays(String dateOut, String dateIn) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        try {
            Date d1 = formatter.parse(dateOut);
            Date d2 = formatter.parse(dateIn);
            long diff = d2.getTime() - d1.getTime();

            return diff / (1000 * 60 * 60 * 24);
        }
        catch(ParseException e)
        {
            System.out.println(e);
        }
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backToHomeFragment:
                onBackPressed();

        }
    }
}