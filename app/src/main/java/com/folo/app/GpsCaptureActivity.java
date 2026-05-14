package com.folo.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.folo.app.gps.GpsCaptureManager;

public class GpsCaptureActivity extends AppCompatActivity {
    private static final int PERM_REQ = 1;
    private TextView tvLat, tvLng, tvAcc, tvStatus, tvCountdown;
    private Button btnStart, btnSave, btnNav;
    private GpsCaptureManager gps;
    private Location captured;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_gps_capture);
        tvLat = findViewById(R.id.tvLatitude); tvLng = findViewById(R.id.tvLongitude);
        tvAcc = findViewById(R.id.tvAccuracy); tvStatus = findViewById(R.id.tvStatus);
        tvCountdown = findViewById(R.id.tvCountdown);
        btnStart = findViewById(R.id.btnStart); btnSave = findViewById(R.id.btnSave); btnNav = findViewById(R.id.btnNavigate);
        gps = new GpsCaptureManager(this);
        btnStart.setOnClickListener(v -> checkPerm());
        btnSave.setOnClickListener(v -> save());
        btnNav.setOnClickListener(v -> navigate());
        btnSave.setEnabled(false); btnNav.setEnabled(false);
    }

    private void checkPerm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERM_REQ);
        else startCapture();
    }

    @Override public void onRequestPermissionsResult(int rc, @NonNull String[] perms, @NonNull int[] res) {
        super.onRequestPermissionsResult(rc, perms, res);
        if (rc == PERM_REQ && res.length > 0 && res[0] == PackageManager.PERMISSION_GRANTED) startCapture();
        else Toast.makeText(this, "GPS permission required", Toast.LENGTH_SHORT).show();
    }

    private void startCapture() {
        tvStatus.setText("Acquiring..."); tvStatus.setTextColor(getColor(R.color.warning_orange)); btnStart.setEnabled(false);
        gps.startCapture(this, new GpsCaptureManager.GpsListener() {
            @Override public void onLocationUpdate(Location loc, float acc, int countdown) {
                runOnUiThread(() -> {
                    tvLat.setText(String.format("%.6f", loc.getLatitude()));
                    tvLng.setText(String.format("%.6f", loc.getLongitude()));
                    tvAcc.setText(String.format("%.1f m", acc));
                    if (acc <= GpsCaptureManager.TARGET_ACCURACY) {
                        tvStatus.setText("Hold steady..."); tvStatus.setTextColor(getColor(R.color.success_green));
                        tvCountdown.setText("Locking: " + countdown);
                    } else {
                        tvStatus.setText("Improving..."); tvStatus.setTextColor(getColor(R.color.warning_orange));
                        tvCountdown.setText("Target ≤5m (current: " + String.format("%.1f", acc) + "m)");
                    }
                });
            }
            @Override public void onAccuracyReached(Location loc) {
                runOnUiThread(() -> {
                    captured = loc; tvStatus.setText("GPS LOCKED ✓"); tvStatus.setTextColor(getColor(R.color.success_green));
                    tvCountdown.setText("Ready"); btnSave.setEnabled(true); btnNav.setEnabled(true);
                    btnStart.setEnabled(true); btnStart.setText("Recapture");
                });
            }
            @Override public void onError(String err) {
                runOnUiThread(() -> { tvStatus.setText("Error: " + err); tvStatus.setTextColor(getColor(R.color.danger_red)); btnStart.setEnabled(true); });
            }
        });
    }

    private void save() {
        if (captured == null) return;
        Intent r = new Intent();
        r.putExtra("latitude", captured.getLatitude()); r.putExtra("longitude", captured.getLongitude());
        r.putExtra("accuracy", captured.getAccuracy()); setResult(RESULT_OK, r); finish();
    }

    private void navigate() {
        if (captured == null) return;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
            "https://maps.google.com/maps?daddr=" + captured.getLatitude() + "," + captured.getLongitude())));
    }

    @Override protected void onDestroy() { super.onDestroy(); gps.stopCapture(); }
}
