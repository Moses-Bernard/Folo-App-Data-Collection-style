package com.folo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.role.Role;
import com.folo.app.role.RoleManager;

public class MainActivity extends AppCompatActivity {
    private RoleManager roleManager;
    private Button btnRegister, btnWomanList, btnGpsCapture, btnHealthCheck, 
                   btnSchedule, btnDelivery, btnSync, btnSettings, btnQuestions, btnExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roleManager = new RoleManager(this);
        initViews();
        applyPermissions();
    }

    private void initViews() {
        btnRegister = findViewById(R.id.btnRegister);
        btnWomanList = findViewById(R.id.btnWomanList);
        btnGpsCapture = findViewById(R.id.btnGpsCapture);
        btnHealthCheck = findViewById(R.id.btnHealthCheck);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnDelivery = findViewById(R.id.btnDelivery);
        btnSync = findViewById(R.id.btnSync);
        btnSettings = findViewById(R.id.btnSettings);
        btnQuestions = findViewById(R.id.btnQuestions);
        btnExport = findViewById(R.id.btnExport);
    }

    private void applyPermissions() {
        Role role = roleManager.getCurrentRole();

        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegistrationActivity2.class)));
        btnWomanList.setOnClickListener(v -> startActivity(new Intent(this, WomanListActivity2.class)));
        btnGpsCapture.setOnClickListener(v -> startActivity(new Intent(this, GpsCaptureActivity2.class)));
        btnHealthCheck.setOnClickListener(v -> startActivity(new Intent(this, HealthCheckActivity2.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(this, ScheduleActivity.class)));
        btnDelivery.setOnClickListener(v -> startActivity(new Intent(this, DeliveryRecordActivity.class)));
        btnSync.setOnClickListener(v -> startActivity(new Intent(this, SyncCenterActivity2.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        btnQuestions.setOnClickListener(v -> startActivity(new Intent(this, QuestionsActivity.class)));
        btnExport.setOnClickListener(v -> startActivity(new Intent(this, ExportActivity.class)));

        btnRegister.setVisibility(role.hasPermission("register") ? Button.VISIBLE : Button.GONE);
        btnExport.setVisibility(role.hasPermission("export") ? Button.VISIBLE : Button.GONE);
        btnSync.setVisibility(role.hasPermission("sync") ? Button.VISIBLE : Button.GONE);
        btnSettings.setVisibility(role.hasPermission("settings") ? Button.VISIBLE : Button.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            roleManager.clear();
            startActivity(new Intent(this, LoginActivity2.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
