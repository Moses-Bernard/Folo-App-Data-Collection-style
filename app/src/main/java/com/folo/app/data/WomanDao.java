package com.folo.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface WomanDao {
    @Insert long insert(Woman woman);
    @Update void update(Woman woman);
    @Delete void delete(Woman woman);
    @Query("SELECT * FROM women ORDER BY registeredAt DESC")
    LiveData<List<Woman>> getAllWomen();
    @Query("SELECT * FROM women WHERE id = :id")
    Woman getWomanById(int id);
    @Query("SELECT * FROM women WHERE synced = 0")
    List<Woman> getUnsyncedWomen();
    @Query("UPDATE women SET synced = 1 WHERE id = :id")
    void markAsSynced(int id);
}
