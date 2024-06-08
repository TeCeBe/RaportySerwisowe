package com.ctrlaltelite.raportyserwisowe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            EditText nameEditText = findViewById(R.id.nameEditText);
            EditText surnameEditText = findViewById(R.id.surnameEditText);
            Button saveButton = findViewById(R.id.saveButton);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = nameEditText.getText().toString();
                    String surname = surnameEditText.getText().toString();

                    db.collection("users").document(user.getUid())
                            .set(new User(name, surname))
                            .addOnSuccessListener(aVoid -> Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(EditProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show());
                }
            });
        }
    }
}