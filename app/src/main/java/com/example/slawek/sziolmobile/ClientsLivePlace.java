
package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import models.CoordinateModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;


public class ClientsLivePlace extends Activity {

    ListView lv;

    RestClientService restClientService;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_place);
        lv = (ListView) findViewById(R.id.LV_client_places);

        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);

        try {
            restService.GetClientPlaces(Fragment_new_client.getClient());
            JSONArray places = new JSONArray(restService.GetContent());

            AddClientsToListView(places);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
            finish();
        }
    }

    private void AddClientsToListView(JSONArray places) throws JSONException {
        ModelConverter modelConverter = new ModelConverter();
        List<CoordinateModel> coordinatesList = new ArrayList<CoordinateModel>();

        for (int i = 0; i < places.length(); i++) {
            JSONObject jsonObj = places.getJSONObject(i);
            coordinatesList.add(modelConverter.ConvertCoordinate(jsonObj));

            ArrayAdapter<CoordinateModel> adap = new ArrayAdapter<CoordinateModel>(this, android.R.layout.simple_list_item_1, coordinatesList);
            lv.setAdapter(adap);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    CoordinateModel coordinate = (CoordinateModel) (lv.getItemAtPosition(position));
                    try {
                        int restStatus = restService.AddCustomer(Fragment_new_client.getClient(), coordinate);

                        if (restStatus == 201) {
                            BaseHelper.ShowMessage(getBaseContext(), "Dodano klienta");
                            Intent myIntent = new Intent(view.getContext(), NavigationActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent);
                            finish();
                        } else {
                            BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas dodawania klienta");
                        }
                    } catch (Exception ex) {
                        BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                    }
                }
            });
        }
    }
}