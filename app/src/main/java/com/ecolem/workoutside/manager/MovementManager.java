package com.ecolem.workoutside.manager;

import android.widget.TextView;

import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Movement;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class MovementManager {

    public static MovementManager sInstance = null;

    public static MovementManager getInstance(){
        if(sInstance == null){
            sInstance = new MovementManager();
        }

        return sInstance;
    }


    public void sendData(Movement movement){
        FirebaseManager.getInstance().getFirebaseRef().setValue(movement);
    }

    public void getData(String mouvementName, final HashMap<String, TextView> mouvFields){
        Firebase mouvRef = FirebaseManager.getInstance().getFirebaseRef().child(mouvementName);

        mouvRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " mouvement(s)");
                Movement movement = snapshot.getValue(Movement.class);

                mouvFields.get("nom").setText(movement.getNom());
                mouvFields.get("image").setText(movement.getImage());
                mouvFields.get("description").setText(movement.getDescription());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

                mouvFields.get("erreur").setText("Mouvement inconnu");
            }
        });
    }
}
