package com.example.mytravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment{
    View view;
    private GoogleSignInClient gsc;
    private FirebaseAuth fAuth;
    private CollectionReference employeesRef;
    GoogleSignInAccount account;
    FirebaseUser currentUser;

    private final ArrayList<String> employees = new ArrayList<>();


    private Button loginOrRegisterButton, signout, employeePanel;
    private TextView welcomeText, loggedIn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        fAuth = FirebaseAuth.getInstance();
        loginOrRegisterButton = view.findViewById(R.id.loginOrRegisterButton);
        signout = view.findViewById(R.id.signout);
        employeePanel = view.findViewById(R.id.employeePanel);
        loggedIn = view.findViewById(R.id.loggedIn);

        welcomeText = view.findViewById(R.id.welcomeText);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(view.getContext(), gso);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        employeesRef = db.collection("employees");

        employeesRef
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot document : task.getResult())
                        {
                            employees.add(document.getId());
                        }
                    }
                });

        loginOrRegisterButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), LoginRegisterActivity.class);
            startActivity(intent);
        });

        account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        currentUser = fAuth.getCurrentUser();

        signout.setOnClickListener(view -> {
            String email = ((account == null) ? currentUser.getEmail() : account.getEmail());
            for(String item : employees)
            {
                assert email != null;
                if(email.equals(item))
                {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    Map<String, Object> loggedOut = new HashMap<>();
                    loggedOut.put(dtf.format(now), "Logged_out");
                    employeesRef.document(email).update(loggedOut);
                    break;
                }
            }
            gsc.signOut();
            fAuth.signOut();
            onResume();

        });

        employeePanel.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EmployeePanelActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        //TODO - Zgłoś problem
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        currentUser = fAuth.getCurrentUser();
        String name;
        String email;
        if(account != null || currentUser != null)
        {
            loginOrRegisterButton.setVisibility(View.INVISIBLE);
            loggedIn.setVisibility(View.VISIBLE);
            signout.setVisibility(View.VISIBLE);
            name = ((account == null) ? currentUser.getDisplayName() : account.getDisplayName());
            email = ((account == null) ? currentUser.getEmail() : account.getEmail());
            welcomeText.setText(name);

            // Sprawdzanie statusu (użytkownik/pracownik) osoby zalogowanej

            for(String item : employees)
            {
                assert email != null;
                if(email.equals(item))
                {
                    employeePanel.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        else
        {
            employeePanel.setVisibility(View.INVISIBLE);
            loggedIn.setVisibility(View.INVISIBLE);
            loginOrRegisterButton.setVisibility(View.VISIBLE);
            signout.setVisibility(View.INVISIBLE);
            welcomeText.setText("");
        }
    }

}