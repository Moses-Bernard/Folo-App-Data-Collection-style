package com.folo.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface DeliveryDao {
    @Insert long insert(DeliveryRecord record);
    @Update void update(DeliveryRecord record);
    @Query("SELECT * FROM delivery_records WHERE womanId = :womanId")
    LiveData<List<DeliveryRecord>> getDeliveriesForWoman(int womanId);
    @Query("SELECT * FROM delivery_records WHERE id = :id")
    DeliveryRecord getDeliveryById(int id);
    @Query("SELECT * FROM delivery_records WHERE synced = 0")
    List<DeliveryRecord> getUnsyncedDeliveries();
}
