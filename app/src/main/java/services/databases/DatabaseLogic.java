package services.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import models.ConfigurationModel;

/**
 * Created by Slawek on 2015-07-15.
 */
public class DatabaseLogic {
    private SQLiteDatabase db;

    public DatabaseLogic(Context context)
    {
        DatabaseDbHelper databaseDbHelper = new DatabaseDbHelper(context);
        db = databaseDbHelper.getWritableDatabase();
    }

    public void InsertConfiguration(ConfigurationModel configuration)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ConfigurationEntry.COLUMN_KEY, configuration.getKey());
        values.put(DatabaseContract.ConfigurationEntry.COLUMN_VALUE, configuration.getValue());

              db.insert(DatabaseContract.ConfigurationEntry.TABLE_NAME,
                        null,
                        values);
    }

    public ConfigurationModel GetConfiguration(String key)
    {
        String selectQuery = "SELECT " +
                DatabaseContract.ConfigurationEntry.COLUMN_KEY + ", " +
                DatabaseContract.ConfigurationEntry.COLUMN_VALUE + " FROM " +
                DatabaseContract.ConfigurationEntry.TABLE_NAME + " WHERE " +
                DatabaseContract.ConfigurationEntry.COLUMN_KEY + "=?";

        Cursor c = db.rawQuery(selectQuery, new String[]{key});

        if(c.moveToFirst())
        {
            ConfigurationModel config = new ConfigurationModel();
            int index = c.getColumnIndexOrThrow(DatabaseContract.ConfigurationEntry.COLUMN_KEY);
            config.setKey(c.getString(index));
            index = c.getColumnIndexOrThrow(DatabaseContract.ConfigurationEntry.COLUMN_VALUE);
            config.setValue(c.getString(index));

            return config;
        }

        return null;
    }

    public List<ConfigurationModel> GetConfigurations()
    {
        String selectQuery = "SELECT " +
                DatabaseContract.ConfigurationEntry.COLUMN_KEY + ", " +
                DatabaseContract.ConfigurationEntry.COLUMN_VALUE + " FROM " +
                DatabaseContract.ConfigurationEntry.TABLE_NAME;

        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<ConfigurationModel> list = new ArrayList<>();
        while(c.moveToNext())
        {
            ConfigurationModel config = new ConfigurationModel();
            int index = c.getColumnIndexOrThrow(DatabaseContract.ConfigurationEntry.COLUMN_KEY);
            config.setKey(c.getString(index));
            index = c.getColumnIndexOrThrow(DatabaseContract.ConfigurationEntry.COLUMN_VALUE);
            config.setValue(c.getString(index));

            list.add(config);
        }

        return list;
    }

    public void DeleteConfiguration(String key)
    {
        String where  =  DatabaseContract.ConfigurationEntry.COLUMN_KEY + "=?";

        db.delete(DatabaseContract.ConfigurationEntry.TABLE_NAME,
                  where, new String[] {key});
    }

}
