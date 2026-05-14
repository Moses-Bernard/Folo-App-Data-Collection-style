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

    private void setBtn(int id, Class<?> cls, String perm) {
        Button b = findViewById(id);
        if (perm != null && !new RoleManager(this).hasPermission(perm)) {
            b.setEnabled(false); b.setAlpha(0.5f);
        } else {
            b.setOnClickListener(v -> startActivity(new Intent(this, cls)));
        }
    }
}
