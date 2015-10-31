package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.manager.MovementManager;
import com.ecolem.workoutside.model.Movement;

public class NewMovementActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mouvement);
    }

    public void onSubmit(View view) {

        EditText nom = (EditText) findViewById(R.id.movement_nom);
        EditText description = (EditText) findViewById(R.id.movement_description);
        EditText image = (EditText) findViewById(R.id.movement_image);

        Movement newMovement = new Movement(nom.getText().toString(), image.getText().toString(), description.getText().toString());
        MovementManager.getInstance().sendData(newMovement);

        SharedPreferences.Editor edit = WorkoutSide.SHARED_PREFS.edit();
        edit.putString("current_mouvement", newMovement.getNom());
        edit.commit();

        Intent intent = new Intent(getApplicationContext(), MovementActivity.class);
        startActivity(intent);
        finish();
    }
}
