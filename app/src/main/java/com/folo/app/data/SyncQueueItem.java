package com.folo.app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sync_queue")
public class SyncQueueItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String entityType;
    public int entityId;
    public String operation;
    public String payload;
    public long createdAt;
    public int retryCount;
    public String status;
}
