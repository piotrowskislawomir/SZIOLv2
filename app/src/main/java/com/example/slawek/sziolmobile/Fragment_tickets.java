package com.example.slawek.sziolmobile;


//import android.app.Fragment;
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
import models.TicketModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-31.
 */
public class Fragment_tickets extends Fragment {

    View rootView;
    public static TicketModel ticket;
    ListView lv;

    RestClientService restClientService;
    RestService restService;

  @Nullable
  @Override
  public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      rootView = inflater.inflate(R.layout.activity_order_menu, container, false);

      lv = (ListView) rootView.findViewById(R.id.LV_orders);

      restClientService = new RestClientService(BaseVariables.RestUrl, getActivity().getBaseContext());
      restService = new RestService(restClientService);
      try {
          restService.GetTickets();
          JSONArray tickets = new JSONArray(restService.GetContent());

          AddOrdersToListView(tickets);
      } catch (JSONException e) {
          BaseHelper.ShowMessage(getActivity().getBaseContext(), "Brak połączenia");
      } catch (Exception ex) {
          BaseHelper.ShowMessage(getActivity().getBaseContext(), "Brak połączenia");
      }
      return rootView;
  }

    private void AddOrdersToListView(JSONArray tickets) throws JSONException {
        ModelConverter modelConverter = new ModelConverter();
        List<TicketModel> ticketList =  new ArrayList<TicketModel>();

        for(int i=0; i<tickets.length(); i++) {
                JSONObject jsonObj = tickets.getJSONObject(i);
                ticketList.add(modelConverter.ConvertTicket(jsonObj));
        }

        ArrayAdapter<TicketModel> adapt = new ArrayAdapter<TicketModel>(getActivity(),
                android.R.layout.simple_list_item_1, ticketList);

        lv.setAdapter(adapt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                ticket = (TicketModel) (lv.getItemAtPosition(position));
                Intent myIntent = new Intent(view.getContext(), OrdersActivitySettings.class);
                startActivity(myIntent);
            }
        });
    }
}
