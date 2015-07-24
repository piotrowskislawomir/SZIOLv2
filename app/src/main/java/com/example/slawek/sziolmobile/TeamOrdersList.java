

package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import conventers.ModelConverter;
import models.CardModel;
import models.TicketModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

public class TeamOrdersList extends Activity {

    ListView lv;
    static TicketModel ticket;

    RestClientService restClientService;
    RestService restService;

    public static TicketModel getCustomerOrder()
    {
        return ticket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_team_orders_card);
        lv = (ListView) findViewById(R.id.LV_customer_orders);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);

        runOnUiThread(new Runnable() {
            public void run() {
                CardModel cr = Fragment_my_team.getCard();
                try {
                    restService.GetCustomerCard(cr.getCardId());
                    JSONArray teamTickets = new JSONArray(restService.GetContent());

                    AddClientsToListView(teamTickets);
                } catch (JSONException e) {
                    BaseHelper.ShowMessage(getBaseContext(), "Błąd połączenia");
                    finish();
                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Błąd połączenia");
                    finish();
                }
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        super.onResume();
    }

    private void AddClientsToListView(JSONArray teamTickets) throws JSONException {
        ModelConverter modelConverter = new ModelConverter();
        List<TicketModel> ticketList =  new ArrayList<>();

        for(int i=0; i<teamTickets.length(); i++)
        {
                JSONObject jsonObj = teamTickets.getJSONObject(i);;
                TicketModel ticket = modelConverter.ConvertTicket(jsonObj);
                ticketList.add(ticket);
        }

        ArrayAdapter<TicketModel> adap = new ArrayAdapter<TicketModel>(this,
                android.R.layout.simple_list_item_1, ticketList);

        lv.setAdapter(adap);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                ticket =   (TicketModel)(lv.getItemAtPosition(position));
                Intent myIntent = new Intent(view.getContext(), TeamOrdersListDetails.class);
                startActivity(myIntent);
            }
        });

    }
}

