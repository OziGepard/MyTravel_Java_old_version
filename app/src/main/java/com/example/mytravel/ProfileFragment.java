package com.example.mytravel;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class ProfileFragment extends Fragment{
    View view;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;


    private Button loginOrRegisterButton, signout;
    private TextView welcomeText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        loginOrRegisterButton = view.findViewById(R.id.loginOrRegisterButton);
        signout = view.findViewById(R.id.signout);

        welcomeText = view.findViewById(R.id.welcomeText);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(view.getContext(), gso);


        loginOrRegisterButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), LoginRegisterActivity.class);
            startActivity(intent);
        });
        signout.setOnClickListener(view -> {
            gsc.signOut();
            onResume();

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        if(account != null)
        {
            loginOrRegisterButton.setVisibility(View.INVISIBLE);
            signout.setVisibility(View.VISIBLE);
            String Name = account.getDisplayName();
            welcomeText.setText("Zalogowano jako " + Name);
        }
        else
        {
            loginOrRegisterButton.setVisibility(View.VISIBLE);
            signout.setVisibility(View.INVISIBLE);
            welcomeText.setText("");
        }

    }

}