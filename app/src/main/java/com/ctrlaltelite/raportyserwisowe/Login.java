package com.ctrlaltelite.raportyserwisowe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Button loginButton = findViewById(R.id.button);
        Button registerButton = findViewById(R.id.button2);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
            EditText passwordEditText = findViewById(R.id.editTextTextPassword);

            if (emailEditText != null && passwordEditText != null) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        });
            } else {
                // One of the EditTexts is null, log error or show error message
                Toast.makeText(Login.this, "Please enter email and password.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            // User is signed in, proceed to next activity
            Toast.makeText(Login.this, "Authentication successful.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, MainView.class);
            startActivity(intent);
        } else {
            // User is not signed in, show error message
            Toast.makeText(Login.this, "Please sign in to continue.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
