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
<<<<<<< HEAD

        String pin = etPin.getText().toString().trim();
        if (pin.isEmpty()) {
            Toast.makeText(this, "Enter PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roleManager.verifyPin(selectedRole, pin)) {
            completeLogin();
        } else {
            Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
            etPin.setText("");
        }
    }

    private void completeLogin() {
        roleManager.setRole(selectedRole, selectedRole.getDisplayName() + "_User");
        startActivity(new Intent(this, MainActivity2.class));
        finish();
    }

    private void showSetPinDialog() {
        selectedRole = Role.fromString(spinnerRole.getSelectedItem().toString());
        startActivity(new Intent(this, SetPinActivity2.class)
            .putExtra("role", selectedRole.name()));
    }

    private void updatePinVisibility() {
        selectedRole = Role.fromString(spinnerRole.getSelectedItem().toString());
        boolean hasPin = roleManager.hasPin(selectedRole);
        layoutPin.setVisibility(hasPin ? View.VISIBLE : View.GONE);
        tvPinLabel.setText(hasPin ? "Enter PIN:" : "No PIN set. Tap 'Set PIN' to create one.");
        btnSetPin.setVisibility(hasPin ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePinVisibility();
=======
        Spinner sp = findViewById(R.id.spinnerRole);
        Button btn = findViewById(R.id.btnLogin);
        String[] roles = {Role.ADMIN.getDisplayName(), Role.SURVEILLANCE_OFFICER.getDisplayName(), Role.SUPERVISOR.getDisplayName()};
        sp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles));
        btn.setOnClickListener(v -> {
            Role r = Role.fromString(sp.getSelectedItem().toString());
            rm.setRole(r, r.getDisplayName() + "_User");
            startActivity(new Intent(this, MainActivity.class)); finish();
        });
>>>>>>> parent of 82e8864 (feat: Add PIN-based login, multi-birth delivery, registration with LGA/LMP/EDD, export feature)
    }
}
