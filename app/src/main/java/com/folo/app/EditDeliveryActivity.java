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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class EditDeliveryActivity extends AppCompatActivity {
    private EditText etDeliveryDate, etDeliveryPlace, etComplications, etNotes;
    private Spinner spinnerAttendantRole;
    private LinearLayout layoutChildren;
    private Button btnAddChild, btnSave, btnBack;
    private TextView tvDeliveryId;
    private int deliveryId;
    private DeliveryRecord existingDelivery;
    private List<DeliveryChildRecord> existingChildren;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private int childCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_record);

        deliveryId = getIntent().getIntExtra("delivery_id", -1);
        initViews();
        setupAttendantSpinner();
        setupDatePicker();

        btnAddChild.setOnClickListener(v -> addChildForm(null));
        btnSave.setOnClickListener(v -> updateDelivery());
        btnBack.setOnClickListener(v -> finish());

        if (deliveryId == -1) {
            Toast.makeText(this, "No delivery selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadExistingData();
    }

    private void initViews() {
        tvDeliveryId = findViewById(R.id.tvWomanId);
        etDeliveryDate = findViewById(R.id.etDeliveryDate);
        etDeliveryPlace = findViewById(R.id.etDeliveryPlace);
        etComplications = findViewById(R.id.etComplications);
        etNotes = findViewById(R.id.etNotes);
        spinnerAttendantRole = findViewById(R.id.spinnerAttendantRole);
        layoutChildren = findViewById(R.id.layoutChildren);
        btnAddChild = findViewById(R.id.btnAddChild);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        tvDeliveryId.setText("Delivery ID: " + deliveryId);
        btnSave.setText("Update");
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

    private void loadExistingData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            existingDelivery = db.deliveryDao().getDeliveryById(deliveryId);
            existingChildren = db.deliveryChildDao().getChildrenForDelivery(deliveryId).getValue();

            runOnUiThread(() -> {
                if (existingDelivery != null) {
                    etDeliveryDate.setText(existingDelivery.deliveryDate);
                    etDeliveryPlace.setText(existingDelivery.deliveryPlace);
                    etComplications.setText(existingDelivery.complications);
                    etNotes.setText(existingDelivery.notes);

                    // Set attendant role spinner
                    String[] roles = getResources().getStringArray(R.array.attendant_role_options);
                    for (int i = 0; i < roles.length; i++) {
                        if (roles[i].equals(existingDelivery.attendantRole)) {
                            spinnerAttendantRole.setSelection(i);
                            break;
                        }
                    }
                }

                // Load existing children
                if (existingChildren != null) {
                    for (DeliveryChildRecord child : existingChildren) {
                        addChildForm(child);
                    }
                }
            });
        });
    }

    private void addChildForm(DeliveryChildRecord existingChild) {
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

        // Pre-fill if editing existing child
        if (existingChild != null) {
            String[] outcomes = getResources().getStringArray(R.array.delivery_outcome_options);
            for (int i = 0; i < outcomes.length; i++) {
                if (outcomes[i].equals(existingChild.outcome)) {
                    spinnerOutcome.setSelection(i);
                    break;
                }
            }
            String[] genders = getResources().getStringArray(R.array.gender_options);
            for (int i = 0; i < genders.length; i++) {
                if (genders[i].equals(existingChild.gender)) {
                    spinnerGender.setSelection(i);
                    break;
                }
            }
            etWeight.setText(String.valueOf(existingChild.weightKg));
            childView.setTag(existingChild.id);
        } else {
            childView.setTag(-1);
        }

        layoutChildren.addView(childView);
    }

    private void updateDelivery() {
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

        existingDelivery.deliveryDate = date;
        existingDelivery.deliveryPlace = place;
        existingDelivery.complications = etComplications.getText().toString().trim();
        existingDelivery.attendantRole = spinnerAttendantRole.getSelectedItem().toString();
        existingDelivery.notes = etNotes.getText().toString().trim();
        existingDelivery.lastModified = System.currentTimeMillis();
        existingDelivery.modifiedBy = new RoleManager(this).getCurrentUser();
        existingDelivery.synced = false;

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            db.deliveryDao().update(existingDelivery);

            // Update children - delete old, insert new
            db.deliveryChildDao().deleteChildrenForDelivery(deliveryId);
            for (int i = 0; i < layoutChildren.getChildCount(); i++) {
                View childView = layoutChildren.getChildAt(i);
                Spinner spinnerOutcome = childView.findViewById(R.id.spinnerOutcome);
                Spinner spinnerGender = childView.findViewById(R.id.spinnerGender);
                EditText etWeight = childView.findViewById(R.id.etChildWeight);

                DeliveryChildRecord child = new DeliveryChildRecord();
                child.deliveryId = deliveryId;
                child.childNumber = i + 1;
                child.outcome = spinnerOutcome.getSelectedItem().toString();
                child.gender = spinnerGender.getSelectedItem().toString();
                child.weightKg = parseDouble(etWeight);
                db.deliveryChildDao().insert(child);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Delivery record updated", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private double parseDouble(EditText et) {
        try { return Double.parseDouble(et.getText().toString().trim()); }
        catch (Exception e) { return 0.0; }
    }
}
