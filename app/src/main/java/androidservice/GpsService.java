package androidservice;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import services.GpsTrackerService;

/**
 * Created by Michał on 2015-05-10.
 */
public class GpsService extends Service {
        GpsTrackerService gpsTracker;

        private void writeToLogs(String message) {
            Log.d("GpsService", message);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            writeToLogs("Called onCreate() method.");
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            writeToLogs("Called onStartCommand() method");

            gpsTracker=new GpsTrackerService(getApplicationContext());
            gpsTracker.startLocalize();

            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onDestroy() {
            writeToLogs("Called onDestroy() method");

            gpsTracker.endLocalize();

            super.onDestroy();
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }
    }
