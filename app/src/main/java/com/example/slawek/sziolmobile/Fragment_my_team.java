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
import models.CardModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-06-01.
 */
public class Fragment_my_team extends Fragment {

    View rootView;

    ListView lv;
    private static CardModel card;

    public static CardModel getCard() {
        return card;
    }

    RestClientService restClientService;
    RestService restService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_team_orders, container, false);
        lv = (ListView) rootView.findViewById(R.id.LV_orders);

        restClientService = new RestClientService(BaseVariables.RestUrl, getActivity().getBaseContext());
        restService = new RestService(restClientService);

        try {
            restService.GetTeams();
            JSONArray teamCards = new JSONArray(restService.GetContent());

            AddClientsToListView(teamCards);
        } catch (JSONException e) {
            BaseHelper.ShowMessage(getActivity().getBaseContext(), "Brak połączenia");
        } catch (Exception ex) {
            BaseHelper.ShowMessage(getActivity().getBaseContext(), "Brak połączenia");
        }

        return rootView;
    }

    private void AddClientsToListView(JSONArray teamCards) throws JSONException {
        ModelConverter modelConverter = new ModelConverter();

        List<CardModel> cardList = new ArrayList<>();

        for (int i = 0; i < teamCards.length(); i++) {
            JSONObject jsonObj = teamCards.getJSONObject(i);
            cardList.add(modelConverter.ConvertCard(jsonObj));
        }

        ArrayAdapter<CardModel> adap = new ArrayAdapter<CardModel>(getActivity(),
                android.R.layout.simple_list_item_1, cardList);

        lv.setAdapter(adap);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                card = (CardModel) (lv.getItemAtPosition(position));
                Intent myIntent = new Intent(view.getContext(), TeamOrdersList.class);
                startActivity(myIntent);
            }
        });
    }
}
