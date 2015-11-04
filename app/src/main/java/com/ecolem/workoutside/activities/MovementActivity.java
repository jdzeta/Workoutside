package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.manager.MovementManager;
import com.ecolem.workoutside.model.Movement;
import com.ecolem.workoutside.tools.ImageLoadTask;
import com.firebase.client.FirebaseError;

public class MovementActivity extends Activity implements MovementManager.MovementListener{

    TextView mNom;
    TextView mDescription;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement);

        mNom = (TextView) findViewById(R.id.movement_nom);
        mDescription = (TextView) findViewById(R.id.movement_description);
        mImage = (ImageView) findViewById(R.id.movement_image);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String mName = getIntent().getExtras().getString("mName");

        MovementManager.getInstance().setListener(this);
        MovementManager.getInstance().getMovement(mName);
    }

    @Override
    public void onSuccess(Movement m) {
        mNom.setText(m.getNom());
        mDescription.setText(m.getDescription());

        new ImageLoadTask(m.getImage(), mImage).execute();
    }

    @Override
    public void onFail(FirebaseError error) {
        Toast.makeText(getApplicationContext(), "Error : Can't load movement data", Toast.LENGTH_SHORT).show();
    }
}
