package com.folo.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.role.RoleManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RoleManager rm = new RoleManager(this);
        ((TextView) findViewById(R.id.tvRole)).setText("Role: " + rm.getCurrentRole().getDisplayName());

        setBtn(R.id.btnRegister, RegistrationActivity.class, "REGISTER");
        setBtn(R.id.btnWomanList, WomanListActivity.class, "VIEW_ALL");
        setBtn(R.id.btnGpsCapture, GpsCaptureActivity.class, "GPS_CAPTURE");
        setBtn(R.id.btnHealthCheck, HealthCheckActivity.class, "HEALTH_CHECK");
        setBtn(R.id.btnSchedule, ScheduleActivity.class, null);
        setBtn(R.id.btnDelivery, DeliveryRecordActivity.class, "EDIT");
        setBtn(R.id.btnSync, SyncCenterActivity.class, "SYNC");
        setBtn(R.id.btnSettings, SettingsActivity.class, "SETTINGS");
        setBtn(R.id.btnQuestions, QuestionModuleActivity.class, "QUESTIONS");
    }

<<<<<<< HEAD
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
=======
    private void setBtn(int id, Class<?> cls, String perm) {
        Button b = findViewById(id);
        if (perm != null && !new RoleManager(this).hasPermission(perm)) {
            b.setEnabled(false); b.setAlpha(0.5f);
        } else {
            b.setOnClickListener(v -> startActivity(new Intent(this, cls)));
>>>>>>> parent of 82e8864 (feat: Add PIN-based login, multi-birth delivery, registration with LGA/LMP/EDD, export feature)
        }
    }
}
