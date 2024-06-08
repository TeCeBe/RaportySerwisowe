package com.ctrlaltelite.raportyserwisowe;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;



public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {
    private List<Report> reports;

    public ReportsAdapter(List<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reports_list, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reports.get(position);
        holder.title.setText(report.getTitle());
        holder.content.setText(report.getContent());
        holder.date.setText(report.getDate());
        holder.time.setText(report.getTime());
        holder.place.setText(report.getPlace());
        holder.userId.setText(report.getUserId());


    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView date;
        TextView time;
        TextView place;
        TextView userId;


        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reportTitle);
            content = itemView.findViewById(R.id.reportContent);
            date = itemView.findViewById(R.id.editTextDate);
            time = itemView.findViewById(R.id.editTextTime);
            place = itemView.findViewById(R.id.reportPlace);


        }
    }
}
