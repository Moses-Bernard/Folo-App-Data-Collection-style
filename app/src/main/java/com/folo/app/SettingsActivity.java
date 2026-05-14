package com.folo.app;

import com.folo.app.data.AppDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.role.RoleManager;

public class SettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_settings);
        RoleManager rm = new RoleManager(this);
        ((TextView) findViewById(R.id.tvCurrentRole)).setText("Role: " + rm.getCurrentRole().getDisplayName());
        ((TextView) findViewById(R.id.tvCurrentUser)).setText("User: " + rm.getCurrentUser());

        findViewById(R.id.btnChangeRole).setOnClickListener(v -> {
            rm.clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        findViewById(R.id.btnClearData).setOnClickListener(v -> {
            new Thread(() -> {
                AppDatabase.getInstance(this).clearAllTables();
                runOnUiThread(() -> Toast.makeText(this, "All data cleared", Toast.LENGTH_SHORT).show());
            }).start();
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            rm.clear();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
