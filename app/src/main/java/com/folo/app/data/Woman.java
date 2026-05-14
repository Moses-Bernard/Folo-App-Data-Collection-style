package com.folo.app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "women")
public class Woman {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String fullName;
    public String phone;
    public String address;
    public String landmark;
    public String area;
    public String lga;
    public String dob;
    public String lmp;
    public String edd;
    public int gravida;
    public int para;
    public double latitude;
    public double longitude;
    public float accuracy;
    public long gpsTimestamp;
    public String recordedBy;
    public long recordedAt;
    public boolean synced;
    public long lastModified;
    public String modifiedBy;
    public String riskLevel;
}
