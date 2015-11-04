package com.ecolem.workoutside.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.ecolem.workoutside.WorkoutSide;

/**
 * Created by snabou on 03/11/2015.
 */
public class SharedPreferenceManager {

    private final static String PREF_NAME = "workoutside_prefs";
    private final static String KEY_EMAIL = "email";
    private final static String KEY_PASSORD = "passwordword";


    public static SharedPreferenceManager sInstance = null;
    private SharedPreferences mSharedPrefs = null;

    public SharedPreferenceManager(Context context) {
        this.mSharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceManager(context);
        }

        return sInstance;
    }

    public void saveLogin(String email, String password) {
        SharedPreferences.Editor editor = this.mSharedPrefs.edit();

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSORD, password);

        editor.commit();
    }

    public String getSavedEmail() {
        return mSharedPrefs.getString(KEY_EMAIL, "");
    }

    public String getSavedPassword() {
        return mSharedPrefs.getString(KEY_PASSORD, "");
    }
}
