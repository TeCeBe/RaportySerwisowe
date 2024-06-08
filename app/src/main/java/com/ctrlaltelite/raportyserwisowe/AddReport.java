package com.ctrlaltelite.raportyserwisowe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private FusedLocationProviderClient fusedLocationClient;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

        addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFirestore(editTitle.getText().toString(), editContent.getText().toString(), editTextDate.getText().toString(), editTextTime.getText().toString(), editPlace.getText().toString());
            }
        });

        Button getLocationButton = findViewById(R.id.button5);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AddReport.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddReport.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request the necessary permissions
                    ActivityCompat.requestPermissions(AddReport.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                } else {
                    // Get the last known location
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(AddReport.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        // Logic to handle location object
                                        Geocoder geocoder = new Geocoder(AddReport.this, Locale.getDefault());
                                        try {
                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            Address address = addresses.get(0);
                                            // Here you can get the address
                                            String addressText = address.getAddressLine(0);
                                            // Set the address to the place EditText
                                            editPlace.setText(addressText);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                }
            }
        });

        editContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Here you can use the imageUri to display the selected image or upload it to Firebase Storage
        }
    }
}