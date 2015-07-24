package androidservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.slawek.sziolmobile.NotificationReciver;
import com.example.slawek.sziolmobile.OrdersActivitySettings;
import com.example.slawek.sziolmobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import conventers.ModelConverter;
import models.ConfigurationDictioniary;
import models.ConfigurationModel;
import models.NotificationModel;
import models.UserModel;
import services.RestClientService;
import services.RestService;
import services.databases.DatabaseLogic;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-10.
 */
public class NotificationService extends Service {
    private Timer timer;
    private TimerTask timerTask;

    RestClientService restClientService;
    RestService restService;
    DatabaseLogic dbLogic;

    public NotificationService()
    {
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            ConfigurationModel configToken = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_TOKEN);

            if (configToken != null) {
                int status = restService.GetNotifications();

                if (status != 200) {
                    Login();
                    restService.GetNotifications();
                }

                try {
                    JSONArray notifications = new JSONArray(restService.GetContent());
                    ProcessNotifications(notifications);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void ProcessNotifications(JSONArray notifications) {
        ModelConverter modelConverter = new ModelConverter();

        ConfigurationModel configNotify = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_NOTIFICATION);

        for (int i = 0; i < notifications.length(); i++) {
            try {
                JSONObject jsonObj = notifications.getJSONObject(i);

                NotificationModel notification = modelConverter.ConvertNotification(jsonObj);
                restService.DeleteNotification(notification.getId());

                SelectNotification(notification, configNotify);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void SelectNotification(NotificationModel notification, ConfigurationModel configNotify) {
        if(configNotify != null && Boolean.parseBoolean(configNotify.getValue())) {
            if(notification.getType().equals("NW"))
            {
                NotifyNearestWorker(notification);
            }
            else if(notification.getType().equals("CE"))
            {
                NotifyChangedExecutor(notification);
            }
            else if(notification.getType().equals("DT"))
            {
                NotifyDeletedTicket(notification);
            }
        }
        else
        {
            if(notification.getType().equals("NW")) {
                restService.SendStatusNotification(notification.getId(), false);
            }
        }
    }

    long[] pattern = {500,500,500,500,500,500,500,500};

    private void NotifyNearestWorker(NotificationModel notificationModel)
    {
        final NotificationManager mgr=
                (NotificationManager)this.getSystemService(getBaseContext().NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getBaseContext(), NotificationReciver.class);

        int uniqueNo =  (int)System.currentTimeMillis();

        notificationIntent.putExtra("notification", notificationModel);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.setAction(Integer.toString(uniqueNo));

        PendingIntent i=PendingIntent.getActivity(getBaseContext(),uniqueNo,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getBaseContext())
                        .setSmallIcon(R.drawable.notifyicon)
                        .setContentTitle(notificationModel.getTitle())
                        .setContentText(notificationModel.getDescription());

        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(i);
        builder.setVibrate(pattern);
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setBigContentTitle(notificationModel.getTitle());
        String[] lines = notificationModel.getDescription().split("\n");
        for(String line : lines)
        {
            style.addLine(line);
        }

        builder.setStyle(style);

        mgr.notify(uniqueNo, builder.build());
    }


    private void NotifyDeletedTicket(NotificationModel notificationModel)
    {
        final NotificationManager mgr=
                (NotificationManager)this.getSystemService(getBaseContext().NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notifyicon)
                        .setContentTitle(notificationModel.getTitle())
                        .setContentText(notificationModel.getDescription());

        int uniqueNo =  (int)System.currentTimeMillis();

        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setVibrate(pattern);
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setBigContentTitle(notificationModel.getTitle());
        String[] lines = notificationModel.getDescription().split("\n");
        for(String line : lines)
        {
            style.addLine(line);
        }
        builder.setStyle(style);

        mgr.notify(uniqueNo, builder.build());
    }

    private void NotifyChangedExecutor(NotificationModel notificationModel)
    {
        final NotificationManager mgr=
                (NotificationManager)this.getSystemService(getBaseContext().NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(getBaseContext(), OrdersActivitySettings.class);

        int uniqueNo =  (int)System.currentTimeMillis();

        notificationIntent.putExtra("notification", notificationModel);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.setAction(Integer.toString(uniqueNo));

        PendingIntent i=PendingIntent.getActivity(this,uniqueNo,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notifyicon)
                        .setContentTitle(notificationModel.getTitle())
                        .setContentText(notificationModel.getDescription());

        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(i);

        builder.setVibrate(pattern);
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setBigContentTitle(notificationModel.getTitle());
        String[] lines = notificationModel.getDescription().split("\n");
        for(String line : lines)
        {
            style.addLine(line);
        }

        builder.setStyle(style);

        mgr.notify(uniqueNo, builder.build());
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

                        restClientService  = new RestClientService(BaseVariables.RestUrl, getBaseContext());
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

    private void writeToLogs(String message) {
        Log.d("GpsService", message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbLogic = new DatabaseLogic(getBaseContext());
        restClientService  = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);

        writeToLogs("Called onStartCommand() method");
        clearTimerSchedule();
        initTask();
        timer.scheduleAtFixedRate(timerTask,  1000, 1 * 60 * 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private void clearTimerSchedule() {
        if(timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
    }

    private void initTask() {
        timerTask = new MyTimerTask();
    }

    @Override
    public void onDestroy() {
        writeToLogs("Called onDestroy() method");
        clearTimerSchedule();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}
