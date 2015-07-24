package com.example.slawek.sziolmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import conventers.ModelConverter;
import models.ClientModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-06-01.
 */
public class Fragment_new_ticket extends Fragment {
    View rootView;

    RestClientService restClientService;
    RestService restService;

    public static ClientModel cl;

    List<String> listAdapter;

    List<ClientModel> clientsList;
    ListView lv;
    ClientModel client;

    public static ClientModel getClient()
    {
        return cl;
    }

    @Nullable
    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_client_menu, container, false);
        lv = (ListView) rootView.findViewById(R.id.LV_clients);

        RestClientService restClientService = new RestClientService(BaseVariables.RestUrl, getActivity().getBaseContext());
        RestService restService = new RestService(restClientService);
        try {
            restService.GetCustomers();
            JSONArray clients = new JSONArray(restService.GetContent());

            AddClientsToListView(clients);
        } catch (JSONException e) {
            BaseHelper.ShowMessage(getActivity().getBaseContext(), "Brak połączenia");
        } catch (Exception ex) {
            BaseHelper.ShowMessage(getActivity().getBaseContext(), "Brak połączenia");
        }
        return rootView;
    }

    private void AddClientsToListView(JSONArray clients) throws JSONException {
        ModelConverter modelConverter = new ModelConverter();
        listAdapter = new ArrayList<>();
        clientsList = new ArrayList<>();

        for (int i = 0; i < clients.length(); i++) {
            JSONObject jsonObj = clients.getJSONObject(i);
            ClientModel client = modelConverter.ConvertClient(jsonObj);
            clientsList.add(client);
            listAdapter.add(client.getFirstName() + " " + client.getLastName());
            ArrayAdapter<ClientModel> adap = new ArrayAdapter<ClientModel>(getActivity(), android.R.layout.simple_list_item_1, clientsList);
            lv.setAdapter(adap);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cl = (ClientModel) (lv.getItemAtPosition(position));
                    Intent myIntent = new Intent(view.getContext(), Fragment_activity_client_settings.class);
                    startActivity(myIntent);
                }
            });
        }
    }
}
