package com.example.slawek.sziolmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import models.ClientModel;
import services.RestClientService;
import services.RestService;
import utils.BaseHelper;
import utils.BaseVariables;

/**
 * Created by Michał on 2015-05-10.
 */
public class EditClientActivity extends Activity {
    EditText fn, ln, flat, home, street, city;

    Button bt;
    ClientModel singleClientEdit;

    RestClientService restClientService;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);

        singleClientEdit = ClientsActivitySettings.getSingleClient();
        bt = (Button) findViewById(R.id.BT_edit_client_save);

        fn = (EditText) findViewById(R.id.ET_edit_firstName);
        ln = (EditText) findViewById(R.id.ET_edit_lastName);
        flat = (EditText) findViewById(R.id.ET_edit_flatNumber);
        street = (EditText) findViewById(R.id.ET_edit_street);
        city = (EditText) findViewById(R.id.ET_edit_city);
        home = (EditText) findViewById(R.id.ET_edit_homeNumber);

        fn.setText(singleClientEdit.getFirstName());
        ln.setText(singleClientEdit.getLastName());
        flat.setText(singleClientEdit.getFlatNumber());
        street.setText(singleClientEdit.getStreet());
        home.setText(singleClientEdit.getHomeNumber());
        city.setText(singleClientEdit.getCity());

        restClientService = new RestClientService(BaseVariables.RestUrl, getBaseContext());
        restService = new RestService(restClientService);
    }

    public void saveEditClientOnClick(final View v) {

        singleClientEdit.setId(singleClientEdit.getId());
        singleClientEdit.setFirstName(fn.getText().toString());
        singleClientEdit.setLastName(ln.getText().toString());
        singleClientEdit.setCity(city.getText().toString());
        singleClientEdit.setStreet(street.getText().toString());
        singleClientEdit.setHomeNumber(home.getText().toString());
        singleClientEdit.setFlatNumber(flat.getText().toString());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    int statusCode = restService.EditCustomer(singleClientEdit.getId(), singleClientEdit);

                    if(statusCode == 200)
                    {
                        BaseHelper.ShowMessage(getBaseContext(), "Edytowano klienta");
                    }
                    else
                    {
                        BaseHelper.ShowMessage(getBaseContext(), "Błąd podczas edycji klienta");
                    }

                    Intent intent = new Intent(v.getContext(), NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    EditClientActivity.this.startActivity(intent);
                    finish();
                } catch (Exception ex) {
                    BaseHelper.ShowMessage(getBaseContext(), "Brak połączenia");
                }
            }
        });
    }
}
