package com.example.mytravel;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OfferDetailActivity extends AppCompatActivity {
    private ImageView offerDetailImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        ImageView backToOffers = findViewById(R.id.backToOffers);
        offerDetailImage = findViewById(R.id.offerDetailImage);
        TextView offerDetailText = findViewById(R.id.offerDetailText);
        TextView offerDetailTitle = findViewById(R.id.offerDetailTitle);
        Button reservationButton = findViewById(R.id.reservationButton);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        Bundle bundle = getIntent().getExtras();
        offerDetailTitle.setText(bundle.getString("Title"));
        offerDetailText.setText(bundle.getString("Description"));
        String imageID = bundle.getString("Image");


        StorageReference storageRef = storage.getReferenceFromUrl("gs://fir-db-52ce4.appspot.com/images/" + imageID + ".jpg");
        long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> offerDetailImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));

        backToOffers.setOnClickListener(view -> onBackPressed());
        reservationButton.setOnClickListener(view -> {
            Intent intent = new Intent(OfferDetailActivity.this, GetReservationActivity.class);
            intent.putExtra("DateOut", bundle.getString("DateOut"));
            intent.putExtra("DateIn", bundle.getString("DateIn"));
            intent.putExtra("Title", bundle.getString("Title"));
            intent.putExtra("OfferID", bundle.getString("OfferID"));
            intent.putExtra("Rooms", bundle.getInt("Rooms"));
            intent.putExtra("Price", bundle.getInt("Price"));
            intent.putExtra("AvailableRooms", bundle.getInt("AvailableRooms"));

            startActivity(intent);
        });
    }
}