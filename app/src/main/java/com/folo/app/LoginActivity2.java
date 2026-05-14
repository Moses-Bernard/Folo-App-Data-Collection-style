package com.folo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.role.Role;
import com.folo.app.role.RoleManager;

public class LoginActivity2 extends AppCompatActivity {
    private Spinner spinnerRole;
    private LinearLayout layoutPin;
    private EditText etPin;
    private Button btnLogin, btnSetPin;
    private TextView tvPinLabel;
    private RoleManager roleManager;
    private Role selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        roleManager = new RoleManager(this);
        spinnerRole = findViewById(R.id.spinnerRole);
        layoutPin = findViewById(R.id.layoutPin);
        etPin = findViewById(R.id.etPin);
        btnLogin = findViewById(R.id.btnLogin);
        btnSetPin = findViewById(R.id.btnSetPin);
        tvPinLabel = findViewById(R.id.tvPinLabel);

        String[] roles = {Role.ADMIN.getDisplayName(),
                         Role.SURVEILLANCE_OFFICER.getDisplayName(),
                         Role.SUPERVISOR.getDisplayName()};
        spinnerRole.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles));

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnSetPin.setOnClickListener(v -> showSetPinDialog());

        updatePinVisibility();
    }

    private void attemptLogin() {
        selectedRole = Role.fromString(spinnerRole.getSelectedItem().toString());

        if (!roleManager.hasPin(selectedRole)) {
            completeLogin();
            return;
        }

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
    }
}
