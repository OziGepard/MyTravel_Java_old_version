package com.example.mytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OffersActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backToHomeFragment;
    private LinearLayout containerLayout;

    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private CollectionReference offersRef;
    private StorageReference storageRef;
    private RecyclerView recyclerView;

    private ArrayList<String> dataObtained = new ArrayList<>();
    private String imageID;
    private String description;
    private Bitmap imageBitmap;
    private int price;
    private ArrayList<DataClass> dataClasses;
    private MyAdapter myAdapter;

    private static final String TAG = "OffersActivity";
    private final long ONE_MEGABYTE = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        storage = FirebaseStorage.getInstance();
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
                    description = document.get("description").toString();
                    imageID = document.get("image").toString();
                    price = Integer.parseInt(document.get("price_per_day").toString()) * rooms * days;

                    storageRef = storage.getReferenceFromUrl("gs://fir-db-52ce4.appspot.com/images/" + imageID + ".jpg");

                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            System.out.println("TUUUUUUUUUUUUUUUUUUUUUTAJ");
                            imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        }
                    });
                    System.out.println("JEEEEEEEEEEEESTEM");
                    DataClass dataClass = new DataClass(description, imageBitmap, price);
                    dataClasses.add(dataClass);
                    myAdapter.notifyItemInserted(dataClasses.size()-1);
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
                for(DataClass item : dataClasses)
                {
                    System.out.println(item.toString());
                }

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