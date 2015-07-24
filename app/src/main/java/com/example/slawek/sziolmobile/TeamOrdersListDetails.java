

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
import models.TicketModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;


public class TeamOrdersListDetails extends Activity {

    TicketModel ticket;
    TextView tv;

    RestClientService restClientService;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ModelConverter modelConverter = new ModelConverter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_orders_card_details);
        tv = (TextView) findViewById(R.id.ET_team_card_order_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);

        final TicketModel ticketShort = TeamOrdersList.getCustomerOrder();

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


    public void takeOrderOtherCustomer(final View v)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    int status = restService.PinTicket(ticket.getId(), ticket);

                    if (status == 200) {
                        BaseHelper.ShowMessage(getBaseContext(), "Przypięto zlecenie");
                    } else {
                        BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas przypinania");
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


    @Override
    public void onRestart() {
        super.onRestart();
        super.onResume();
    }
}
