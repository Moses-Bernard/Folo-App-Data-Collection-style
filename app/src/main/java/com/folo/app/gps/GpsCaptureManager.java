package com.folo.app.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;

public class GpsCaptureManager {
    private final Context context;
    private final LocationManager locationManager;
    private LocationListener listener;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private static final float TARGET_ACCURACY = 5.0f;
    private static final int COUNTDOWN_SECONDS = 3;

    public interface GpsCallback {
        void onCaptured(double latitude, double longitude, float accuracy);
    }

    public GpsCaptureManager(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startCapture(GpsCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float accuracy = location.getAccuracy();
                if (accuracy <= TARGET_ACCURACY) {
                    startCountdown(location, callback);
                }
            }
            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
    }

    private void startCountdown(Location location, GpsCallback callback) {
        if (countdownRunnable != null) return;
        final int[] seconds = {COUNTDOWN_SECONDS};
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (seconds[0] > 0) {
                    seconds[0]--;
                    handler.postDelayed(this, 1000);
                } else {
                    callback.onCaptured(location.getLatitude(), location.getLongitude(), location.getAccuracy());
                    stop();
                }
            }
        };
        handler.post(countdownRunnable);
    }

    public void stop() {
        if (listener != null) {
            locationManager.removeUpdates(listener);
            listener = null;
        }
        if (countdownRunnable != null) {
            handler.removeCallbacks(countdownRunnable);
            countdownRunnable = null;
        }
    }
}
