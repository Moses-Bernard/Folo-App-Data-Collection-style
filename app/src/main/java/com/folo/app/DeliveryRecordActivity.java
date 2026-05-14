package com.folo.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.DeliveryRecord;
import com.folo.app.role.RoleManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeliveryRecordActivity extends AppCompatActivity {
    private int womanId;
    private EditText etDate, etPlace, etOutcome, etGender, etWeight, etComplications, etAttendant;
    private Button btnSave;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_delivery_record);
        womanId = getIntent().getIntExtra("woman_id", -1);
        etDate = findViewById(R.id.etDelDate); etPlace = findViewById(R.id.etDelPlace);
        etOutcome = findViewById(R.id.etDelOutcome); etGender = findViewById(R.id.etBabyGender);
        etWeight = findViewById(R.id.etBabyWeight); etComplications = findViewById(R.id.etComplications);
        etAttendant = findViewById(R.id.etAttendant); btnSave = findViewById(R.id.btnSaveDelivery);
        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        DeliveryRecord d = new DeliveryRecord();
        d.womanId = womanId; d.deliveryDate = etDate.getText().toString().trim();
        d.deliveryPlace = etPlace.getText().toString().trim();
        d.deliveryOutcome = etOutcome.getText().toString().trim();
        d.babyGender = etGender.getText().toString().trim();
        d.babyWeightKg = parse(etWeight);
        d.complications = etComplications.getText().toString().trim();
        d.attendantName = etAttendant.getText().toString().trim();
        d.recordedBy = new RoleManager(this).getCurrentRole().getDisplayName();
        d.recordedAt = System.currentTimeMillis(); d.synced = false;

        new Thread(() -> {
            AppDatabase.getInstance(this).deliveryDao().insert(d);
            runOnUiThread(() -> { Toast.makeText(this, "Delivery recorded", Toast.LENGTH_SHORT).show(); finish(); });
        }).start();
    }

    private double parse(EditText e) { try { return Double.parseDouble(e.getText().toString().trim()); } catch (Exception ex) { return 0; } }
}
