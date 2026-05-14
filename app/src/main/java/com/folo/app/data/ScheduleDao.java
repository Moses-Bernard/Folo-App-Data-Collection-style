package com.folo.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert long insert(Schedule schedule);
    @Update void update(Schedule schedule);
    @Query("SELECT * FROM schedules WHERE womanId = :womanId ORDER BY scheduledDate")
    LiveData<List<Schedule>> getSchedulesForWoman(int womanId);
    @Query("SELECT * FROM schedules WHERE completed = 0 AND womanId = :womanId")
    List<Schedule> getPendingSchedules(int womanId);
}
