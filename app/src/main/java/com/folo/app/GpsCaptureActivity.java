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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.folo.app.gps.GpsCaptureManager;

public class GpsCaptureActivity extends AppCompatActivity {
    private TextView tvStatus;
    private Button btnStart, btnNavigate;
    private GpsCaptureManager gps;
    private Location captured;
    private static final int PERMISSION_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_capture);

        tvStatus = findViewById(R.id.tvStatus);
        btnStart = findViewById(R.id.btnStart);
        btnNavigate = findViewById(R.id.btnNavigate);
        gps = new GpsCaptureManager(this);

        btnStart.setOnClickListener(v -> startCapture());
        btnNavigate.setOnClickListener(v -> navigate());
        btnNavigate.setEnabled(false);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void startCapture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
            return;
        }

        tvStatus.setText("Acquiring...");
        tvStatus.setTextColor(ContextCompat.getColor(this, R.color.orchid_primary));
        btnStart.setEnabled(false);

        gps.startCapture(this, new GpsCaptureManager.GpsListener() {
            @Override
            public void onLocationUpdate(Location loc, float acc, int countdown) {
                runOnUiThread(() -> {
                    if (countdown > 0) {
                        tvStatus.setText(String.format("Hold steady... %ds", countdown));
                        tvStatus.setTextColor(ContextCompat.getColor(GpsCaptureActivity.this, R.color.low_risk));
                    } else if (countdown == -1) {
                        tvStatus.setText(String.format("Improving... %.1fm", acc));
                        tvStatus.setTextColor(ContextCompat.getColor(GpsCaptureActivity.this, R.color.orchid_primary));
                    }
                });
            }

            @Override
            public void onAccuracyReached(Location loc) {
                runOnUiThread(() -> {
                    captured = loc;
                    tvStatus.setText(String.format("GPS LOCKED (%.1fm)", loc.getAccuracy()));
                    tvStatus.setTextColor(ContextCompat.getColor(GpsCaptureActivity.this, R.color.low_risk));
                    btnStart.setEnabled(true);
                    btnStart.setText("Recapture");
                    btnNavigate.setEnabled(true);
                });
            }

            @Override
            public void onError(String err) {
                runOnUiThread(() -> {
                    tvStatus.setText("Error: " + err);
                    tvStatus.setTextColor(ContextCompat.getColor(GpsCaptureActivity.this, R.color.high_risk));
                    btnStart.setEnabled(true);
                });
            }
        });
    }

    private void navigate() {
        if (captured == null) return;
        String uri = String.format("google.navigation:q=%.6f,%.6f", captured.getLatitude(), captured.getLongitude());
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCapture();
        } else {
            Toast.makeText(this, "GPS permission required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.stopCapture();
    }
}
