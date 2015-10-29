package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.manager.MouvementManager;
import com.ecolem.workoutside.object.Mouvement;

public class NewCatalogueActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_catalogue);
    }

    public void onSubmit(View view) {
        MouvementManager mouvementManager = new MouvementManager(FirebaseManager.getInstance().getFirebaseRef().child("mouvements"));

        EditText nom = (EditText) findViewById(R.id.new_mouvement_nom);
        EditText description = (EditText) findViewById(R.id.new_mouvement_description);
        EditText image = (EditText) findViewById(R.id.new_mouvement_image);

        Mouvement newMouvement = new Mouvement(nom.getText().toString(), image.getText().toString(), description.getText().toString());
        mouvementManager.sendData(newMouvement);

        SharedPreferences.Editor edit = WorkoutSide.SHARED_PREFS.edit();
        edit.putString("current_mouvement", newMouvement.getNom());
        edit.commit();

        Intent intent = new Intent(getApplicationContext(), MouvementActivity.class);
        startActivity(intent);
        finish();
    }
}
