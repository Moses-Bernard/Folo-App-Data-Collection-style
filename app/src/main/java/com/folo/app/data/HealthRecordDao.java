package com.folo.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HealthRecordDao {
    @Insert long insert(HealthRecord record);
    @Query("SELECT * FROM health_records WHERE womanId = :womanId ORDER BY checkDate DESC")
    LiveData<List<HealthRecord>> getHealthRecordsForWoman(int womanId);
    @Query("SELECT * FROM health_records WHERE synced = 0")
    List<HealthRecord> getUnsyncedRecords();
}
