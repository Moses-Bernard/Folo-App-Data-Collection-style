package com.folo.app.schedule;

import com.folo.app.data.Schedule;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleGenerator {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static List<Schedule> generateANC(int womanId, String lmpDate) {
        List<Schedule> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        try { cal.setTime(sdf.parse(lmpDate)); } catch (Exception e) {}
        int[] wks = {12, 20, 26, 32, 36, 38, 40};
        String[] pur = {"ANC 1 - Baseline", "ANC 2 - Growth", "ANC 3 - Screening", "ANC 4 - Birth plan", "ANC 5 - Position", "ANC 6 - Final prep", "ANC 7 - Due date"};
        for (int i = 0; i < wks.length; i++) {
            Calendar c = (Calendar) cal.clone(); c.add(Calendar.WEEK_OF_YEAR, wks[i]);
            Schedule s = new Schedule(); s.womanId = womanId; s.stage = "ANC";
            s.scheduledDate = sdf.format(c.getTime()); s.purpose = pur[i];
            s.completed = false; s.missed = false; list.add(s);
        }
        return list;
    }

    public static List<Schedule> generateDelivery(int womanId, String edd) {
        List<Schedule> list = new ArrayList<>();
        Schedule s = new Schedule(); s.womanId = womanId; s.stage = "DELIVERY";
        s.scheduledDate = edd; s.purpose = "Expected delivery"; s.completed = false; s.missed = false;
        list.add(s); return list;
    }

    public static List<Schedule> generatePNC(int womanId, String deliveryDate) {
        List<Schedule> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        try { cal.setTime(sdf.parse(deliveryDate)); } catch (Exception e) { return list; }
        int[] days = {1, 3, 7, 14, 42};
        String[] pur = {"PNC Day 1 - Immediate", "PNC Day 3 - Breastfeeding", "PNC Day 7 - Healing", "PNC Day 14 - Recovery", "PNC Day 42 - Final"};
        for (int i = 0; i < days.length; i++) {
            Calendar c = (Calendar) cal.clone(); c.add(Calendar.DAY_OF_YEAR, days[i]);
            Schedule s = new Schedule(); s.womanId = womanId; s.stage = "PNC";
            s.scheduledDate = sdf.format(c.getTime()); s.purpose = pur[i];
            s.completed = false; s.missed = false; list.add(s);
        }
        return list;
    }
}
