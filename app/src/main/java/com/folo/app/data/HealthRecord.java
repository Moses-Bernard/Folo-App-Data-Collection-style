package com.folo.app.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "health_records", foreignKeys = @ForeignKey(
    entity = Woman.class, parentColumns = "id", childColumns = "womanId", onDelete = ForeignKey.CASCADE))
public class HealthRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int womanId;
    public String checkDate;
    public String bloodPressure;
    public double weightKg;
    public String symptoms;
    public String conditions;
    public String checkedBy;
    public long checkedAt;
    public boolean synced;
}
