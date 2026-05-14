package com.folo.app.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "delivery_children", foreignKeys = @ForeignKey(
    entity = DeliveryRecord.class, parentColumns = "id", childColumns = "deliveryId", onDelete = ForeignKey.CASCADE))
public class DeliveryChildRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int deliveryId;
    public int childNumber;
    public String outcome;
    public String gender;
    public double weightKg;
}
