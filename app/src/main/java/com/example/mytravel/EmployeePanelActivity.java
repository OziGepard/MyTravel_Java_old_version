package com.example.mytravel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytravel.databinding.ActivityEmployeePanelBinding;
import com.example.mytravel.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EmployeePanelActivity extends AppCompatActivity {

    ActivityEmployeePanelBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    private final Map<String, Object> offer = new HashMap<>();
    private ProgressDialog progressDialog;
    private CollectionReference offerCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeePanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference offersRef = db.collection("offers");

        binding.addImage.setOnClickListener(view -> {

            selectImage();

        });
        binding.addOffer.setOnClickListener(view -> {

            if(checkIsCorrect())
            {
                String filename = uploadImage();
                offer.put("available_rooms", Integer.parseInt(binding.availableRoomsTravel.getText().toString()));
                offer.put("city", binding.cityTravel.getText().toString());
                offer.put("country", binding.countryTravel.getText().toString());
                offer.put("description", binding.descriptionTravel.getText().toString());
                offer.put("image", filename);
                offer.put("people_per_room", Integer.parseInt(binding.peopolePerRoomTravel.getText().toString()));
                offer.put("price_per_day", Integer.parseInt(binding.priceTravel.getText().toString()));
                offer.put("title", binding.titleTravel.getText().toString());

                offersRef.document().set(offer);
            }
            else
            {
                Toast.makeText(this, "Uzupełnij brakujące informacje!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean checkIsCorrect() {
        ArrayList<Boolean> checkList = new ArrayList<>();

        if(binding.imageOfPlace.getDrawable() == null)
        {
            return false;
        }
        checkList.add(Objects.requireNonNull(binding.countryTravel.getText()).toString().isEmpty());
        checkList.add(Objects.requireNonNull(binding.cityTravel.getText()).toString().isEmpty());
        checkList.add(Objects.requireNonNull(binding.titleTravel.getText()).toString().isEmpty());
        checkList.add(Objects.requireNonNull(binding.availableRoomsTravel.getText()).toString().isEmpty());
        checkList.add(Objects.requireNonNull(binding.peopolePerRoomTravel.getText()).toString().isEmpty());
        checkList.add(Objects.requireNonNull(binding.priceTravel.getText()).toString().isEmpty());
        checkList.add(binding.descriptionTravel.getText().toString().isEmpty());

        for (Boolean item: checkList)
        {
            if(item)
            {
                return false;
            }
        }
        return true;

    }

    private String uploadImage()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("ss_mm_HH_dd_MM_yyyy");
        Date now = new Date();
        String filename = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + filename + ".jpg");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Dodawanie oferty do bazy...");
        progressDialog.show();

        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    binding.imageOfPlace.setImageURI(null);

                    if(progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
                    if(progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(EmployeePanelActivity.this, "Nie udało się wysłać zdjęcia na serwer", Toast.LENGTH_SHORT).show();
                });

        return filename;

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 170);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 170 && data != null && data.getData() != null){
            imageUri = data.getData();
            binding.imageOfPlace.setImageURI(imageUri);
            System.out.println(binding.imageOfPlace.getDrawable());
        }
    }
}