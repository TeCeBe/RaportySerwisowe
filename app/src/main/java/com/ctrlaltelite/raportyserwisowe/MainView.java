package com.ctrlaltelite.raportyserwisowe;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainView extends AppCompatActivity {

    private List<Report> reportList = new ArrayList<>();
    private ReportsAdapter reportsAdapter;

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

        // Initialize and set up the report list
        ListView reportsListView = findViewById(R.id.reportsListView);
        reportsAdapter = new ReportsAdapter(this, reportList);
        reportsListView.setAdapter(reportsAdapter);

        // Load reports
        loadReports();
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

    private void loadReports() {
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("reports");
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportList.clear();
                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    Report report = reportSnapshot.getValue(Report.class);
                    reportList.add(report);
                }
                reportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}
