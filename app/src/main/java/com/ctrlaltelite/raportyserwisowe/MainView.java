package com.ctrlaltelite.raportyserwisowe;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainView extends AppCompatActivity {

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

            // Find the options button and set an OnClickListener
            View optionsButton = findViewById(R.id.optionsButton);
            optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a new PopupMenu and inflate it with the options menu
                    PopupMenu popup = new PopupMenu(MainView.this, v);
                    popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());

                    // Set an OnMenuItemClickListener to handle menu item clicks
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.logout) {
                                // Handle logout
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainView.this, Login.class));
                                finish();
                                return true;
                            } else if (id == R.id.editProfile) {
                                // Handle profile editing
                                startActivity(new Intent(MainView.this, EditProfile.class));
                                return true;
                            }
                            return false;
                        }
                    });

                    // Show the PopupMenu
                    popup.show();
                }
            });
        }
    }
}