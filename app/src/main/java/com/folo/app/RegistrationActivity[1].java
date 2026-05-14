package com.folo.app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.Woman;
import com.folo.app.gps.GpsCaptureManager;
import com.folo.app.role.RoleManager;
import com.folo.app.schedule.ScheduleGenerator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etName, etPhone, etAddress, etLandmark, etArea, etGravida, etPara;
    private Spinner spinnerLga;
    private TextView tvDob, tvLmp, tvEdd, tvGpsStatus;
    private Button btnPickDob, btnPickLmp, btnCaptureGps, btnSave;
    private double latitude, longitude;
    private float accuracy = 999f;
    private String dobStr, lmpStr, eddStr;
    private GpsCaptureManager gpsManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        gpsManager = new GpsCaptureManager(this);
        initViews();
        setupLgaSpinner();
        setupDatePickers();
        setupGpsCapture();

        btnSave.setOnClickListener(v -> saveWoman());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etLandmark = findViewById(R.id.etLandmark);
        etArea = findViewById(R.id.etArea);
        etGravida = findViewById(R.id.etGravida);
        etPara = findViewById(R.id.etPara);
        spinnerLga = findViewById(R.id.spinnerLga);
        tvDob = findViewById(R.id.tvDob);
        tvLmp = findViewById(R.id.tvLmp);
        tvEdd = findViewById(R.id.tvEdd);
        tvGpsStatus = findViewById(R.id.tvGpsStatus);
        btnPickDob = findViewById(R.id.btnPickDob);
        btnPickLmp = findViewById(R.id.btnPickLmp);
        btnCaptureGps = findViewById(R.id.btnCaptureGps);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupLgaSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.lga_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLga.setAdapter(adapter);
    }

    private void setupDatePickers() {
        btnPickDob.setOnClickListener(v -> showDatePicker((year, month, day) -> {
            calendar.set(year, month, day);
            dobStr = dateFormat.format(calendar.getTime());
            tvDob.setText(dobStr);
        }));

        btnPickLmp.setOnClickListener(v -> showDatePicker((year, month, day) -> {
            calendar.set(year, month, day);
            lmpStr = dateFormat.format(calendar.getTime());
            tvLmp.setText(lmpStr);
            calculateEdd();
        }));
    }

    private void showDatePicker(DatePickerCallback callback) {
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
            callback.onDateSelected(year, month, dayOfMonth),
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void calculateEdd() {
        if (lmpStr == null) return;
        try {
            Calendar lmpCal = Calendar.getInstance();
            lmpCal.setTime(dateFormat.parse(lmpStr));
            lmpCal.add(Calendar.DAY_OF_YEAR, 280);
            eddStr = dateFormat.format(lmpCal.getTime());
            tvEdd.setText("EDD: " + eddStr);
        } catch (Exception e) {
            tvEdd.setText("EDD calculation error");
        }
    }

    private void setupGpsCapture() {
        btnCaptureGps.setOnClickListener(v -> {
            tvGpsStatus.setText("Capturing GPS... Wait for <=5m accuracy");
            gpsManager.startCapture((lat, lon, acc) -> {
                runOnUiThread(() -> {
                    latitude = lat;
                    longitude = lon;
                    accuracy = acc;
                    tvGpsStatus.setText(String.format("GPS: %.6f, %.6f (+/-%.1fm)", lat, lon, acc));
                    Toast.makeText(this, "GPS captured!", Toast.LENGTH_SHORT).show();
                });
            });
        });
    }

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
}
