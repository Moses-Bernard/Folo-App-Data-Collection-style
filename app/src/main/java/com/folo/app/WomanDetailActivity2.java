package com.folo.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.Schedule;
import com.folo.app.data.Visit;
import com.folo.app.data.Woman;
import java.util.List;

public class WomanDetailActivity2 extends AppCompatActivity {
    private int womanId;
    private TextView tvName, tvPhone, tvAddress, tvGps, tvRisk;
    private Button btnEdit, btnNavigate, btnAddHealth, btnAddVisit, btnAddDelivery;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_woman_detail);
        womanId = getIntent().getIntExtra("woman_id", -1);
        tvName = findViewById(R.id.tvDetailName); tvPhone = findViewById(R.id.tvDetailPhone);
        tvAddress = findViewById(R.id.tvDetailAddress); tvGps = findViewById(R.id.tvDetailGps);
        tvRisk = findViewById(R.id.tvDetailRisk);
        btnEdit = findViewById(R.id.btnEditWoman); btnNavigate = findViewById(R.id.btnNavigate);
        btnAddHealth = findViewById(R.id.btnAddHealth); btnAddVisit = findViewById(R.id.btnAddVisit);
        btnAddDelivery = findViewById(R.id.btnAddDelivery);

        loadWoman();
        setupRecycler(R.id.rvVisits, AppDatabase.getInstance(this).visitDao().getVisitsForWoman(womanId), "visit");
        setupRecycler(R.id.rvSchedules, AppDatabase.getInstance(this).scheduleDao().getSchedulesForWoman(womanId), "schedule");

        btnEdit.setOnClickListener(v -> startActivity(new Intent(this, EditWomanActivity.class).putExtra("woman_id", womanId)));
        btnNavigate.setOnClickListener(v -> navigateToWoman());
        btnAddHealth.setOnClickListener(v -> startActivity(new Intent(this, HealthCheckActivity2.class).putExtra("woman_id", womanId)));
        btnAddVisit.setOnClickListener(v -> startActivity(new Intent(this, ScheduleActivity.class).putExtra("woman_id", womanId)));
        btnAddDelivery.setOnClickListener(v -> startActivity(new Intent(this, DeliveryRecordActivity.class).putExtra("woman_id", womanId)));
    }

    private void loadWoman() {
        new Thread(() -> {
            Woman w = AppDatabase.getInstance(this).womanDao().getWomanById(womanId);
            runOnUiThread(() -> {
                if (w == null) return;
                tvName.setText(w.fullName); tvPhone.setText(w.phone);
                tvAddress.setText(w.address + ", " + w.area + ", " + w.lga);
                tvGps.setText(String.format("%.6f, %.6f (±%.1fm)", w.latitude, w.longitude, w.accuracy));
                tvRisk.setText("Risk: " + w.riskLevel);
                tvRisk.setTextColor(getColor("HIGH".equals(w.riskLevel) ? R.color.danger_red : R.color.success_green));
            });
        }).start();
    }

    private void navigateToWoman() {
        new Thread(() -> {
            Woman w = AppDatabase.getInstance(this).womanDao().getWomanById(womanId);
            if (w != null) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://maps.google.com/maps?daddr=" + w.latitude + "," + w.longitude)));
            }
        }).start();
    }

    private <T> void setupRecycler(int id, androidx.lifecycle.LiveData<List<T>> data, String type) {
        RecyclerView rv = findViewById(id);
        rv.setLayoutManager(new LinearLayoutManager(this));
        data.observe(this, list -> rv.setAdapter(new SimpleAdapter<>(list, type)));
    }

    static class SimpleAdapter<T> extends RecyclerView.Adapter<SimpleAdapter.VH> {
        private final List<T> list; private final String type;
        SimpleAdapter(List<T> list, String type) { this.list = list; this.type = type; }
        @NonNull @Override public VH onCreateViewHolder(@NonNull android.view.ViewGroup p, int t) {
            return new VH(android.view.LayoutInflater.from(p.getContext()).inflate(
                type.equals("visit") ? R.layout.item_visit : R.layout.item_schedule, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            Object item = list.get(pos);
            if (item instanceof Visit) {
                Visit v = (Visit) item; ((TextView)h.itemView.findViewById(R.id.tvVisitDate)).setText(v.visitDate);
                ((TextView)h.itemView.findViewById(R.id.tvVisitType)).setText(v.visitType);
            } else if (item instanceof Schedule) {
                Schedule s = (Schedule) item; ((TextView)h.itemView.findViewById(R.id.tvScheduleDate)).setText(s.scheduledDate);
                ((TextView)h.itemView.findViewById(R.id.tvSchedulePurpose)).setText(s.purpose);
                ((TextView)h.itemView.findViewById(R.id.tvScheduleStage)).setText(s.stage);
            }
        }
        @Override public int getItemCount() { return list.size(); }
        static class VH extends RecyclerView.ViewHolder { VH(android.view.View v) { super(v); } }
    }
}
