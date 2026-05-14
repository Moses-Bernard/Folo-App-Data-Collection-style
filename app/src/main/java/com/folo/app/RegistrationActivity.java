package com.folo.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.data.*;
import com.folo.app.role.RoleManager;
import com.folo.app.schedule.ScheduleGenerator;
import java.text.SimpleDateFormat;
import java.util.*;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etName, etPhone, etAddress, etLandmark, etArea, etLga, etDob, etEdd, etGravida, etPara;
    private Button btnSave, btnGps;
    private double lat, lng; private float acc;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_registration);
        etName = findViewById(R.id.etName); etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress); etLandmark = findViewById(R.id.etLandmark);
        etArea = findViewById(R.id.etArea); etLga = findViewById(R.id.etLga);
        etDob = findViewById(R.id.etDob); etEdd = findViewById(R.id.etEdd);
        etGravida = findViewById(R.id.etGravida); etPara = findViewById(R.id.etPara);
        btnSave = findViewById(R.id.btnSave); btnGps = findViewById(R.id.btnCaptureGps);
        btnGps.setOnClickListener(v -> startActivityForResult(new Intent(this, GpsCaptureActivity.class), 100));
        btnSave.setOnClickListener(v -> save());
    }

    @Override protected void onActivityResult(int rc, int res, Intent d) {
        super.onActivityResult(rc, res, d);
        if (rc == 100 && res == RESULT_OK && d != null) {
            lat = d.getDoubleExtra("latitude", 0); lng = d.getDoubleExtra("longitude", 0); acc = d.getFloatExtra("accuracy", 0);
            btnGps.setText("GPS ✓ (" + String.format("%.1f", acc) + "m)");
            btnGps.setBackgroundColor(getColor(R.color.success_green));
        }
    }

    private void save() {
        if (etName.getText().toString().trim().isEmpty()) { etName.setError("Required"); return; }
        if (acc == 0) { Toast.makeText(this, "Capture GPS first", Toast.LENGTH_SHORT).show(); return; }
        Woman w = new Woman();
        w.fullName = etName.getText().toString().trim(); w.phone = etPhone.getText().toString().trim();
        w.address = etAddress.getText().toString().trim(); w.landmark = etLandmark.getText().toString().trim();
        w.area = etArea.getText().toString().trim(); w.lga = etLga.getText().toString().trim();
        w.dob = etDob.getText().toString().trim(); w.edd = etEdd.getText().toString().trim();
        w.gravida = parse(etGravida); w.para = parse(etPara);
        w.latitude = lat; w.longitude = lng; w.accuracy = acc;
        w.gpsTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        w.riskLevel = (w.gravida > 4 || (w.para == 0 && w.gravida > 0)) ? "HIGH" : "LOW";
        w.registeredBy = new RoleManager(this).getCurrentRole().getDisplayName();
        w.registeredAt = System.currentTimeMillis(); w.synced = false;

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            long wid = db.womanDao().insert(w);
            if (w.edd != null && !w.edd.isEmpty()) {
                for (Schedule s : ScheduleGenerator.generateANC((int) wid, w.edd)) db.scheduleDao().insert(s);
                for (Schedule s : ScheduleGenerator.generateDelivery((int) wid, w.edd)) db.scheduleDao().insert(s);
            }
            runOnUiThread(() -> { Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show(); finish(); });
        }).start();
    }

    private int parse(EditText e) { try { return Integer.parseInt(e.getText().toString().trim()); } catch (Exception ex) { return 0; } }
}
