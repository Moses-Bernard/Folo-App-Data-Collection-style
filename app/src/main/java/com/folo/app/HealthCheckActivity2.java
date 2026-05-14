package com.folo.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.HealthRecord;
import com.folo.app.role.RoleManager;

public class HealthCheckActivity2 extends AppCompatActivity {
    private int womanId;
    private EditText etBp, etWeight, etSymptoms, etConditions;
    private Button btnSave;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_health_check);
        womanId = getIntent().getIntExtra("woman_id", -1);
        etBp = findViewById(R.id.etBp); etWeight = findViewById(R.id.etWeight);
        etSymptoms = findViewById(R.id.etSymptoms); etConditions = findViewById(R.id.etConditions);
        btnSave = findViewById(R.id.btnSaveHealth);
        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        HealthRecord h = new HealthRecord();
        h.womanId = womanId;
        h.bloodPressure = etBp.getText().toString().trim();
        h.weightKg = parseDouble(etWeight);
        h.symptoms = etSymptoms.getText().toString().trim();
        h.conditions = etConditions.getText().toString().trim();
        h.recordedBy = new RoleManager(this).getCurrentUser();
        h.recordedAt = System.currentTimeMillis();
        h.synced = false;

        new Thread(() -> {
            AppDatabase.getInstance(this).healthRecordDao().insert(h);
            runOnUiThread(() -> { Toast.makeText(this, "Health record saved", Toast.LENGTH_SHORT).show(); finish(); });
        }).start();
    }

    private double parseDouble(EditText e) { try { return Double.parseDouble(e.getText().toString().trim()); } catch (Exception ex) { return 0; } }
}
