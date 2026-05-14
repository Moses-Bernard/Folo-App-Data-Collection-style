package com.folo.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.role.Role;
import com.folo.app.role.RoleManager;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RoleManager rm = new RoleManager(this);
        if (!rm.getCurrentUser().equals("Unknown")) {
            startActivity(new Intent(this, MainActivity.class)); finish(); return;
        }
        Spinner sp = findViewById(R.id.spinnerRole);
        Button btn = findViewById(R.id.btnLogin);
        String[] roles = {Role.ADMIN.getDisplayName(), Role.SURVEILLANCE_OFFICER.getDisplayName(), Role.SUPERVISOR.getDisplayName()};
        sp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles));
        btn.setOnClickListener(v -> {
            Role r = Role.fromString(sp.getSelectedItem().toString());
            rm.setRole(r, r.getDisplayName() + "_User");
            startActivity(new Intent(this, MainActivity.class)); finish();
        });
    }
}
