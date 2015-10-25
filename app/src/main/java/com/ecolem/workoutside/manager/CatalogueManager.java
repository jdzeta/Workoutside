package com.ecolem.workoutside.manager;

import com.ecolem.workoutside.object.Catalogue;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class CatalogueManager extends  MyManager {
    public CatalogueManager(Firebase ref) {
        super(ref);

        // ref = https://workout-side.firebaseio.com/categorieName
    }

    public void sendData(Catalogue catalogue){
        ref.setValue(catalogue.getNom(), catalogue);
    }

    public void setCatalogueData(String catalogueName){
        Firebase catalogueRef = ref.child(catalogueName);

        catalogueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " catalogue(s)");
                Catalogue catalogue = snapshot.getValue(Catalogue.class);

                //@TODO Set catalogue activity data
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
