package com.example.mytravel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ReservationFragment extends Fragment {

    private ArrayList<ReservationData> reservationsData;
    private ReservationAdapter reservationAdapter;
    private String reservationID;
    private String title;
    private String isConfirmed;
    private int rooms;
    private int price;
    private final String TAG = "ReservationFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference resRef = db.collection("reservations");

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.requireContext());
        FirebaseUser currentUser = fAuth.getCurrentUser();

        String userEmail;

        RecyclerView reservationRecyclerView = view.findViewById(R.id.reservationRecyclerView);
        TextView reservationNoLoggedIn = view.findViewById(R.id.reservationNoLoggedIn);

        reservationRecyclerView.setHasFixedSize(true);
        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        reservationsData = new ArrayList<>();
        reservationAdapter = new ReservationAdapter(this.getContext(), reservationsData);
        reservationRecyclerView.setAdapter(reservationAdapter);

        if(account == null && currentUser == null)
        {
            reservationNoLoggedIn.setVisibility(View.VISIBLE);
        }
        else
        {
            userEmail = getUserEmail(account, currentUser);
            reservationNoLoggedIn.setVisibility(View.INVISIBLE);
            resRef
                    .whereEqualTo("user_email", userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                title = Objects.requireNonNull(document.get("title")).toString();
                                rooms = Integer.parseInt(Objects.requireNonNull(document.get("reserved_rooms")).toString()) ;
                                price = Integer.parseInt(Objects.requireNonNull(document.get("price")).toString());
                                isConfirmed = Objects.requireNonNull(document.get("is_confirmed")).toString();
                                reservationID = document.getId();

                                ReservationData reservation = new ReservationData(title, rooms, price, reservationID, isConfirmed);
                                reservationsData.add(reservation);

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            reservationAdapter.notifyItemInserted(reservationsData.size() - 1);
                        }
                        else
                        {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    });
        }

        return view;
    }

    private String getUserEmail(GoogleSignInAccount account, FirebaseUser currentUser) {
        if(account != null)
        {
            return account.getEmail();
        }
        else
        {
            return currentUser.getEmail();
        }
    }
}