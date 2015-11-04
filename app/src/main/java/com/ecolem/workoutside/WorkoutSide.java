package com.ecolem.workoutside;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;

/**
 * Created by snabou on 09/10/2015.
 */
public class WorkoutSide extends Application {

    private static WorkoutSide mInstance;

    public static Context APP_CONTEXT;
    public static Resources APP_RESOURCES;

    public static final String FIREBASE_URL = "https://workout-side.firebaseio.com/";
    public static final String FIREBASE_GEOFIRE_URL = "https://publicdata-transit.firebaseio.com/_geofire";

    public WorkoutSide() {

    }

    public WorkoutSide(Context context) {
        APP_CONTEXT = context;
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
    }
}
