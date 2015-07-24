package com.example.slawek.sziolmobile;

/**
 * Created by Michał on 2015-05-10.
 */


        import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import androidservice.SziolLogic;
import conventers.ModelConverter;
        import models.ClientModel;
        import models.NotificationModel;
import models.TicketModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
        import utils.BaseVariables;

public class NotificationReciver extends Activity {
    TextView tv;

    RestClientService restClientService;
    RestService restService;

    TicketModel ticket;
    NotificationModel notification;

    @Override
    protected void onResume()
    {
        super.onResume();
        LoadNotification();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_notification);

    }

    private void LoadNotification()
    {
        final ModelConverter modelConverter = new ModelConverter();
        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);

        try {

            notification = (NotificationModel) getIntent().getExtras().getSerializable("notification");

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            runOnUiThread(new Runnable() {
                public void run() {
                    try
                    {
                        restService.GetTicket(notification.getTicketId());

                        JSONObject jsonObj = new JSONObject(restService.GetContent());
                         ticket = modelConverter.ConvertTicket(jsonObj);

                        restService.GetClientById(ticket.getCustomerId());
                        jsonObj = new JSONObject(restService.GetContent());
                        ClientModel client = modelConverter.ConvertClient(jsonObj);

                        tv = (TextView) findViewById(R.id.TV_notification);

                        tv.setText(SziolLogic.GetTextFromTicket(ticket) + SziolLogic.GetTextFromClient(client));

                    } catch (Exception ex) {
                        BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                        finish();
                    }
                }
            });

           }catch (Exception ex)
        {

        }
    }

    public void acceptNotificationOnClick(final View v) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {

                try {
                    int status = restService.PinTicket(ticket.getId(), ticket);

                    restService.SendStatusNotification(notification.getId(), true);

                    if (status == 200) {
                        BaseHelper.ShowMessage(getBaseContext(), "Przypięto zlecenie");
                    } else {
                        BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas przypięcia zlecenia");
                    }

                    Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NotificationReciver.this.startActivity(myIntent);
                    finish();
                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Błąd połączenia");
                }
            }
        });
    }


    public void notAcceptNotificationOnClick(View v) {
        runOnUiThread(new Runnable() {
            public void run() {

                try {
                    restService.SendStatusNotification(notification.getId(), false);
                    finish();
                } catch (Exception ex) {
                   BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                }
            }
        });
    }
}
