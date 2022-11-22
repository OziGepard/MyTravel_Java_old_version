package com.example.mytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private ImageView backToLogin;
    private TextInputEditText nameRegsiterField, lastnameRegisterField, emailRegisterField, passwordRegisterField, phoneRegisterField;
    private Button registerButton;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        backToLogin =           findViewById(R.id.backToLogin);
        nameRegsiterField =     findViewById(R.id.nameRegisterField);
        lastnameRegisterField = findViewById(R.id.lastnameRegisterField);
        emailRegisterField =    findViewById(R.id.emailRegisterField);
        passwordRegisterField = findViewById(R.id.passwordRegisterField);
        phoneRegisterField =    findViewById(R.id.phoneRegisterField);
        registerButton =        findViewById(R.id.registerButton);

        fAuth =                 FirebaseAuth.getInstance();

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsExists();
            }
        });
    }

    private void checkIsExists() {
        RegisterValidation registerValidation = new RegisterValidation();
        String name = nameRegsiterField.getText().toString();
        String lastname = lastnameRegisterField.getText().toString();
        String email = emailRegisterField.getText().toString();
        String password = passwordRegisterField.getText().toString();
        String phonenumber = phoneRegisterField.getText().toString();

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
        //TODO REGEX EMAIL
//        if(!registerValidation.emailValidation(email))
//        {
//            emailRegisterField.setError("Pole z nazwiskiem:\nNie może być puste\nMusi zaczynać się z \nwielkiej litery");
//            return;
//        }
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
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Użytkownik o podanym e-mailu już istnieje!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}