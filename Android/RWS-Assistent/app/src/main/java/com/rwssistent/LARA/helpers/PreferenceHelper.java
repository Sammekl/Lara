package com.rwssistent.LARA.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Samme on 19-3-2015.
 */
public class PreferenceHelper {

    public static boolean savePreference(Context activity, String key, String value, String fileName) {
        try {
            if (activity != null) {
                SharedPreferences prefs = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
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

    public static String readPreference(Context activity, String key, String defaultValue, String fileName) {
        String retVal = defaultValue;
        try {
            if (activity != null) {
                SharedPreferences prefs = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
                retVal = prefs.getString(key, defaultValue);
            }
        } catch (Exception e) {
            Log.e(PreferenceHelper.class.getName(), e.getMessage(), e);
        }
        return retVal;
    }
}
