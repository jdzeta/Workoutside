package com.ecolem.workoutside.database;

import com.ecolem.workoutside.WorkoutSide;
import com.firebase.client.Firebase;

/**
 * Created by snabou on 09/10/2015.
 */
public class FirebaseManager {


    public static FirebaseManager sInstance = null;
    private Firebase mFirebaseRef = null;

    public FirebaseManager(){
        mFirebaseRef = new Firebase(WorkoutSide.FIREBASE_URL);
    }

    public static FirebaseManager getInstance(){
        if(sInstance == null){
            sInstance = new FirebaseManager();
        }

        return sInstance;
    }

    public Firebase getFirebaseRef(){
        return mFirebaseRef;
    }
}
