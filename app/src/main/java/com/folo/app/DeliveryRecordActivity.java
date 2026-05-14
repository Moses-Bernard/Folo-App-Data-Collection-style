package com.folo.app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.DeliveryChildRecord;
import com.folo.app.data.DeliveryRecord;
import com.folo.app.role.RoleManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class DeliveryRecordActivity extends AppCompatActivity {
    private EditText etDeliveryDate, etDeliveryPlace, etComplications, etNotes;
    private Spinner spinnerAttendantRole;
    private LinearLayout layoutChildren;
    private Button btnAddChild, btnSave, btnBack;
    private TextView tvWomanId;
    private int womanId;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private int childCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_record);

        womanId = getIntent().getIntExtra("woman_id", -1);
        initViews();
        setupAttendantSpinner();
        setupDatePicker();

        btnAddChild.setOnClickListener(v -> addChildForm());
        btnSave.setOnClickListener(v -> saveDelivery());
        btnBack.setOnClickListener(v -> finish());

        if (womanId == -1) {
            Toast.makeText(this, "No woman selected", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvWomanId = findViewById(R.id.tvWomanId);
        etDeliveryDate = findViewById(R.id.etDeliveryDate);
        etDeliveryPlace = findViewById(R.id.etDeliveryPlace);
        etComplications = findViewById(R.id.etComplications);
        etNotes = findViewById(R.id.etNotes);
        spinnerAttendantRole = findViewById(R.id.spinnerAttendantRole);
        layoutChildren = findViewById(R.id.layoutChildren);
        btnAddChild = findViewById(R.id.btnAddChild);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        tvWomanId.setText("Woman ID: " + womanId);
    }

    private void setupAttendantSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.attendant_role_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAttendantRole.setAdapter(adapter);
    }

    private void setupDatePicker() {
        etDeliveryDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                cal.set(year, month, day);
                etDeliveryDate.setText(sdf.format(cal.getTime()));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void addChildForm() {
        childCount++;
        View childView = LayoutInflater.from(this).inflate(R.layout.item_child_form, layoutChildren, false);

        TextView tvChildNum = childView.findViewById(R.id.tvChildNumber);
        Spinner spinnerOutcome = childView.findViewById(R.id.spinnerOutcome);
        Spinner spinnerGender = childView.findViewById(R.id.spinnerGender);
        EditText etWeight = childView.findViewById(R.id.etChildWeight);

        tvChildNum.setText(String.format(getString(R.string.child_number), childCount));

        ArrayAdapter<CharSequence> outcomeAdapter = ArrayAdapter.createFromResource(this,
                R.array.delivery_outcome_options, android.R.layout.simple_spinner_item);
        outcomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOutcome.setAdapter(outcomeAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        childView.setTag(childCount);
        layoutChildren.addView(childView);
    }

    private void saveDelivery() {
        String date = etDeliveryDate.getText().toString().trim();
        String place = etDeliveryPlace.getText().toString().trim();

        if (date.isEmpty()) {
            etDeliveryDate.setError("Delivery date required");
            return;
        }
        if (place.isEmpty()) {
            etDeliveryPlace.setError("Delivery place required");
            return;
        }

        DeliveryRecord delivery = new DeliveryRecord();
        delivery.womanId = womanId;
        delivery.deliveryDate = date;
        delivery.deliveryPlace = place;
        delivery.complications = etComplications.getText().toString().trim();
        delivery.attendantRole = spinnerAttendantRole.getSelectedItem().toString();
        delivery.notes = etNotes.getText().toString().trim();
        delivery.recordedBy = new RoleManager(this).getCurrentUser();
        delivery.recordedAt = System.currentTimeMillis();
        delivery.synced = false;
        delivery.lastModified = System.currentTimeMillis();
        delivery.modifiedBy = delivery.recordedBy;

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            long deliveryId = db.deliveryDao().insert(delivery);

            // Save each child
            for (int i = 0; i < layoutChildren.getChildCount(); i++) {
                View childView = layoutChildren.getChildAt(i);
                Spinner spinnerOutcome = childView.findViewById(R.id.spinnerOutcome);
                Spinner spinnerGender = childView.findViewById(R.id.spinnerGender);
                EditText etWeight = childView.findViewById(R.id.etChildWeight);

                DeliveryChildRecord child = new DeliveryChildRecord();
                child.deliveryId = (int) deliveryId;
                child.childNumber = (int) childView.getTag();
                child.outcome = spinnerOutcome.getSelectedItem().toString();
                child.gender = spinnerGender.getSelectedItem().toString();
                child.weightKg = parseDouble(etWeight);
                db.deliveryChildDao().insert(child);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Delivery record saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private double parseDouble(EditText et) {
        try { return Double.parseDouble(et.getText().toString().trim()); }
        catch (Exception e) { return 0.0; }
    }
}
