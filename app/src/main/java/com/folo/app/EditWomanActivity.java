package com.folo.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.Woman;
import com.folo.app.role.RoleManager;

public class EditWomanActivity extends AppCompatActivity {
    private int womanId;
    private EditText etName, etPhone, etAddress, etArea, etLga, etEdd;
    private Button btnSave;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_edit_woman);
        womanId = getIntent().getIntExtra("woman_id", -1);
        etName = findViewById(R.id.etEditName); etPhone = findViewById(R.id.etEditPhone);
        etAddress = findViewById(R.id.etEditAddress); etArea = findViewById(R.id.etEditArea);
        etLga = findViewById(R.id.etEditLga); etEdd = findViewById(R.id.etEditEdd);
        btnSave = findViewById(R.id.btnSaveEdit);
        btnSave.setOnClickListener(v -> save());
        loadWoman();
    }

    private void loadWoman() {
        new Thread(() -> {
            Woman w = AppDatabase.getInstance(this).womanDao().getWomanById(womanId);
            runOnUiThread(() -> {
                if (w == null) return;
                etName.setText(w.fullName); etPhone.setText(w.phone);
                etAddress.setText(w.address); etArea.setText(w.area);
                etLga.setText(w.lga); etEdd.setText(w.edd);
            });
        }).start();
    }

    private void save() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Woman w = db.womanDao().getWomanById(womanId);
            if (w == null) return;
            w.fullName = etName.getText().toString().trim();
            w.phone = etPhone.getText().toString().trim();
            w.address = etAddress.getText().toString().trim();
            w.area = etArea.getText().toString().trim();
            w.lga = etLga.getText().toString().trim();
            w.edd = etEdd.getText().toString().trim();
            w.lastModified = System.currentTimeMillis();
            w.modifiedBy = new RoleManager(this).getCurrentRole().getDisplayName();
            w.synced = false;
            db.womanDao().update(w);
            runOnUiThread(() -> { Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show(); finish(); });
        }).start();
    }
}
