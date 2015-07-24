package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import models.TicketModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-10.
 */

public class EditOrderActivity extends Activity {
    EditText title, desc, status;
    Button bt;
    TicketModel ticket;

    RestClientService restClientService;
    RestService restService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        ticket = OrdersActivitySettings.getSingleOrderFromSettings();

        bt = (Button) findViewById(R.id.BT_savae_change_order);
        title = (EditText) findViewById(R.id.ET_edit_order_title);
        desc = (EditText) findViewById(R.id.ET_edit_order_desc);
        status = (EditText) findViewById(R.id.ET_edit_order_status);

        title.setText(ticket.getTitle().toString());
        desc.setText(ticket.getDescription().toString());
        status.setText(ticket.getStatus().toString());

        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);
    }

    public void saveEditOrderOnClick(final View v) {

        ticket.setDescription(desc.getText().toString());
        ticket.setTitle(title.getText().toString());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    restService.EditTicket(ticket.getId(), ticket);

                    Intent intent = new Intent(v.getContext(), NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    EditOrderActivity.this.startActivity(intent);
                    finish();
                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                }
            }
        });
    }
}
