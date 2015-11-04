package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.adapter.MovementListAdapter;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Movement;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class EventDetailsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_training);


    }


}
