package com.rwssistent.LARA.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.rwssistent.LARA.R;

/**
 * Created by Samme on 19-3-2015.
 */
public class PreferenceHelper {

    public static boolean savePreference(Context context, String key, String value, String fileName) {
        try {
            if (context != null) {
                SharedPreferences prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, value);
                editor.commit();
                return true;
            }
        } catch (Exception e) {
            Log.e(PreferenceHelper.class.getName(), e.getMessage(), e);
        }
        return false;
    }

    public static String readPreference(Context context, String key, String defaultValue, String fileName) {
        String retVal = defaultValue;
        try {
            if (context != null) {
                SharedPreferences prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
                retVal = prefs.getString(key, defaultValue);
            }
        } catch (Exception e) {
            Log.e(PreferenceHelper.class.getName(), e.getMessage(), e);
        }
        return retVal;
    }
}
