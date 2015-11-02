package com.ecolem.workoutside;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;

/**
 * Created by snabou on 09/10/2015.
 */
public class WorkoutSide  extends Application {

    private static WorkoutSide mInstance;
    private static Context mCtx;

    public static final String FIREBASE_URL = "https://workout-side.firebaseio.com/";
    public static Resources APP_RESOURCES;

    public static SharedPreferences SHARED_PREFS;

    private WorkoutSide(Context context) {
        mCtx = context;
    }

    public static synchronized WorkoutSide getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WorkoutSide(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        APP_RESOURCES = getResources();

        Firebase.setAndroidContext(this);

        SHARED_PREFS = PreferenceManager.getDefaultSharedPreferences(this);

        System.out.println("STARTING APPLICATION");

    }
}
