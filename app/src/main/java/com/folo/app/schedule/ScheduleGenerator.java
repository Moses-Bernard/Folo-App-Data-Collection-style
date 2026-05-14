package com.folo.app.schedule;

import android.content.Context;
import com.folo.app.data.AppDatabase;
import com.folo.app.data.Schedule;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ScheduleGenerator {
    private final Context context;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public ScheduleGenerator(Context context) {
        this.context = context;
    }

    public void generateSchedules(int womanId, String edd, String lmp) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Calendar cal = Calendar.getInstance();
                if (lmp != null) {
                    cal.setTime(sdf.parse(lmp));
                    // ANC visits at 12, 20, 26, 32, 36, 38, 40 weeks
                    int[] ancWeeks = {12, 20, 26, 32, 36, 38, 40};
                    for (int week : ancWeeks) {
                        Calendar ancCal = (Calendar) cal.clone();
                        ancCal.add(Calendar.WEEK_OF_YEAR, week);
                        Schedule s = new Schedule();
                        s.womanId = womanId;
                        s.type = "ANC";
                        s.scheduledDate = sdf.format(ancCal.getTime());
                        s.description = "ANC Visit - Week " + week;
                        s.completed = false;
                        AppDatabase.getInstance(context).scheduleDao().insert(s);
                    }
                }
                if (edd != null) {
                    // Delivery on EDD
                    Schedule del = new Schedule();
                    del.womanId = womanId;
                    del.type = "Delivery";
                    del.scheduledDate = edd;
                    del.description = "Expected Delivery Date";
                    del.completed = false;
                    AppDatabase.getInstance(context).scheduleDao().insert(del);

                    // PNC visits: Day 1, 3, 7, 14, 42
                    Calendar eddCal = Calendar.getInstance();
                    eddCal.setTime(sdf.parse(edd));
                    int[] pncDays = {1, 3, 7, 14, 42};
                    for (int day : pncDays) {
                        Calendar pncCal = (Calendar) eddCal.clone();
                        pncCal.add(Calendar.DAY_OF_YEAR, day);
                        Schedule s = new Schedule();
                        s.womanId = womanId;
                        s.type = "PNC";
                        s.scheduledDate = sdf.format(pncCal.getTime());
                        s.description = "PNC Visit - Day " + day;
                        s.completed = false;
                        AppDatabase.getInstance(context).scheduleDao().insert(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
