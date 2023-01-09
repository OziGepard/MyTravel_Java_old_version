package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.mytravel.databinding.ActivityNotificationDetailBinding;

public class NotificationDetailActivity extends AppCompatActivity {

    ActivityNotificationDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String message = getIntent().getStringExtra("Message");

        binding.notificationMessage.setText(message);

        binding.backNotification.setOnClickListener(view -> onBackPressed());


    }
}