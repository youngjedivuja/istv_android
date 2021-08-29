package com.example.istv_andorid.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
    public static void saveString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static String getSavedString(Context context, String name) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(name, "");
    }

    public static void removeSavedKey(Context context, String name) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(name).apply();
    }
}
