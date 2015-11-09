package com.ecolem.workoutside.helpers;

import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;
import com.firebase.client.Firebase;

/**
 * Created by Sandra on 06/11/15.
 */
public class UserHelper {

    public static void changePassword(String email, Firebase.ResultHandler resultHandler){
        // Call Firebase method to reset password
        FirebaseManager.getInstance().getFirebaseRef().resetPassword(email, resultHandler);
    }

    public static boolean currentUserIsCreator(Event event) {

        if (event == null)
            return false;

        User u = UserManager.getInstance().getUser();

        if (u != null && u.getUID() != null && event.getCreator() != null) {
            return u.getUID().equalsIgnoreCase(event.getCreator().getUID());
        }

        return false;
    }
}
