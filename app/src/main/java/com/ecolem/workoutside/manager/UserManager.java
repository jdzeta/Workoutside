package com.ecolem.workoutside.manager;

import android.widget.TextView;

import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.User;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by akawa_000 on 24/10/2015.
 */
public class UserManager {

    public static UserManager sInstance = null;

    public static UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }

        return sInstance;
    }

    public void saveUser(User user) {
        FirebaseManager.getInstance().getFirebaseRef().setValue(user);
    }

    public void createUser(String email, String password, final UserListener listener) {
        FirebaseManager.getInstance().getFirebaseRef().createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                if (listener != null){
                    listener.onCreateUserSuccess();
                }
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                if (listener != null){
                    listener.onFail(firebaseError);
                }
            }
        });
    }

    public void getUser(String pseudo, final UserListener listener) {
        Firebase userRef = FirebaseManager.getInstance().getFirebaseRef().child(pseudo);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " user(s)");
                User user = snapshot.getValue(User.class);

                if (listener != null) {
                    listener.onGetUserSuccess(user);
                }

                /*userFields.get("pseudo").setText(user.getPseudo());
                userFields.get("nom").setText(user.getNom());
                userFields.get("prenom").setText(user.getPrenom());
                userFields.get("email").setText(user.getEmail());
                userFields.get("sexe").setText(user.getSexe());
                userFields.get("date").setText(user.getDateNaissance().toString());
                userFields.get("ville").setText(user.getVille());
                userFields.get("niveau").setText(user.getNiveau());

                userFields.get("description").setText(user.getDescription());
                userFields.get("taille").setText(user.getTaille() + " cm");
                userFields.get("poids").setText(user.getPoids() + " kg");*/
                /**@TODO
                 * Set liste amis
                 * Bouton change mdp
                 **/
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if (listener != null) {
                    listener.onFail(firebaseError);
                }

                // userFields.get("erreur").setText("Utilisateur inconnu");
            }
        });
    }


    public void login(String email, String password, final LoginListener listener) {


        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                if (listener != null) {
                    listener.onLoginSuccess();
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                if (listener != null) {
                    listener.onLoginFail(firebaseError);
                }
            }
        };

        FirebaseManager.getInstance().getFirebaseRef().authWithPassword(email, password, authResultHandler);
    }

    public void logout() {
        FirebaseManager.getInstance().getFirebaseRef().unauth();
    }

    /**
     * Listeners
     **/

    public interface UserListener {
        void onGetUserSuccess(User user);
        void onCreateUserSuccess();
        void onFail(FirebaseError error);
    }

    public interface LoginListener {
        void onLoginSuccess();

        void onLoginFail(FirebaseError error);
    }


}
