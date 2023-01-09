package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.mytravel.databinding.ActivityNotificationBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notRef = db.collection("notifications");
    private final Map<String, Object> notification = new HashMap<>();

    private ArrayList<NotificationData> notificationData;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backActivity.setOnClickListener(view -> onBackPressed());

        RecyclerView notificationRecyclerView = binding.notificationRecyclerView;
        notificationRecyclerView.setHasFixedSize(true);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationData = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationData);
        notificationRecyclerView.setAdapter(notificationAdapter);
        getNotifications();
    }

    private void getNotifications() {
        String userEmail = getUserEmail();
        notRef
                .whereEqualTo("user_email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot document : task.getResult())
                        {
                            String title = document.get("title").toString();
                            String message = document.get("message").toString();
                            String email = document.get("user_email").toString();
                            String isRead = document.get("is_read").toString();
                            String date = document.get("date").toString();
                            String notificationID = document.getId();

                            NotificationData notificationSingleData = new NotificationData(title, message, email, isRead, date, notificationID);
                            notificationData.add(notificationSingleData);
                        }
                        notificationAdapter.notifyItemInserted(notificationData.size() - 1);
                    }
                });
    }

    private String getUserEmail() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser currentUser = fAuth.getCurrentUser();

        if(account != null)
        {
            return account.getEmail();
        }
        else
        {
            return currentUser.getEmail();
        }
    }

    public void createNotification(String title, String message, String email) {
        String todaysDate = LocalDate.now().toString();
        notification.put("title", title);
        notification.put("message", message);
        notification.put("user_email", email);
        notification.put("is_read", "false");
        notification.put("date", todaysDate);

        notRef.document().set(notification);
    }
}