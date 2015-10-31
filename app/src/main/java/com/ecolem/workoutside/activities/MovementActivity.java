package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Movement;
import com.ecolem.workoutside.tools.ImageLoadTask;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MovementActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement);

        initActivity();
    }

    public void initActivity() {
        String movName = WorkoutSide.SHARED_PREFS.getString("sMovName", "");

        Firebase movRef = FirebaseManager.getInstance().getFirebaseRef().child("catalog").child(movName);

        movRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " catalog(s)");
                Movement movement = snapshot.getValue(Movement.class);

                TextView movName = (TextView) findViewById(R.id.movement_nom);
                TextView movDescription = (TextView) findViewById(R.id.movement_description);
                ImageView movImage = (ImageView) findViewById(R.id.movement_image);

                movName.setText(movement.getNom());
                movDescription.setText(movement.getDescription());

                // Setting movement image from URL
                new ImageLoadTask(movement.getImage(), movImage).execute();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
