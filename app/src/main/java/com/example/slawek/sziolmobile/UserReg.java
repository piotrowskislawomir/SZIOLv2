package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import models.UserModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-04-11.
 */
public class UserReg extends Activity {

    private EditText login;
    private EditText pass;
    private EditText pass2;
    private EditText firstName;
    private EditText lastName;
    private EditText teamKey;
    private TextView tv;

    RestClientService restClientService;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        tv = (TextView) findViewById(R.id.textView7);
        login = (EditText) findViewById(R.id.ET_log_reg);
        pass = (EditText) findViewById(R.id.ET_pass_reg);
        pass2 = (EditText) findViewById(R.id.ET_pass_reg_2);
        firstName = (EditText) findViewById(R.id.ET_name_reg);
        lastName = (EditText) findViewById(R.id.ET_lastName_reg);
        teamKey = (EditText) findViewById(R.id.ET_reg_key);

        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);
    }


    public void registerButtonOnClick(View v) {

        if (!ValidInput()) {
            BaseHelper.ShowMessage(getBaseContext(), "Wszystkie pola muszą zostać uzupełnione");
        }
        if (!pass.getText().toString().equals(pass2.getText().toString())) {

            BaseHelper.ShowMessage(getBaseContext(), "Hasła różnią się");
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                UserModel newUser = new UserModel();
                newUser.setUserName(login.getText().toString());
                newUser.setPassword(pass.getText().toString());
                newUser.setFirstName(firstName.getText().toString());
                newUser.setLastName(lastName.getText().toString());

                String teamKeyActivity = teamKey.getText().toString();

                try {
                    int restStatus = restService.RegisterUser(newUser, teamKeyActivity);
                    if (restStatus == 200) {
                        BaseHelper.ShowMessage(getBaseContext(), "Użytkownik został zarejestrowany.");

                        Intent myIntent = new Intent(UserReg.this, UserLog.class);
                        UserReg.this.startActivity(myIntent);
                        finish();
                    } else {
                        JSONObject jsonObj = new JSONObject(restService.GetContent());
                        String message = jsonObj.get("Message").toString();

                        if (message != null && !message.isEmpty()) {
                            BaseHelper.ShowMessage(getBaseContext(), "Rejestracja nie powiodła się");
                        }
                    }

                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                }
            }
        });
    }

    private boolean ValidInput() {
        return !tv.getText().toString().isEmpty() &&
                !login.getText().toString().isEmpty() &&
                !pass.getText().toString().isEmpty() &&
                !pass2.getText().toString().isEmpty() &&
                !firstName.getText().toString().isEmpty() &&
                !lastName.getText().toString().isEmpty()&&
                !teamKey.getText().toString().isEmpty();
    }
}
