package com.folo.app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sync_queue")
public class SyncQueueItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String tableName;
    public int recordId;
    public String operation;
    public String payload;
    public int retryCount;
    public long createdAt;
}
