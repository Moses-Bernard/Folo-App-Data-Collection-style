package com.folo.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.DeliveryRecord;
import com.folo.app.role.RoleManager;

public class EditDeliveryActivity extends AppCompatActivity {
    private int deliveryId;
    private EditText etDate, etPlace, etOutcome, etNotes;
    private Button btnSave;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_edit_delivery);
        deliveryId = getIntent().getIntExtra("delivery_id", -1);
        etDate = findViewById(R.id.etEditDelDate); etPlace = findViewById(R.id.etEditDelPlace);
        etOutcome = findViewById(R.id.etEditDelOutcome); etNotes = findViewById(R.id.etEditDelNotes);
        btnSave = findViewById(R.id.btnSaveEditDelivery);
        btnSave.setOnClickListener(v -> save());
        loadDelivery();
    }

    private void loadDelivery() {
        new Thread(() -> {
            DeliveryRecord d = AppDatabase.getInstance(this).deliveryDao().getDeliveryById(deliveryId);
            runOnUiThread(() -> {
                if (d == null) return;
                etDate.setText(d.deliveryDate); etPlace.setText(d.deliveryPlace);
                etOutcome.setText(d.deliveryOutcome); etNotes.setText(d.notes);
            });
        }).start();
    }

    private void save() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            DeliveryRecord d = db.deliveryDao().getDeliveryById(deliveryId);
            if (d == null) return;
            d.deliveryDate = etDate.getText().toString().trim();
            d.deliveryPlace = etPlace.getText().toString().trim();
            d.deliveryOutcome = etOutcome.getText().toString().trim();
            d.notes = etNotes.getText().toString().trim();
            d.lastModified = System.currentTimeMillis();
            d.modifiedBy = new RoleManager(this).getCurrentRole().getDisplayName();
            d.synced = false;
            db.deliveryDao().update(d);
            runOnUiThread(() -> { Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show(); finish(); });
        }).start();
    }
}
