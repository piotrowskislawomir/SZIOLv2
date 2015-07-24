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
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-10.
 */
public class Fragment_activity_client_settings extends Activity {

    TextView tv;

    RestClientService restClientService;
    RestService restService;

    private static ClientModel singleClient;
    public static ClientModel getSingleClient()
    {
        return singleClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_ticket);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final ModelConverter modelConverter = new ModelConverter();
        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);

        runOnUiThread(new Runnable() {
            public void run() {

                try
                {
                    restService.GetClientById(Fragment_new_ticket.getClient().getId());

                    JSONObject jsonObj = new JSONObject(restService.GetContent());
                    singleClient = modelConverter.ConvertClient(jsonObj);

                    tv = (TextView) findViewById(R.id.ET_clients_settings);

                    tv.append(SziolLogic.GetTextFromClient(singleClient));
                }
                catch(JSONException e){
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                    finish();
                }
                catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                    finish();
                }
            }
        });
    }

    public void editClientOnClick(View v) {
        Intent myIntent = new Intent(v.getContext(), EditClientActivity.class);
        Fragment_activity_client_settings.this.startActivity(myIntent);
        this.finish();
    }

    public void addNewOrderOnClickFrag(View v)
    {
        Intent myIntent = new Intent(v.getContext(), NewOrderActivity.class);
        Fragment_activity_client_settings.this.startActivity(myIntent);
        finish();//bylo zakometowane
    }
}
