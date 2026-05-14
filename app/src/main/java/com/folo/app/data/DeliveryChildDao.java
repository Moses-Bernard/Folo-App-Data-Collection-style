package com.folo.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DeliveryChildDao {
    @Insert long insert(DeliveryChildRecord child);
    @Query("SELECT * FROM delivery_children WHERE deliveryId = :deliveryId ORDER BY childNumber")
    LiveData<List<DeliveryChildRecord>> getChildrenForDelivery(int deliveryId);
    @Query("DELETE FROM delivery_children WHERE deliveryId = :deliveryId")
    void deleteChildrenForDelivery(int deliveryId);
}
