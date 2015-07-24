package com.example.slawek.sziolmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import models.ClientModel;
import utils.BaseHelper;


public class Fragment_new_client extends Fragment {

    View rootView;
    private EditText firstNameClient;
    private EditText lastNameClient;
    private EditText streetClient;
    private EditText homeNumberClient;
    private EditText flatNumberClient;
    private EditText cityClient;

    private static ClientModel client;
    public static ClientModel getClient()
    {
        return client;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.activity_add_new_client, container, false);

        firstNameClient = (EditText)rootView.findViewById(R.id.ET_new_client_name);
        lastNameClient = (EditText)rootView.findViewById(R.id.ET_new_client_last_name);
        cityClient = (EditText)rootView.findViewById(R.id.ET_new_client_city);
        streetClient = (EditText)rootView.findViewById(R.id.ET_new_client_street);
        homeNumberClient = (EditText)rootView.findViewById(R.id.ET_client_home_number);
        flatNumberClient = (EditText)rootView.findViewById(R.id.ET_new_client_flat_number);

        Button button = (Button) rootView.findViewById(R.id.button13);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!firstNameClient.getText().toString().isEmpty() && !lastNameClient.getText().toString().isEmpty() &&
                        !cityClient.getText().toString().isEmpty() && !streetClient.getText().toString().isEmpty() &&
                        !homeNumberClient.getText().toString().isEmpty()) {

                  client = new ClientModel();
                  client.setFirstName(firstNameClient.getText().toString());
                  client.setLastName(lastNameClient.getText().toString());
                  client.setCity(cityClient.getText().toString());
                  client.setStreet(streetClient.getText().toString());
                  client.setHomeNumber(homeNumberClient.getText().toString());
                  client.setFlatNumber(flatNumberClient.getText().toString());

                    Intent intent = new Intent(getActivity(), ClientsLivePlace.class);
                    startActivity(intent);
            }
            else
                {
                    BaseHelper.ShowMessage(getActivity().getBaseContext(),"Błąd podczas dodawania klienta");
                }
            }
        });

        return rootView;
    }
}

