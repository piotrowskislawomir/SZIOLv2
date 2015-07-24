package services;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;

import models.ConfigurationDictioniary;
import models.ConfigurationModel;
import models.CoordinateModel;
import models.UserModel;
import services.databases.DatabaseLogic;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-09.
 */
public class GpsListener implements LocationListener {

    Context context;

    RestClientService restClientService;
    RestService restService;
    DatabaseLogic dbLogic;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public Location previousBestLocation = null;

    public GpsListener(Context context)
    {
        this.context= context;
        dbLogic = new DatabaseLogic(context);

        restClientService  = new RestClientService(BaseVariables.RestUrl, context);
        restService = new RestService(restClientService);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (isBetterLocation(location, previousBestLocation)) {
            CoordinateModel coordinate = new CoordinateModel(location.getLatitude(), location.getLongitude());

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ConfigurationModel configLocalization = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_LOCALIZATION);
            ConfigurationModel configToken = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_TOKEN);

            if (configToken != null && configLocalization != null && Boolean.parseBoolean(configLocalization.getValue())) {
                int status = restService.SendCoordinate(coordinate);

                if (status != 201) {
                    Login();
                    restService.SendCoordinate(coordinate);
                }
            }
            previousBestLocation = location;
        }
    }

    private boolean Login()
    {
        ConfigurationModel configLogin = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_LOGIN);
        ConfigurationModel configPassword = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_PASSWORD);

        if(configLogin != null && configPassword != null)
        {
            UserModel user = new UserModel();
            user.setUserName(configLogin.getValue());
            user.setPassword(configPassword.getValue());
            int status = restService.LoginUser(user);

            try
            {
                if(status == 200)
                {
                    JSONObject jsonObj = new JSONObject(restService.GetContent());
                    String result = jsonObj.get("Result").toString();
                    String token = jsonObj.get("Token").toString();

                    if(result == "true" && token != null)
                    {
                        dbLogic.DeleteConfiguration(ConfigurationDictioniary.USER_TOKEN);
                        ConfigurationModel configToken = new ConfigurationModel(ConfigurationDictioniary.USER_TOKEN, token);
                        dbLogic.InsertConfiguration(configToken);

                        restClientService  = new RestClientService(BaseVariables.RestUrl, context);
                        restService = new RestService(restClientService);

                        return  true;
                    }
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
