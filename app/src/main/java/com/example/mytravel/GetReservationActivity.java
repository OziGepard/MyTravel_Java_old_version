package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GetReservationActivity extends AppCompatActivity {

    private DocumentReference offerDoc;
    private CollectionReference resCol;

    private String offerID;
    private String userEmail;
    private String dateOut;
    private String dateIn;
    private String title;
    private int rooms;
    private int roomsDifferenc;
    private int price;
    private final Map<String, Object> reservation = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser currentUser = fAuth.getCurrentUser();

        ImageView backToDetail = findViewById(R.id.backToDetail);
        TextView dateOutInText = findViewById(R.id.dateOutInText);
        TextView destinationText = findViewById(R.id.destinationText);
        TextView roomsText = findViewById(R.id.roomsText);
        TextView priceText = findViewById(R.id.priceText);
        Button payButton = findViewById(R.id.payButton);


        Bundle bundle = getIntent().getExtras();

        offerID = bundle.getString("OfferID");
        int availableRooms = bundle.getInt("AvailableRooms");
        dateOut = bundle.getString("DateOut");
        dateIn = bundle.getString("DateIn");
        rooms = bundle.getInt("Rooms");
        price = bundle.getInt("Price");
        title = bundle.getString("Title");


        dateOutInText.setText("Data wyjazdu: " + dateOut + " - " + dateIn);
        roomsText.setText("Ilość pokoi: " + rooms);
        destinationText.setText("Miejsce wyjazdu: " + title);
        priceText.setText("Cena do zapłaty: " + price);

        roomsDifferenc = availableRooms - rooms;

        offerDoc = db.collection("offers").document(offerID);
        resCol = db.collection("reservations");

        payButton.setOnClickListener(view -> {

            if(account == null && currentUser == null)
            {
                Toast.makeText(this, "Musisz być zalogowany, aby dokonać rezerwacji!", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                userEmail = ((account == null) ? currentUser.getEmail() : account.getEmail());
            }
            if(roomsDifferenc < 0)
            {
                Toast.makeText(this, "Niestety oferta nie jest już dostępna w podanej liczbie pokoi.", Toast.LENGTH_SHORT).show();
                return;
            }
            reservation.put("user_email", userEmail);
            reservation.put("offer_ID", offerID);
            reservation.put("date_out", dateOut);
            reservation.put("date_in", dateIn);
            reservation.put("reserved_rooms", rooms);
            reservation.put("price", price);
            reservation.put("title", title);
            reservation.put("is_confirmed", "");

            resCol.document().set(reservation);

            offerDoc.
                    update("available_rooms", roomsDifferenc);
            Toast.makeText(GetReservationActivity.this, "Dziękujemy za dokonanie rezerwacji!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(GetReservationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        });

        backToDetail.setOnClickListener(view -> onBackPressed());


    }
}