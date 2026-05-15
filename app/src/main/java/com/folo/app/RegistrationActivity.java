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

<<<<<<< HEAD
    private void saveWoman() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String lga = spinnerLga.getSelectedItem().toString();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }
        if (phone.length() != 11 || !phone.matches("\\d{11}")) {
            etPhone.setError("Enter valid 11-digit phone number");
            return;
        }
        if (accuracy > 10) {
            Toast.makeText(this, "GPS accuracy must be <=10m. Capture again.", Toast.LENGTH_LONG).show();
            return;
        }

        Woman woman = new Woman();
        woman.fullName = name;
        woman.phone = phone;
        woman.address = etAddress.getText().toString().trim();
        woman.landmark = etLandmark.getText().toString().trim();
        woman.area = etArea.getText().toString().trim();
        woman.lga = lga;
        woman.dob = dobStr;
        woman.lmp = lmpStr;
        woman.edd = eddStr;
        woman.gravida = parseInt(etGravida);
        woman.para = parseInt(etPara);
        woman.latitude = latitude;
        woman.longitude = longitude;
        woman.accuracy = accuracy;
        woman.gpsTimestamp = System.currentTimeMillis();
        woman.recordedBy = new RoleManager(this).getCurrentUser();
        woman.recordedAt = System.currentTimeMillis();
        woman.synced = false;
        woman.lastModified = System.currentTimeMillis();
        woman.modifiedBy = woman.recordedBy;
        woman.riskLevel = assessRisk(woman);

        Executors.newSingleThreadExecutor().execute(() -> {
            int womanId = (int) AppDatabase.getInstance(this).womanDao().insert(woman);
            new ScheduleGenerator(this).generateSchedules(womanId, eddStr, lmpStr);
            runOnUiThread(() -> {
                Toast.makeText(this, "Woman registered successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private int parseInt(EditText et) {
        try { return Integer.parseInt(et.getText().toString().trim()); }
        catch (Exception e) { return 0; }
    }

    private String assessRisk(Woman w) {
        if (w.gravida > 4 || (w.dob != null && getAge(w.dob) < 18)) return "high";
        return "low";
    }

    private int getAge(String dob) {
        try {
            Calendar dobCal = Calendar.getInstance();
            dobCal.setTime(dateFormat.parse(dob));
            Calendar now = Calendar.getInstance();
            return now.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);
        } catch (Exception e) { return 0; }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsManager.stop();
    }

    interface DatePickerCallback {
        void onDateSelected(int year, int month, int day);
    }
=======
    private int parse(EditText e) { try { return Integer.parseInt(e.getText().toString().trim()); } catch (Exception ex) { return 0; } }
>>>>>>> parent of 82e8864 (feat: Add PIN-based login, multi-birth delivery, registration with LGA/LMP/EDD, export feature)
}
