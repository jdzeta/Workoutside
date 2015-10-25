package com.ecolem.workoutside.manager;

import android.widget.TextView;

import com.ecolem.workoutside.object.Categorie;
import com.ecolem.workoutside.object.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class CategorieManager {

    private Firebase ref;

    public CategorieManager(Firebase ref) {
        this.ref = ref;
    }

    public void sendData(Categorie categorie){
        HashMap<String, String> catData = categorie.catMap();
        HashMap<String, HashMap<String, String>> usersData = new HashMap<String, HashMap<String, String>>();
        usersData.put(categorie.getNom(), catData);

        ref.setValue(usersData);

        Firebase userRef = ref.child(categorie.getNom());
        userRef.setValue("catalogue", categorie.getCatalogues());
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
