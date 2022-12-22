package com.example.mytravel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText nameRegsiterField, lastnameRegisterField, emailRegisterField, passwordRegisterField, phoneRegisterField;
    private FirebaseAuth fAuth;
    private String name;
    private String lastname;
    private FirebaseUser user;
    private UserProfileChangeRequest profileChangeRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImageView backToLogin = findViewById(R.id.backToLogin);
        nameRegsiterField =     findViewById(R.id.nameRegisterField);
        lastnameRegisterField = findViewById(R.id.lastnameRegisterField);
        emailRegisterField =    findViewById(R.id.emailRegisterField);
        passwordRegisterField = findViewById(R.id.passwordRegisterField);
        phoneRegisterField =    findViewById(R.id.phoneRegisterField);
        Button registerButton = findViewById(R.id.registerButton);

        fAuth =                 FirebaseAuth.getInstance();

        backToLogin.setOnClickListener(view -> onBackPressed());

        registerButton.setOnClickListener(view -> checkIsExists());
    }

    private void checkIsExists() {
        RegisterValidation registerValidation = new RegisterValidation();
        name = Objects.requireNonNull(nameRegsiterField.getText()).toString();
        lastname = Objects.requireNonNull(lastnameRegisterField.getText()).toString();
        String email = Objects.requireNonNull(emailRegisterField.getText()).toString();
        String password = Objects.requireNonNull(passwordRegisterField.getText()).toString();
        String phonenumber = Objects.requireNonNull(phoneRegisterField.getText()).toString();

        if(!registerValidation.nameValidation(name))
        {
            nameRegsiterField.setError("Pole z imieniem nie może:\nByć puste\nPrzekraczać 30 znaków");
            return;
        }
        if(!registerValidation.lastnameValidation(lastname))
        {
            lastnameRegisterField.setError("Pole z nazwiskiem nie może:\nByć puste\nPrzekraczać 30 znaków");
            return;
        }

        if(!registerValidation.emailValidation(email))
        {
            emailRegisterField.setError("Pole z e-mailem \nnie spełnia założeń");
            return;
        }
        if(!registerValidation.passwordValidation(password))
        {
            passwordRegisterField.setError("Pole z hasłem musi zawierać przynajmniej:\n1 dużą literę\n1 małą literę\n1 cyfrę\n1 znak specjalny [!@#$%/]\nZawierać się w przedziale 8-30 znaków\n");
            return;
        }
        if(!registerValidation.phoneValidation(phonenumber))
        {
            phoneRegisterField.setError("Pole z telefonem:\nNie może być puste\nMusi zawierać 9 cyfr");
            return;
        }
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                user = FirebaseAuth.getInstance().getCurrentUser();
                profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name + " " + lastname).build();
                user.updateProfile(profileChangeRequest);
                System.out.println(user.getDisplayName());
                Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(RegisterActivity.this, "Użytkownik o podanym e-mailu już istnieje!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}