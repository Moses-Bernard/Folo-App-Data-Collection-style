package com.folo.app.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "delivery_records", foreignKeys = @ForeignKey(
    entity = Woman.class, parentColumns = "id", childColumns = "womanId", onDelete = ForeignKey.CASCADE))
public class DeliveryRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int womanId;
    public String deliveryDate;
    public String deliveryPlace;
    public String deliveryOutcome;
    public String babyGender;
    public double babyWeightKg;
    public String complications;
    public String attendantName;
    public String notes;
    public String recordedBy;
    public long recordedAt;
    public boolean synced;
    public long lastModified;
    public String modifiedBy;
}
