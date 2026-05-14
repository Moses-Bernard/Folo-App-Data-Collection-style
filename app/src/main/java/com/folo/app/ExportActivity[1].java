package com.folo.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.Woman;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ExportActivity extends AppCompatActivity {
    private RadioGroup rgFormat;
    private Button btnExport;
    private static final int PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        rgFormat = findViewById(R.id.rgFormat);
        btnExport = findViewById(R.id.btnExport);

        btnExport.setOnClickListener(v -> checkPermissionAndExport());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void checkPermissionAndExport() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        } else {
            performExport();
        }
    }

    private void performExport() {
        boolean isJson = rgFormat.getCheckedRadioButtonId() == R.id.rbJson;
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Woman> women = AppDatabase.getInstance(this).womanDao().getAllWomen().getValue();
                if (women == null || women.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show());
                    return;
                }

                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "FoloApp");
                dir.mkdirs();

                if (isJson) {
                    exportJson(women, new File(dir, "folo_export_" + timestamp + ".json"));
                } else {
                    exportCsv(women, new File(dir, "folo_export_" + timestamp + ".csv"));
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void exportJson(List<Woman> women, File file) throws Exception {
        JSONArray array = new JSONArray();
        for (Woman w : women) {
            JSONObject obj = new JSONObject();
            obj.put("id", w.id);
            obj.put("full_name", w.fullName);
            obj.put("phone", w.phone);
            obj.put("address", w.address);
            obj.put("landmark", w.landmark);
            obj.put("area", w.area);
            obj.put("lga", w.lga);
            obj.put("dob", w.dob);
            obj.put("lmp", w.lmp);
            obj.put("edd", w.edd);
            obj.put("gravida", w.gravida);
            obj.put("para", w.para);
            obj.put("latitude", w.latitude);
            obj.put("longitude", w.longitude);
            obj.put("accuracy", w.accuracy);
            obj.put("risk_level", w.riskLevel);
            obj.put("recorded_by", w.recordedBy);
            array.put(obj);
        }
        FileWriter writer = new FileWriter(file);
        writer.write(array.toString(2));
        writer.close();
        runOnUiThread(() -> Toast.makeText(this, "Exported to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show());
    }

    private void exportCsv(List<Woman> women, File file) throws Exception {
        FileWriter writer = new FileWriter(file);
        writer.write("ID,Full Name,Phone,Address,Landmark,Area,LGA,DOB,LMP,EDD,Gravida,Para,Latitude,Longitude,Accuracy,Risk Level,Recorded By\n");
        for (Woman w : women) {
            writer.write(String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,%d,%.6f,%.6f,%.1f,\"%s\",\"%s\"\n",
                w.id, w.fullName, w.phone, w.address, w.landmark, w.area, w.lga,
                w.dob, w.lmp, w.edd, w.gravida, w.para, w.latitude, w.longitude,
                w.accuracy, w.riskLevel, w.recordedBy));
        }
        writer.close();
        runOnUiThread(() -> Toast.makeText(this, "Exported to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            performExport();
        } else {
            Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
        }
    }
}
