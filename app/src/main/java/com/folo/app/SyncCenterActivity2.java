package com.folo.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.SyncQueueItem;

public class SyncCenterActivity2 extends AppCompatActivity {
    private TextView tvStatus;
    private Button btnSync;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_sync_center);
        tvStatus = findViewById(R.id.tvSyncStatus);
        btnSync = findViewById(R.id.btnSyncNow);
        btnSync.setOnClickListener(v -> sync());
        updateStatus();
    }

    private void updateStatus() {
        new Thread(() -> {
            int count = AppDatabase.getInstance(this).syncQueueDao().getPendingItems().size();
            runOnUiThread(() -> tvStatus.setText("Pending items: " + count));
        }).start();
    }

    private void sync() {
        new Thread(() -> {
            // Simulate sync - mark all as success
            for (SyncQueueItem item : AppDatabase.getInstance(this).syncQueueDao().getPendingItems()) {
                AppDatabase.getInstance(this).syncQueueDao().updateStatus(item.id, "SUCCESS");
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Sync completed", Toast.LENGTH_SHORT).show();
                updateStatus();
            });
        }).start();
    }
}
