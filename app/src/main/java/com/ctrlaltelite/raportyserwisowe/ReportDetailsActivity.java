package com.ctrlaltelite.raportyserwisowe;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ctrlaltelite.raportyserwisowe.R;
public class ReportDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        // Get the report details from the intent
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String place = getIntent().getStringExtra("place");

        // Set the report details to the views
        TextView titleTextView = findViewById(R.id.reportTitleTextView);
        TextView contentTextView = findViewById(R.id.reportContentTextView);
        TextView dateTextView = findViewById(R.id.reportDateTextView);
        TextView timeTextView = findViewById(R.id.reportTimeTextView);
        TextView placeTextView = findViewById(R.id.reportPlaceTextView);

        titleTextView.setText(title);
        contentTextView.setText(content);
        dateTextView.setText(date);
        timeTextView.setText(time);
        placeTextView.setText(place);
    }
}