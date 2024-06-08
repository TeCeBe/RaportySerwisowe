package com.ctrlaltelite.raportyserwisowe;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainView extends AppCompatActivity {

    private RecyclerView reportsRecyclerView;
    private ReportsAdapter reportsAdapter;
    private List<Report> reports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String email = user.getEmail();

            TextView uidTextView = findViewById(R.id.nameTextView);
            TextView emailTextView = findViewById(R.id.emailTextView);

            FloatingActionButton addReportButton = findViewById(R.id.addReportButton);

            uidTextView.setText(uid);
            emailTextView.setText(email);

            addReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainView.this, AddReport.class);
                    startActivity(intent);
                }
            });

            reportsRecyclerView = findViewById(R.id.reportsRecyclerView);
            reportsAdapter = new ReportsAdapter(this, reports);
            reportsRecyclerView.setAdapter(reportsAdapter);
            reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            loadReportsFromFirestore();
        }

        View optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainView.this, v);
                popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            handleLogout();
            return true;
        } else if (id == R.id.editProfile) {
            handleEditProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainView.this, Login.class));
        finish();
    }

    private void handleEditProfile() {
        startActivity(new Intent(MainView.this, EditProfile.class));
    }

    private void loadReportsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reports.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Report report = document.toObject(Report.class);
                            reports.add(report);
                        }
                        reportsAdapter.notifyDataSetChanged();
                    } else {
                        // Handle errors here
                    }
                });
    }
}