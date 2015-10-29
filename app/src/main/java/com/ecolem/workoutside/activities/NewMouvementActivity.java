package com.ecolem.workoutside.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ecolem.workoutside.MyActivity;
import com.ecolem.workoutside.R;
import com.ecolem.workoutside.manager.MouvementManager;
import com.ecolem.workoutside.object.Mouvement;

public class NewMouvementActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mouvement);
    }

    public void onSubmit(View view) {
        MouvementManager mouvementManager = new MouvementManager(ref.child("mouvements"));

        EditText nom = (EditText) findViewById(R.id.new_mouvement_nom);
        EditText description = (EditText) findViewById(R.id.new_mouvement_description);
        EditText image = (EditText) findViewById(R.id.new_mouvement_image);

        Mouvement newMouvement = new Mouvement(nom.getText().toString(), image.getText().toString(), description.getText().toString());
        mouvementManager.sendData(newMouvement);

        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("current_mouvement", newMouvement.getNom());
        edit.commit();

        Intent intent = new Intent(getApplicationContext(), MouvementActivity.class);
        startActivity(intent);
        finish();
    }
}
