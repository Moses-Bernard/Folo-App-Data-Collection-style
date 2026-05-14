package com.folo.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.Schedule;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;

public class ScheduleActivity extends AppCompatActivity {
    private int womanId;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_schedule);
        womanId = getIntent().getIntExtra("woman_id", -1);
        RecyclerView rv = findViewById(R.id.rvSchedules);
        rv.setLayoutManager(new LinearLayoutManager(this));
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        AppDatabase.getInstance(this).scheduleDao().getUpcomingSchedules(today).observe(this, list -> {
            rv.setAdapter(new ScheduleAdapter(list));
        });
    }

    static class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.VH> {
        private final List<Schedule> list;
        ScheduleAdapter(List<Schedule> list) { this.list = list; }
        @NonNull @Override public VH onCreateViewHolder(@NonNull android.view.ViewGroup p, int t) {
            return new VH(android.view.LayoutInflater.from(p.getContext()).inflate(R.layout.item_schedule, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            Schedule s = list.get(pos);
            ((TextView)h.itemView.findViewById(R.id.tvScheduleDate)).setText(s.scheduledDate);
            ((TextView)h.itemView.findViewById(R.id.tvSchedulePurpose)).setText(s.purpose);
            ((TextView)h.itemView.findViewById(R.id.tvScheduleStage)).setText(s.stage);
            ((TextView)h.itemView.findViewById(R.id.tvScheduleStatus)).setText(s.completed ? "Done" : "Pending");
        }
        @Override public int getItemCount() { return list.size(); }
        static class VH extends RecyclerView.ViewHolder { VH(android.view.View v) { super(v); } }
    }
}
