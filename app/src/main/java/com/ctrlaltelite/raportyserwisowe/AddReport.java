package com.ctrlaltelite.raportyserwisowe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddReport extends AppCompatActivity {

    EditText editTextDate;
    EditText editTextTime;
    private Button buttonUpdate;
    private Button buttonSMS;

    EditText editTitle;
    EditText editContent;
    EditText editPlace;
    FirebaseFirestore db;

    FirebaseAuth mAuth;
    private Button addReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        buttonUpdate = findViewById(R.id.setTimeButton);
        editTitle = findViewById(R.id.reportTitle);
        editContent = findViewById(R.id.reportContent);
        editPlace = findViewById(R.id.reportPlace);
        buttonSMS = findViewById(R.id.sendSMS);
        addReport = findViewById(R.id.buttonAddReport);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDateTime();
            }
        });

        buttonSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
            }
        });

        buttonSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
            }
        });
        addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFirestore(editTitle.getText().toString(), editContent.getText().toString(), editTextDate.getText().toString(), editTextTime.getText().toString(), editPlace.getText().toString());
            }
        });
    }

    private void updateDateTime() {
        Date now = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(now);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(now);

        editTextDate.setText(currentDate);
        editTextTime.setText(currentTime);
    }

    void sendText() {
        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        String place = editContent.getText().toString();

        String message = "Raport: " + title + "\nSzczegóły: " + content + "\nData: " + date + " " + time + "\nMiejsce: " + place + "\nWygenerowano w aplikacji Raporty serwisowe.";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    void addToFirestore(String title, String content, String date, String time, String place) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Utwórz mapę z danymi do dodania
            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("content", content);
            data.put("date", date);
            data.put("time", time);
            data.put("place", place);
            data.put("userId", userId);

            // Dodaj dane do kolekcji "datetime"
            db.collection("reports")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddReport.this, "Pomyślnie dodano raport", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddReport.this, "Błąd podczas dodawania raportu", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(AddReport.this, "Użytkownik niezalogowany", Toast.LENGTH_SHORT).show();
        }

    }
}