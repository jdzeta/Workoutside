package com.ecolem.workoutside;

import android.app.Application;
import android.content.res.Resources;

import com.firebase.client.Firebase;

/**
 * Created by snabou on 09/10/2015.
 */
public class WorkoutSide  extends Application {

    public static final String FIREBASE_URL = "https://workout-side.firebaseio.com/";

    public static Resources APP_RESOURCES;

    @Override
    public void onCreate() {
        super.onCreate();

        APP_RESOURCES = getResources();

        Firebase.setAndroidContext(this);

    }
}
