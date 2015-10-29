package com.ecolem.workoutside.manager;

import android.widget.TextView;

import com.ecolem.workoutside.object.Mouvement;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class MouvementManager extends MyManager {

    public MouvementManager(Firebase ref) {
        super(ref);

        // ref = https://workout-side.firebaseio.com/categorieName/catalogueName/
    }

    public void sendData(Mouvement mouvement){
        ref.setValue(mouvement);
    }

    public void getData(String mouvementName, final HashMap<String, TextView> mouvFields){
        Firebase mouvRef = ref.child(mouvementName);

        mouvRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " mouvement(s)");
                Mouvement mouvement = snapshot.getValue(Mouvement.class);

                mouvFields.get("nom").setText(mouvement.getNom());
                mouvFields.get("image").setText(mouvement.getImage());
                mouvFields.get("description").setText(mouvement.getDescription());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

                mouvFields.get("erreur").setText("Mouvement inconnu");
            }
        });
    }
}
