package com.folo.app.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Woman.class, Visit.class, DeliveryRecord.class, DeliveryChildRecord.class,
    HealthRecord.class, Schedule.class, SyncQueueItem.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract WomanDao womanDao();
    public abstract VisitDao visitDao();
    public abstract DeliveryDao deliveryDao();
    public abstract DeliveryChildDao deliveryChildDao();
    public abstract HealthRecordDao healthRecordDao();
    public abstract ScheduleDao scheduleDao();
    public abstract SyncQueueDao syncQueueDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "folo_app_db_v2")
                .fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
