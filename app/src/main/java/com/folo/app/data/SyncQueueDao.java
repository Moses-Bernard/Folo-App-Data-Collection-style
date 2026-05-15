package com.folo.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SyncQueueDao {
    @Insert void insert(SyncQueueItem item);
    @Query("SELECT * FROM sync_queue WHERE status = 'PENDING' ORDER BY createdAt ASC")
    List<SyncQueueItem> getPendingItems();
    @Query("UPDATE sync_queue SET status = :status, retryCount = retryCount + 1 WHERE id = :id")
    void updateStatus(int id, String status);
    @Query("DELETE FROM sync_queue WHERE status = 'SUCCESS'")
    void clearSuccessful();
}
