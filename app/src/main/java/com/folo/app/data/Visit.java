package com.folo.app.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "visits", foreignKeys = @ForeignKey(
    entity = Woman.class, parentColumns = "id", childColumns = "womanId", onDelete = ForeignKey.CASCADE))
public class Visit {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int womanId;
    public String visitDate;
    public String notes;
    public String nextVisitDate;
    public String visitType;
    public String recordedBy;
    public long recordedAt;
    public boolean synced;
}
