# Folo-App - Maternal Health Tracker

## Complete Android Project (64 files)

### Features Implemented

1. **No Lock Screen** - Simple role selection login (no PIN/password)
2. **Role-Based Access Control** - Stored locally in SharedPreferences
   - Admin: Full access (register, edit, delete, view all, sync, settings, export)
   - Surveillance Officer: Register, edit own records, GPS capture, health checks, questions
   - Supervisor: View all, edit, approve, alerts, reports, export
3. **Purple Theme (Mauve/Orchid)** - No white backgrounds
   - Light mauve (#D4A5C9) to orchid (#DA70D6) gradient
   - Purple text fields (#D8B8D8) for visibility
   - Purple cards (#EAD0EA) for content
4. **App Icon** - Silhouette of pregnant African woman with low hair/bun
5. **GPS Capture** - ODK-style with countdown at ≤5m accuracy
   - Real-time accuracy display
   - 3-second countdown when accuracy ≤5m
   - Navigate to captured location via Google Maps
6. **Woman Registration** - Full demographic capture with mandatory GPS
7. **Edit Woman Details** - Modify name, phone, address, area, LGA, EDD
8. **Woman List** - RecyclerView with risk indicators and edit buttons
9. **Woman Detail** - View all info, navigate to GPS, add health/visit/delivery
10. **Health Check** - Record BP, weight, symptoms, conditions
11. **Delivery Record** - Record delivery date, place, outcome, baby details, complications
12. **Edit Delivery Record** - Modify existing delivery records
13. **Schedule Generator** - Auto-generates:
    - ANC visits (7 visits at 12, 20, 26, 32, 36, 38, 40 weeks)
    - Delivery schedule (on EDD)
    - PNC visits (Day 1, 3, 7, 14, 42)
14. **Standalone Question Module** - Easy to add/remove questions
    - Categories: Demographics, Pregnancy, Health, Risk, Emergency
    - Answer types: Text, Number, Yes/No, Choice, Date
    - Edit ONLY QuestionBank.java to modify questions
15. **Sync Center** - Queue-based sync with retry logic
16. **Settings** - Change role, clear data, logout

### How to Add/Remove Questions

Edit ONLY this file: `app/src/main/java/com/folo/app/questions/QuestionBank.java`

```java
// Example: Add a new question
q.add(new Question("unique_id", "CATEGORY", "Question text?", "ANSWER_TYPE",
    new String[]{"Choice1", "Choice2"}, true, "Help text"));
```

### Project Structure

```
folo-app/
├── build.gradle              (Project level)
├── settings.gradle
└── app/
    ├── build.gradle          (App level with dependencies)
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/folo/app/
        │   ├── Activities (14 files)
        │   ├── data/       (13 files - Room entities & DAOs)
        │   ├── gps/        (1 file - GpsCaptureManager)
        │   ├── questions/  (3 files - Standalone question module)
        │   ├── role/       (2 files - Role enum & manager)
        │   └── schedule/   (1 file - ScheduleGenerator)
        └── res/
            ├── drawable/   (5 files - Icon & backgrounds)
            ├── layout/     (18 files - All screens)
            └── values/     (3 files - Colors, strings, themes)
```

### Build Instructions

1. Open Android Studio
2. File → New → Import Project
3. Select the `folo-app` folder
4. Sync Gradle
5. Run on device or emulator

### Required Permissions
- ACCESS_FINE_LOCATION (GPS capture)
- ACCESS_COARSE_LOCATION (Fallback)
- INTERNET (Sync & navigation)
- ACCESS_NETWORK_STATE

### Database
- Room database with 6 entities
- Automatic migration (fallbackToDestructiveMigration)
- Foreign key constraints with CASCADE delete
