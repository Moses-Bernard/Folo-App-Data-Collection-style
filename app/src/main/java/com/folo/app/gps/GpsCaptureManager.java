package com.folo.app.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;

public class GpsCaptureManager {
    public static final float TARGET_ACCURACY = 5.0f;
    public static final long UPDATE_INTERVAL = 1000;
    private final FusedLocationProviderClient fusedClient;
    private LocationCallback locationCallback;
    private GpsListener listener;
    private int countdown = 3;

    public interface GpsListener {
        void onLocationUpdate(Location location, float accuracy, int countdown);
        void onAccuracyReached(Location location);
        void onError(String error);
    }

    public GpsCaptureManager(Context context) {
        fusedClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void startCapture(Context context, GpsListener listener) {
        this.listener = listener;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            listener.onError("Location permission not granted");
            return;
        }
        LocationRequest request = new LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL)
            .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(500).build();

        countdown = 3;
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                if (result == null) return;
                Location location = result.getLastLocation();
                if (location == null) return;
                float accuracy = location.getAccuracy();

                if (accuracy <= TARGET_ACCURACY) {
                    countdown--;
                    if (countdown <= 0) {
                        listener.onAccuracyReached(location);
                        stopCapture();
                        return;
                    }
                } else {
                    countdown = 3;
                }
                listener.onLocationUpdate(location, accuracy, Math.max(0, countdown));
            }
        };
        fusedClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
    }

    public void stopCapture() {
        if (locationCallback != null) fusedClient.removeLocationUpdates(locationCallback);
    }
}
