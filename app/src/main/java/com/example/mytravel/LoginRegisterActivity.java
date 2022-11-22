package com.example.mytravel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginRegisterActivity extends AppCompatActivity {
private ImageView backToFragment;
private GoogleSignInOptions gso;
private GoogleSignInClient gsc;
private TextView signInText;
private Button loginButton;
private TextInputEditText emailField, passwordField;
private FirebaseAuth fAuth;

ImageView googleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        fAuth = FirebaseAuth.getInstance();
        googleButton = findViewById(R.id.googleButton);
        backToFragment = findViewById(R.id.backToFragment);
        signInText = findViewById(R.id.signInText);
        loginButton = findViewById(R.id.loginButton);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsCorrect();
            }
        });

    }

    private void checkIsCorrect() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Podano błędny e-mail lub hasło", Toast.LENGTH_SHORT).show();
        }

        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginRegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(LoginRegisterActivity.this, "Podano błędny e-mail lub hasło", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LoginRegisterActivity.this, task1.getException().getMessage(), Toast.LENGTH_SHORT);
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }


}