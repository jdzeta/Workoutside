package com.ecolem.workoutside.manager;

import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Movement;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class MovementManager {

    public static MovementManager sInstance = null;
    public MovementListener mListener;

    public static MovementManager getInstance(){
        if(sInstance == null){
            sInstance = new MovementManager();
        }

        return sInstance;
    }

    public void setListener(MovementListener listener){
        this.mListener = listener;
    }


    public void sendData(Movement movement){
        FirebaseManager.getInstance().getFirebaseRef().setValue(movement);
    }

    public void getMovement(String movName){

        Firebase movRef = FirebaseManager.getInstance().getFirebaseRef().child("catalog").child(movName);

        movRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " mouvement(s)");
                Movement mouvement = snapshot.getValue(Movement.class);
                if(mListener != null){
                    mListener.onSuccess(mouvement);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if(mListener != null){
                    mListener.onFail(firebaseError);
                }
            }
        });
    }

    public interface MovementListener {
        public void onSuccess(Movement m);
        public void onFail(FirebaseError error);
    }
}
