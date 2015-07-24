package com.example.slawek.sziolmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import models.ConfigurationDictioniary;
import models.ConfigurationModel;
import services.ExceptionLoggerService;
import services.databases.DatabaseLogic;


public class MainActivity extends ActionBarActivity {

    DatabaseLogic dbLogic;

 @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     Thread.setDefaultUncaughtExceptionHandler(
             new Thread.UncaughtExceptionHandler() {

                 @Override
                 public void uncaughtException(Thread thread, Throwable ex) {
                     Log.e("Error", "Unhandled exception: " + ex.getMessage());

                     ExceptionLoggerService exceptionLogger = new ExceptionLoggerService();
                     exceptionLogger.writefile("sziolerror.txt", ex.getMessage());
                     System.exit(1);
                 }
             });

     dbLogic = new DatabaseLogic(getBaseContext());
     ConfigurationModel configToken = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_TOKEN);

     if (configToken != null) {
         Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);
     }
 }

  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
  }

    public void buttonLogOnClick(View v)
    {
        Intent myIntent = new Intent(MainActivity.this, UserLog.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void buttonRegOnClick(View v)
    {
        Intent myIntent = new Intent(MainActivity.this, UserReg.class);
        MainActivity.this.startActivity(myIntent);
    }
}
