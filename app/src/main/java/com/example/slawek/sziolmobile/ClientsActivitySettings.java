package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import androidservice.SziolLogic;
import conventers.ModelConverter;
import models.ClientModel;
import services.RestClientService;
import services.RestService;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-10.
 */
public class ClientsActivitySettings extends Activity {

    TextView tv;
    JSONObject jsonObj;

    private static ClientModel singleClient;


    public static ClientModel cl;

    public static ClientModel getSingleClient()
    {
        return singleClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ModelConverter modelConverter = new ModelConverter();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_options);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RestClientService restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        final RestService restService = new RestService(restClientService);
        runOnUiThread(new Runnable() {
            public void run() {


                try
                {
                    restService.GetClientById(Fragment_clients.getClient().getId());

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        try {
            try {
                jsonObj = new JSONObject(restService.GetContent());
                singleClient = modelConverter.ConvertClient(jsonObj);
            } catch (JSONException e) {
            }

            tv = (TextView) findViewById(R.id.ET_clients_settings);

            tv.append(SziolLogic.GetTextFromClient(singleClient));

        }
        catch(Exception ex)
        {}


    }

    public void editClientOnClick(View v) {
        Intent myIntent = new Intent(v.getContext(), EditClientActivity.class);
        ClientsActivitySettings.this.startActivity(myIntent);
       this.finish();
    }


    public void addNewOrderOnClick(View v)
    {
        Intent myIntent = new Intent(v.getContext(), NewOrderActivity.class);
        ClientsActivitySettings.this.startActivity(myIntent);
       // finish();
    }
}
