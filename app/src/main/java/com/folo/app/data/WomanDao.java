package com.folo.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface WomanDao {
    @Insert long insert(Woman woman);
    @Update void update(Woman woman);
    @Query("SELECT * FROM women ORDER BY lastModified DESC")
    LiveData<List<Woman>> getAllWomen();
    @Query("SELECT * FROM women WHERE id = :id")
    Woman getWomanById(int id);
    @Query("SELECT * FROM women WHERE synced = 0")
    List<Woman> getUnsyncedWomen();
    @Query("SELECT * FROM women WHERE recordedBy = :user")
    LiveData<List<Woman>> getWomenByUser(String user);
}
