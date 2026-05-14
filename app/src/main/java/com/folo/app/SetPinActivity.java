package com.folo.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.role.Role;
import com.folo.app.role.RoleManager;

public class SetPinActivity extends AppCompatActivity {
    private EditText etPin, etConfirmPin;
    private Button btnSavePin;
    private TextView tvRoleName;
    private Role role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);

        role = Role.valueOf(getIntent().getStringExtra("role"));
        tvRoleName = findViewById(R.id.tvRoleName);
        etPin = findViewById(R.id.etPin);
        etConfirmPin = findViewById(R.id.etConfirmPin);
        btnSavePin = findViewById(R.id.btnSavePin);

        tvRoleName.setText("Set PIN for " + role.getDisplayName());
        btnSavePin.setOnClickListener(v -> savePin());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void savePin() {
        String pin = etPin.getText().toString().trim();
        String confirm = etConfirmPin.getText().toString().trim();

        if (pin.length() < 4) {
            Toast.makeText(this, "PIN must be at least 4 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pin.equals(confirm)) {
            Toast.makeText(this, "PINs do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        new RoleManager(this).setPin(role, pin);
        Toast.makeText(this, "PIN saved for " + role.getDisplayName(), Toast.LENGTH_SHORT).show();
        finish();
    }
}
