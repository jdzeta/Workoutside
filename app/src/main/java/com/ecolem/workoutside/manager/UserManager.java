package com.ecolem.workoutside.manager;

import android.widget.TextView;

import com.ecolem.workoutside.object.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by akawa_000 on 24/10/2015.
 */
public class UserManager {

    private Firebase ref;
    public UserManager(Firebase ref) {
        this.ref = ref;
    }

    public void sendUserData(User user){
        HashMap<String, String> userData = user.userMap();
        HashMap<String, HashMap<String, String>> usersData = new HashMap<String, HashMap<String, String>>();
        usersData.put(user.getPseudo(), userData);

        ref.setValue(usersData);

        Firebase userRef = ref.child(user.getPseudo());
        userRef.setValue("amis", user.getAmis());
    }

    public void setCompteData(String pseudo, final HashMap<String, TextView> userFields){
        Firebase userRef = ref.child(pseudo);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " user(s)");
                User user = snapshot.getValue(User.class);

                userFields.get("pseudo").setText(user.getPseudo());
                userFields.get("nom").setText(user.getNom());
                userFields.get("prenom").setText(user.getPrenom());
                userFields.get("email").setText(user.getEmail());
                userFields.get("sexe").setText(user.getSexe());
                userFields.get("date").setText(user.getDateNaissance());
                userFields.get("ville").setText(user.getVille());
                userFields.get("niveau").setText(user.getNiveau());

                userFields.get("description").setText(user.getDescription());
                userFields.get("taille").setText(user.getTaille() + " cm");
                userFields.get("poids").setText(user.getPoids() + " kg");

                /**@TODO
                 * Set liste amis
                 * Bouton change mdp
                 **/
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

                userFields.get("erreur").setText("Utilisateur inconnu");
            }
        });
    }
}
