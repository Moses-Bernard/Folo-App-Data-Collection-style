package com.folo.app.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedules", foreignKeys = @ForeignKey(
    entity = Woman.class, parentColumns = "id", childColumns = "womanId", onDelete = ForeignKey.CASCADE))
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int womanId;
    public String stage;
    public String scheduledDate;
    public String purpose;
    public boolean completed;
    public boolean missed;
    public String completedDate;
    public String notes;
}
