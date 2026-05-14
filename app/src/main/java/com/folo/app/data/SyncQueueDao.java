package com.folo.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SyncQueueDao {
    @Insert void insert(SyncQueueItem item);
    @Query("SELECT * FROM sync_queue ORDER BY createdAt")
    List<SyncQueueItem> getAllItems();
    @Query("DELETE FROM sync_queue WHERE id = :id")
    void deleteItem(int id);
    @Query("UPDATE sync_queue SET retryCount = retryCount + 1 WHERE id = :id")
    void incrementRetry(int id);
}
