package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

import models.ClientModel;
import models.TicketModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-10.
 */
public class NewOrderActivity extends Activity {

    EditText title, description, status;

    RestClientService restClientService;
    RestService restService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);

        title = (EditText) findViewById(R.id.ET_order_title);
        description = (EditText) findViewById(R.id.ET_order_description);
        status = (EditText) findViewById(R.id.ET_order_status);

        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);
    }

    public void addNewOrderActivityOnClick(final View v) {
        final ClientModel clientShort = Fragment_activity_client_settings.getSingleClient();

        final TicketModel ticket = new TicketModel();
        ticket.setCustomerId(clientShort.getId());
        ticket.setTitle(title.getText().toString());
        ticket.setDescription(description.getText().toString());
        //ticket.setStatus("CR");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    int status = restService.AddTicket(ticket);

                    if (status == 201) {
                        BaseHelper.ShowMessage(getBaseContext(), "Dodano zlecenie");
                    } else {
                        BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas dodawania zlecenia");
                    }

                    Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NewOrderActivity.this.startActivity(myIntent);
                    finish();
                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                }
            }
        });
    }
}
