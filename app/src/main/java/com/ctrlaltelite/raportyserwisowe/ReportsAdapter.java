package com.ctrlaltelite.raportyserwisowe;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReportsAdapter extends BaseAdapter {
    private Context context;
    private List<Report> reports;

    public ReportsAdapter(Context context, List<Report> reports) {
        this.context = context;
        this.reports = reports;
    }

    @Override
    public int getCount() {
        return reports.size();
    }

    @Override
    public Object getItem(int position) {
        return reports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_report_details, parent, false);
        }

        Report report = reports.get(position);

        TextView titleTextView = convertView.findViewById(R.id.reportTitleTextView);
        TextView contentTextView = convertView.findViewById(R.id.reportContentTextView);
        TextView dateTextView = convertView.findViewById(R.id.reportDateTextView);
        TextView timeTextView = convertView.findViewById(R.id.reportTimeTextView);
        TextView placeTextView = convertView.findViewById(R.id.reportPlaceTextView);

        titleTextView.setText(report.getTitle());
        contentTextView.setText(report.getContent());
        dateTextView.setText(report.getDate());
        timeTextView.setText(report.getTime());
        placeTextView.setText(report.getPlace());

        return convertView;
    }
}
