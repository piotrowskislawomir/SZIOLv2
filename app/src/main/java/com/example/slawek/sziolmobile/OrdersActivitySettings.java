package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
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

/**
 * Created by Michał on 2015-05-10.
 */

public class OrdersActivitySettings extends Activity {

    TextView tv;
    static TicketModel ticket;

    RestClientService restClientService;
    RestService restService;

    public static TicketModel getSingleOrderFromSettings()
    {
       return ticket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ModelConverter modelConverter = new ModelConverter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_options);
        tv = (TextView) findViewById(R.id.ET_orders_settings);

        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);

        NotificationModel notificationModel = null;
        final TicketModel ticketShort;

        if (getIntent() != null)
            if (getIntent().getExtras() != null)
                if (getIntent().getExtras().getSerializable("notification") != null) {
                    notificationModel = (NotificationModel) getIntent().getExtras().getSerializable("notification");
                }

        if (notificationModel != null) {
            ticketShort = new TicketModel(notificationModel.getTicketId(), null);
        } else {
            ticketShort = Fragment_tickets.ticket;
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    restService.GetTicket(ticketShort.getId());
                    JSONObject jsonObj = new JSONObject(restService.GetContent());
                    ticket = modelConverter.ConvertTicket(jsonObj);

                    restService.GetClientById(ticket.getCustomerId());
                    jsonObj = new JSONObject(restService.GetContent());
                    ClientModel client = modelConverter.ConvertClient(jsonObj);

                    tv.append(SziolLogic.GetTextFromTicket(ticket) + SziolLogic.GetTextFromClient(client));
                } catch (JSONException e) {
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                    finish();
                } catch (Exception ex) {

                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                    finish();
                }
            }
        });
    }

    public void editSingleOrderOnClick(View v) {
        Intent myInt = new Intent(v.getContext(), EditOrderActivity.class);
        startActivity(myInt);
        this.finish();
    }

    public void pinOrderOnClick(final View v) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try
                {
                      int status =  restService.PinTicket(ticket.getId(), ticket);

                        if(status == 200)
                        {
                            BaseHelper.ShowMessage(getBaseContext(), "Przypięto zlecenie");
                        }
                        else
                        {
                            BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas przypinania zlecenia");
                        }

                        Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        finish();
                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                }
            }
        });
    }

    public void deleteOrderOnClick(final View v) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    restService.DeleteTicket(ticket.getId());

                    Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    OrdersActivitySettings.this.startActivity(myIntent);
                    finish();
                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                }
            }
        });
    }
}
