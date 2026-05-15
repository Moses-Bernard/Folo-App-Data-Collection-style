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
<<<<<<< HEAD
    private static final int COUNTDOWN_SECONDS = 3;

    private final Context context;
    private final LocationManager locationManager;
    private LocationListener listener;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private GpsListener gpsListener;

    public interface GpsListener {
        void onLocationUpdate(Location loc, float acc, int countdown);
        void onAccuracyReached(Location loc);
        void onError(String err);
=======
    public static final long UPDATE_INTERVAL = 1000;
    private final FusedLocationProviderClient fusedClient;
    private LocationCallback locationCallback;
    private GpsListener listener;
    private int countdown = 3;

    public interface GpsListener {
        void onLocationUpdate(Location location, float accuracy, int countdown);
        void onAccuracyReached(Location location);
        void onError(String error);
>>>>>>> parent of 82e8864 (feat: Add PIN-based login, multi-birth delivery, registration with LGA/LMP/EDD, export feature)
    }

    public GpsCaptureManager(Context context) {
        fusedClient = LocationServices.getFusedLocationProviderClient(context);
    }

<<<<<<< HEAD
    public void startCapture(Context ctx, GpsListener callback) {
        this.gpsListener = callback;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (gpsListener != null) {
                gpsListener.onError("Location permission not granted");
            }
=======
    public void startCapture(Context context, GpsListener listener) {
        this.listener = listener;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            listener.onError("Location permission not granted");
>>>>>>> parent of 82e8864 (feat: Add PIN-based login, multi-birth delivery, registration with LGA/LMP/EDD, export feature)
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
<<<<<<< HEAD
                    startCountdown(location);
                } else {
                    if (gpsListener != null) {
                        gpsListener.onLocationUpdate(location, accuracy, -1);
                    }
                }
            }
            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onProviderDisabled(String provider) {
                if (gpsListener != null) {
                    gpsListener.onError("GPS provider disabled");
                }
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
    }

    private void startCountdown(Location location) {
        if (countdownRunnable != null) return;
        final int[] seconds = {COUNTDOWN_SECONDS};
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (gpsListener != null) {
                    gpsListener.onLocationUpdate(location, location.getAccuracy(), seconds[0]);
                }
                if (seconds[0] > 0) {
                    seconds[0]--;
                    handler.postDelayed(this, 1000);
                } else {
                    if (gpsListener != null) {
                        gpsListener.onAccuracyReached(location);
                    }
                    stopCapture();
=======
                    countdown--;
                    if (countdown <= 0) {
                        listener.onAccuracyReached(location);
                        stopCapture();
                        return;
                    }
                } else {
                    countdown = 3;
>>>>>>> parent of 82e8864 (feat: Add PIN-based login, multi-birth delivery, registration with LGA/LMP/EDD, export feature)
                }
                listener.onLocationUpdate(location, accuracy, Math.max(0, countdown));
            }
        };
        fusedClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
    }

    public void stopCapture() {
<<<<<<< HEAD
        if (listener != null) {
            locationManager.removeUpdates(listener);
            listener = null;
        }
        if (countdownRunnable != null) {
            handler.removeCallbacks(countdownRunnable);
            countdownRunnable = null;
        }
=======
        if (locationCallback != null) fusedClient.removeLocationUpdates(locationCallback);
>>>>>>> parent of 82e8864 (feat: Add PIN-based login, multi-birth delivery, registration with LGA/LMP/EDD, export feature)
    }
}
