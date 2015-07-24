package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Slawek on 2015-07-14.
 */
public class BaseHelper {
    public static Integer TryParseInt(String someText) {
        try {
            return Integer.parseInt(someText);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static void ShowMessage(Context context, String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }
}
