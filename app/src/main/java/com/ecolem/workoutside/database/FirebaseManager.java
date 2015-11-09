package com.ecolem.workoutside.database;

import android.util.Log;

import com.ecolem.workoutside.WorkoutSide;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

/**
 * Created by snabou on 09/10/2015.
 */
public class FirebaseManager implements Firebase.AuthStateListener {


    public static FirebaseManager sInstance = null;
    private Firebase mFirebaseRef = null;
    private AuthenticationListener mListener = null;

    public FirebaseManager() {
        mFirebaseRef = new Firebase(WorkoutSide.FIREBASE_URL);
        mFirebaseRef.addAuthStateListener(this);

    }

    public static FirebaseManager getInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseManager();
        }

        return sInstance;
    }

    public void register(AuthenticationListener listener) {
        mListener = listener;
    }

    public void unregister() {

    }

    public Firebase getFirebaseRef() {
        return mFirebaseRef;
    }

    @Override
    public void onAuthStateChanged(AuthData authData) {
        Log.i("auth", "Is logged ? " + (authData != null));
        if (mListener != null) {
            mListener.onUserIsLogged(authData != null);
        }
    }

    public interface AuthenticationListener {
        void onUserIsLogged(boolean isLogged);
    }
}
