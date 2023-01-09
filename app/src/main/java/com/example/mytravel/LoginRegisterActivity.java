package com.example.mytravel;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LoginRegisterActivity extends AppCompatActivity {
    private GoogleSignInClient gsc;
    private TextInputEditText emailField, passwordField;
    private FirebaseAuth fAuth;
    private CollectionReference employeesRef;
    private TextView forgotPassword;

    private final ArrayList<String> employees = new ArrayList<>();

    ImageView googleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        fAuth = FirebaseAuth.getInstance();
        googleButton = findViewById(R.id.googleButton);
        ImageView backToFragment = findViewById(R.id.backToFragment);
        TextView signInText = findViewById(R.id.signInText);
        Button loginButton = findViewById(R.id.loginButton);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        forgotPassword = findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginRegisterActivity.this, ReceivePasswordActivity.class);
            startActivity(intent);
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        googleButton.setOnClickListener(view -> SignIn());
        backToFragment.setOnClickListener(view -> onBackPressed());
        signInText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        loginButton.setOnClickListener(view -> checkIsCorrect());

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

    }

    private void checkIsCorrect() {
        String email = Objects.requireNonNull(emailField.getText()).toString();
        String password = Objects.requireNonNull(passwordField.getText()).toString();
        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Podano błędny e-mail lub hasło", Toast.LENGTH_SHORT).show();
        }
        else
        {
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for(String item : employees)
                    {
                        if(email.equals(item))
                        {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            Map<String, Object> loggedIn = new HashMap<>();
                            loggedIn.put(dtf.format(now), "Logged_in");
                            employeesRef.document(email).update(loggedIn);
                            break;
                        }
                    }
                    finish();
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "Podano błędny e-mail lub hasło", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void SignIn()
    {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful())
                            {
                                for(String item : employees)
                                {
                                    if(Objects.equals(account.getEmail(), item))
                                    {
                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        Map<String, Object> loggedIn = new HashMap<>();
                                        loggedIn.put(dtf.format(now), "Logged_in");
                                        employeesRef.document(Objects.requireNonNull(account.getEmail())).update(loggedIn);
                                        break;
                                    }
                                }
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LoginRegisterActivity.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }


}