package com.ctrlaltelite.raportyserwisowe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {
    private Context context;
    private List<Report> reports;
    private FirebaseFirestore db ;

    public ReportsAdapter(Context context, List<Report> reports) {
        this.context = context;
        this.reports = reports;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_report_details, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reports.get(position);
        holder.titleTextView.setText(report.getTitle());
        holder.contentTextView.setText(report.getContent());
        holder.dateTextView.setText(report.getDate());
        holder.timeTextView.setText(report.getTime());
        holder.placeTextView.setText(report.getPlace());

        // Add a click listener for the report item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the ReportDetailsActivity
                Intent intent = new Intent(context, ReportDetailsActivity.class);

                // Pass the report details to the ReportDetailsActivity
                intent.putExtra("title", report.getTitle());
                intent.putExtra("content", report.getContent());
                intent.putExtra("date", report.getDate());
                intent.putExtra("time", report.getTime());
                intent.putExtra("place", report.getPlace());

                // Start the ReportDetailsActivity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView placeTextView;
        Button deleteButton;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            titleTextView = itemView.findViewById(R.id.reportTitleTextView);
            contentTextView = itemView.findViewById(R.id.reportContentTextView);
            dateTextView = itemView.findViewById(R.id.reportDateTextView);
            timeTextView = itemView.findViewById(R.id.reportTimeTextView);
            placeTextView = itemView.findViewById(R.id.reportPlaceTextView);
        }

    }

}
