package services;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Michał on 2015-05-10.
 */
public class GpsTrackerService {

    GpsListener gpsLocalizator;

    private static final long MIN_DISTANCE_CHANGE_TO_UPDATE = 1;
    private static final long MIN_TIME_TO_UPDATE = 1000 * 30 * 1;

    private LocationManager locationManager;

    public GpsTrackerService(Context context) {
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        gpsLocalizator = new GpsListener(context);
    }

    public void startLocalize() {
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_TO_UPDATE, MIN_DISTANCE_CHANGE_TO_UPDATE, gpsLocalizator);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_TO_UPDATE, MIN_DISTANCE_CHANGE_TO_UPDATE, gpsLocalizator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endLocalize() {
        try {
            locationManager.removeUpdates(gpsLocalizator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
