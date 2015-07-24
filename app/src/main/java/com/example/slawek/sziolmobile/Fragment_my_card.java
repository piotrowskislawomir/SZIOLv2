package com.example.slawek.sziolmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
import models.TicketModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-31.
 */
public class Fragment_my_card extends Fragment {

    View rootView;
    static TicketModel ticket;
    ListView lv;

    RestClientService restClientService;
    RestService restService;

    public static TicketModel getTicket() {
        return ticket;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        restClientService = new RestClientService(BaseVariables.RestUrl, getActivity().getBaseContext());
        restService = new RestService(restClientService);

        rootView = inflater.inflate(R.layout.activity_card_items, container, false);
        lv = (ListView) rootView.findViewById(R.id.LV_card_items);
        AddItemsFromCardToListView();

        return rootView;
    }

    private void AddItemsFromCardToListView() {
        ModelConverter modelConverter = new ModelConverter();
        List<TicketModel> cardList = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            restService.GetMyCard();
            JSONArray cardsItems = new JSONArray(restService.GetContent());

            for (int i = 0; i < cardsItems.length(); i++) {
                JSONObject jsonObj = cardsItems.getJSONObject(i);
                cardList.add(modelConverter.ConvertTicket(jsonObj));
                ArrayAdapter<TicketModel> adapt = new ArrayAdapter<TicketModel>(getActivity(), android.R.layout.simple_list_item_1, cardList);
                lv.setAdapter(adapt);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ticket = (TicketModel) (lv.getItemAtPosition(position));
                        Intent myIntent = new Intent(view.getContext(), CardItemsDetails.class);
                        startActivity(myIntent);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            BaseHelper.ShowMessage(getActivity().getBaseContext(), "Brak połączenia");
        } catch (Exception ex) {
            BaseHelper.ShowMessage(getActivity().getBaseContext(), "Brak połączenia");
        }
    }
}