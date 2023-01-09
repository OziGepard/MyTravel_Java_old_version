package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.mytravel.databinding.ActivityReceivePasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ReceivePasswordActivity extends AppCompatActivity {

    ActivityReceivePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_password);

        binding = ActivityReceivePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProgressBar progressBar = new ProgressBar(ReceivePasswordActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);
        binding.receiveLayout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);

        binding.backToLoginRegister.setOnClickListener(view -> onBackPressed());

        binding.receiveButton.setOnClickListener(view -> {
            if(!binding.emailToReceive.getText().toString().isEmpty())
            {
                System.out.println("tutaj");
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String email = binding.emailToReceive.getText().toString();

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            binding.receiveInfo.setVisibility(View.VISIBLE);
                            if(task.isSuccessful())
                            {
                                binding.receiveInfo.setText("Sprawdź swoją pocztę, aby dokończyć\n proces resetowania hasła!");
                            }
                            else
                            {
                                binding.receiveInfo.setText("Podano niepoprawną nazwę e-mail!");
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.receiveInfo.setVisibility(View.INVISIBLE);
    }
}