package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import androidservice.GpsService;
import androidservice.NotificationService;
import models.ConfigurationDictioniary;
import models.ConfigurationModel;
import models.UserModel;
import services.RestClientService;
import services.RestService;
import services.databases.DatabaseLogic;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-04-11.
 */

    public class UserLog extends Activity {

    private EditText login;
    private EditText pass;

    DatabaseLogic dbLogic;
    RestClientService restClientService;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        login = (EditText) findViewById(R.id.ET_log_log);
        pass = (EditText) findViewById(R.id.ET_pass_log);

        dbLogic = new DatabaseLogic(getBaseContext());
        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void loginButtonOnClick(View v) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    UserModel user = new UserModel();
                    user.setUserName(login.getText().toString());
                    user.setPassword(pass.getText().toString());
                    int statusCode = restService.LoginUser(user);

                    if (statusCode  == 200) {
                        try {
                            JSONObject jsonObj = new JSONObject(restService.GetContent());
                            String res = jsonObj.get("Result").toString();
                            String token = jsonObj.get("Token").toString();

                            if (res == "true" && token != "") {

                               SaveUserData(user, token);
                               StartServices();

                                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                String message = jsonObj.get("Message").toString();

                                if (message != "null")
                                    BaseHelper.ShowMessage(getBaseContext(),  message);
                                else {
                                    BaseHelper.ShowMessage(getBaseContext(),  "Błędny login lub hasło");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        BaseHelper.ShowMessage(getBaseContext(),  "Brak połączenia");
                    }

                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(),  "Brak połączenia");
                }
            }
        });
    }

    private void StartServices() {
        try {
            stopNotificationService();
        } catch (Exception ex) {
        }
        startNotificationService();

        try {
            stopGpsService();
        } catch (Exception ex) {
        }
        startGpsService();
    }

    private void SaveUserData(UserModel user, String token) {
        dbLogic.DeleteConfiguration(ConfigurationDictioniary.USER_LOGIN);
        dbLogic.DeleteConfiguration(ConfigurationDictioniary.USER_PASSWORD);
        dbLogic.DeleteConfiguration(ConfigurationDictioniary.USER_TOKEN);

        ConfigurationModel configUserName = new ConfigurationModel(ConfigurationDictioniary.USER_LOGIN, user.getUserName());
        ConfigurationModel configPassword = new ConfigurationModel(ConfigurationDictioniary.USER_PASSWORD, user.getPassword());
        ConfigurationModel configToken = new ConfigurationModel(ConfigurationDictioniary.USER_TOKEN, token);

        dbLogic.InsertConfiguration(configUserName);
        dbLogic.InsertConfiguration(configPassword);
        dbLogic.InsertConfiguration(configToken);
    }

    public void startGpsService() {
        Intent serviceIntent = new Intent(this, GpsService.class);
        startService(serviceIntent);
    }

    public void stopGpsService() {
        Intent serviceIntent = new Intent(this, GpsService.class);
        stopService(serviceIntent);
    }

    public void startNotificationService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
    }

    public void stopNotificationService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }
}


