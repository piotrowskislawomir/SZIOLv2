package com.example.slawek.sziolmobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import models.ConfigurationDictioniary;
import models.ConfigurationModel;
import services.databases.DatabaseLogic;

/**
 * Created by Michał on 2015-05-31.
 */
public class Fragment_settings extends Fragment {

    DatabaseLogic dbLogic;

    View rootView;
    Switch switchLocalization;
    Switch switchNotification;

    public Fragment_settings()
    {}

    public Fragment_settings(Context context)
    {
        dbLogic = new DatabaseLogic(context);
    }

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_switch, container, false);
        switchLocalization = (Switch)rootView.findViewById(R.id.switchLocalization);
        switchNotification = (Switch)rootView.findViewById(R.id.switchNotifiaction);

        switchLocalization.setChecked(false);
        switchNotification.setChecked(false);

        ConfigurationModel configNotify = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_NOTIFICATION);
        ConfigurationModel configLocal = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_LOCALIZATION);

        if(configLocal != null)
        {
            switchNotification.setChecked(Boolean.parseBoolean(configLocal.getValue()));
        }

        if(configNotify != null)
        {
            switchLocalization.setChecked(Boolean.parseBoolean(configNotify.getValue()));
        }

        switchLocalization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigurationModel config = new ConfigurationModel(ConfigurationDictioniary.USER_LOCALIZATION, Boolean.toString(switchLocalization.isChecked()));
                dbLogic.DeleteConfiguration(ConfigurationDictioniary.USER_LOCALIZATION);
                dbLogic.InsertConfiguration(config);
            }
        });

        switchNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigurationModel config = new ConfigurationModel(ConfigurationDictioniary.USER_NOTIFICATION, Boolean.toString(switchNotification.isChecked()));
                dbLogic.DeleteConfiguration(ConfigurationDictioniary.USER_NOTIFICATION);
                dbLogic.InsertConfiguration(config);
            }
        });

        return rootView;
    }
}
