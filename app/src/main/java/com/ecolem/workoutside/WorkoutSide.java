package com.ecolem.workoutside;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by snabou on 09/10/2015.
 */
public class WorkoutSide  extends Application {

    public static final String FIREBASE_URL = "https://workout-side.firebaseio.com/";

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
