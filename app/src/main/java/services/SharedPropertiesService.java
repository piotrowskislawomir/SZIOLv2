package services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.example.slawek.sziolmobile.R;

/**
 * Created by Slawek on 2015-05-23.
 */
public class SharedPropertiesService {

    Context _ctx;
    SharedPreferences _sharedPreferences;

    public SharedPropertiesService(Context ctx)
    {
        _ctx = ctx;
        Initialize();
    }

    public SharedPropertiesService(SharedPreferences sharedPreferences)
    {
        _sharedPreferences = sharedPreferences;
    }

    private void Initialize()
    {
        Resources res = _ctx.getResources();
        _sharedPreferences = _ctx.getSharedPreferences(res.getString(R.string.shared_space),Context.MODE_PRIVATE);
    }

    public void SetValue(String key, String value)
    {
        _sharedPreferences.edit().putString(key, value).apply();
    }

    public String GetValue(String key, String defaultValue)
    {
        return _sharedPreferences.getString(key, defaultValue);
    }
}
