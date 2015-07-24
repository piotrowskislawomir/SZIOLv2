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

/**
 * Created by Michał on 2015-05-10.
 */

public class CardItemsDetails extends Activity {

    TextView tv;
    ClientModel cl;

    RestClientService restClientService;
    RestService restService;
    ModelConverter modelConverter = new ModelConverter();

    TicketModel ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                TicketModel ticketShort = Fragment_my_card.getTicket();

                try {
                    restService.GetTicket(ticketShort.getId());
                    JSONObject jsonObj = new JSONObject(restService.GetContent());
                    ticket = modelConverter.ConvertTicket(jsonObj);

                    GetClient(ticket);
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

    private void GetClient(TicketModel ticket) throws JSONException {
        restService.GetClientById(ticket.getCustomerId());
        JSONObject jsonObj = new JSONObject(restService.GetContent());
        ClientModel client = modelConverter.ConvertClient(jsonObj);

        tv = (TextView) findViewById(R.id.TV_card_order_unpin);

        tv.setText(SziolLogic.GetTextFromTicket(ticket) + SziolLogic.GetTextFromClient(client));

    }

    public void unpinOrderOnClick(final View v) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    int status = restService.UnPinTicket(ticket.getId(), ticket);

                    if (status == 200) {
                        BaseHelper.ShowMessage(getBaseContext(), "Opięto zlecenie");
                    } else {
                        BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas odpięcia");
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

        public void executeOrderOnClick(final View v) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            runOnUiThread(new Runnable() {
                public void run() {

                    try {
                        int status = restService.ExecuteTicket(ticket.getId(), ticket);

                        if (status == 200) {
                            BaseHelper.ShowMessage(getBaseContext(), "Zlecenie oznaczone jako realozowane");
                        } else {
                            BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas oznaczenia do realizacji");
                        }

                        Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        finish();
                    } catch (Exception ex) {
                        BaseHelper.ShowMessage(getBaseContext(), "Błąd połączenia");
                    }
                }
            });
        }

    public void closeOrderOnClick(final View v) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    int status = restService.CloseTicket(ticket.getId(), ticket);

                    if (status == 200) {
                        BaseHelper.ShowMessage(getBaseContext(), "Zamknięto zlecenie");
                    } else {
                        BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas zamknięcia zlecenia");
                    }

                    Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                    finish();

                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Błąd połączenia");
                }
            }
        });
    }


}


