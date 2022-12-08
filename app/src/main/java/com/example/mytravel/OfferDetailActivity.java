package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class OfferDetailActivity extends AppCompatActivity {
    private ImageView backToOffers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        backToOffers = findViewById(R.id.backToOffers);

        backToOffers.setOnClickListener(view -> onBackPressed());
    }
}