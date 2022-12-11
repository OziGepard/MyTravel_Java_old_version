package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OfferDetailActivity extends AppCompatActivity {
    private ImageView backToOffers, offerDetailImage;
    private TextView offerDetailText, offerDetailTitle;
    private Button reservationButton;


    private String imageID;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private final long ONE_MEGABYTE = 1024 * 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        backToOffers = findViewById(R.id.backToOffers);
        offerDetailImage = findViewById(R.id.offerDetailImage);
        offerDetailText = findViewById(R.id.offerDetailText);
        offerDetailTitle = findViewById(R.id.offerDetailTitle);
        reservationButton = findViewById(R.id.reservationButton);

        storage = FirebaseStorage.getInstance();

        Bundle bundle = getIntent().getExtras();
        offerDetailTitle.setText(bundle.getString("Text"));
        imageID = bundle.getString("Image");



        storageRef = storage.getReferenceFromUrl("gs://fir-db-52ce4.appspot.com/images/" + imageID + ".jpg");
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            offerDetailImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        });

        backToOffers.setOnClickListener(view -> onBackPressed());
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfferDetailActivity.this, ReservationActivity.class);
                intent.putExtra("DateOut", bundle.getString("DateOut"));
                intent.putExtra("DateIn", bundle.getString("DateIn"));
                intent.putExtra("Title", bundle.getString("Title"));
                intent.putExtra("OfferID", bundle.getString("OfferID"));
                intent.putExtra("Rooms", bundle.getInt("Rooms"));
                intent.putExtra("Price", bundle.getInt("Price"));
                intent.putExtra("AvailableRooms", bundle.getInt("AvailableRooms"));

                startActivity(intent);
            }
        });
    }
}