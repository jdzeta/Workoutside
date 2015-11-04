package com.ecolem.workoutside.manager;

import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Movement;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class TrainingManager {

    public static TrainingManager sInstance = null;
    public MoveListener mListener;

    public static TrainingManager getInstance() {
        if (sInstance == null) {
            sInstance = new TrainingManager();
        }

        return sInstance;
    }


    public void sendData(Movement movement) {
        FirebaseManager.getInstance().getFirebaseRef().setValue(movement);
    }

    public void startGetMove(String movName, final MoveListener listener) {

        Firebase movRef = FirebaseManager.getInstance().getFirebaseRef().child("catalog").child(movName);

        movRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " mouvement(s)");
                Movement move = snapshot.getValue(Movement.class);
                if (listener != null) {
                    listener.onGetMoveSuccess(move);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if (listener != null) {
                    listener.onFail(firebaseError);
                }
            }
        });
    }

    public void startGetMoves(final MoveListener listener) {
        Firebase catalogueRef = FirebaseManager.getInstance().getFirebaseRef().child("catalog");

        catalogueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " catalog(s)");

                ArrayList<Movement> movements = new ArrayList<Movement>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    movements.add(postSnapshot.getValue(Movement.class));
                }

                Collections.reverse(movements);

                if (listener != null) {
                    listener.onGetMovesSuccess(movements);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if (listener != null) {
                    listener.onFail(firebaseError);
                }
            }
        });
    }

    public interface MoveListener {
        public void onGetMoveSuccess(Movement m);

        public void onGetMovesSuccess(ArrayList<Movement> moves);

        public void onFail(FirebaseError error);
    }
}
