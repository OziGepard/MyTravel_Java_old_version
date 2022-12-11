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

public class ReservationActivity extends AppCompatActivity {

    private ImageView backToDetail;
    private TextView dateOutInText, destinationText, roomsText, priceText;
    private Button payButton;

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private DocumentReference offerDoc;
    private CollectionReference resCol;

    private String offerID;
    private String userEmail;
    private String dateOut;
    private String dateIn;
    private int availableRooms;
    private int rooms;
    private int roomsDifferenc;
    private Map<String, Object> reservation = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        db = FirebaseFirestore.getInstance();


        fAuth = FirebaseAuth.getInstance();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser currentUser = fAuth.getCurrentUser();

        backToDetail = findViewById(R.id.backToDetail);
        dateOutInText = findViewById(R.id.dateOutInText);
        destinationText = findViewById(R.id.destinationText);
        roomsText = findViewById(R.id.roomsText);
        priceText = findViewById(R.id.priceText);
        payButton = findViewById(R.id.payButton);


        Bundle bundle = getIntent().getExtras();

        offerID = bundle.getString("OfferID");
        availableRooms = bundle.getInt("AvailableRooms");
        dateOut = bundle.getString("DateOut");
        dateIn = bundle.getString("DateIn");
        rooms = bundle.getInt("Rooms");
        dateOutInText.setText("Data wyjazdu: " + dateOut + " - " + dateIn);
        roomsText.setText("Ilość pokoi: " + rooms);
        destinationText.setText("Miejsce wyjazdu: " + bundle.getString("Title"));
        priceText.setText("Cena do zapłaty: " + bundle.getInt("Price"));

        roomsDifferenc = availableRooms - bundle.getInt("Rooms");

        offerDoc = db.collection("offers").document(offerID);
        resCol = db.collection("reservations");

        payButton.setOnClickListener(view -> {

            if(account == null && currentUser == null)
            {
                Toast.makeText(this, "Musisz być zalogowany, aby dokonać rezerwacji!", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (account != null)
            {
                userEmail = account.getEmail();
            }
            else
            {
                userEmail = currentUser.getEmail();
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

            resCol.document().set(reservation);

            offerDoc.
                    update("available_rooms", roomsDifferenc);
            Toast.makeText(ReservationActivity.this, "Dziękujemy za dokonanie rezerwacji!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ReservationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        });


    }
}