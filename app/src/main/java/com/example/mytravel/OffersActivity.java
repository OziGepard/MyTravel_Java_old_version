package com.example.mytravel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class OffersActivity extends AppCompatActivity implements View.OnClickListener {

    private String imageID;
    private String title;
    private String offerID;
    private String description;
    private int price;
    private int availableRooms;
    private int peoplePerRoom;
    private ArrayList<OfferData> offerData;
    private OfferAdapter offerAdapter;

    private static final String TAG = "OffersActivity";

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference offersRef = db.collection("offers");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Dane z HomeFragment
        ArrayList<String> dataObtained = getIntent().getExtras().getStringArrayList("offersData");

        ImageView backToHomeFragment = findViewById(R.id.backToHomeFragment);
        backToHomeFragment.setOnClickListener(this);

        String city = dataObtained.get(0);
        String dateOut = dataObtained.get(1);
        String dateIn = dataObtained.get(2);
        double adults = Integer.parseInt(dataObtained.get(4).replaceAll("[\\D]", ""));
        int days = (int) howManyDays(dateOut,dateIn) + 1;
        double rooms = Integer.parseInt(dataObtained.get(3).replaceAll("[\\D]", ""));


        offerData = new ArrayList<>();
        offerAdapter = new OfferAdapter(this, offerData);
        recyclerView.setAdapter(offerAdapter);

        Query query = offersRef.whereEqualTo("city", city.toLowerCase()).whereGreaterThanOrEqualTo("available_rooms", rooms);

        //Pobieranie danych z bazy

        query
            .get()
            .addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        availableRooms = Integer.parseInt(Objects.requireNonNull(document.get("available_rooms")).toString());
                        peoplePerRoom = Integer.parseInt(Objects.requireNonNull(document.get("people_per_room")).toString());
                        if(adults / rooms <= peoplePerRoom)
                        {
                            title = Objects.requireNonNull(document.get("title")).toString();
                            imageID = Objects.requireNonNull(document.get("image")).toString();
                            description = Objects.requireNonNull(document.get("description").toString());
                            offerID = document.getId();
                            price = Integer.parseInt(Objects.requireNonNull(document.get("price_per_day")).toString()) * (int) rooms * days;
                            OfferData offerData = new OfferData(title, imageID, price, (int) rooms, dateOut, dateIn, offerID, availableRooms, description);
                            this.offerData.add(offerData);
                        }
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    offerAdapter.notifyItemInserted(offerData.size()-1);
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
            assert d2 != null;
            assert d1 != null;
            long diff = d2.getTime() - d1.getTime();

            return diff / (1000 * 60 * 60 * 24);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backToHomeFragment) {
            onBackPressed();
        }
    }
}