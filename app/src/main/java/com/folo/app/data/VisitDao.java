package com.folo.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface VisitDao {
    @Insert long insert(Visit visit);
    @Query("SELECT * FROM visits WHERE womanId = :womanId ORDER BY visitDate DESC")
    LiveData<List<Visit>> getVisitsForWoman(int womanId);
    @Query("SELECT * FROM visits WHERE synced = 0")
    List<Visit> getUnsyncedVisits();
}
