package com.ecolem.workoutside.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.adapter.EventListAdapter;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.manager.EventManager;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyEventsActivity extends ActionBarActivity implements FirebaseManager.AuthenticationListener, EventManager.EventListener {

    private ListView mListView;

    private EventListAdapter mAdapter = null;
    private ArrayList<Event> myEvents;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        mListView = (ListView) findViewById(R.id.my_events_listview);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.my_events_activity_title).toUpperCase());
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

        //populateEvents();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) mListView.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("eventUUID", event.getUID());
                Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                MyEventsActivity.this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventManager.getInstance().startGetEventsComing(this);
        FirebaseManager.getInstance().register(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_event) {
            Intent intent = new Intent(getApplication(), NewEventActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGetEventSuccess(Event m) {

    }

    @Override
    public void onGetEventsSuccess(ArrayList<Event> events) {
        ArrayList<Event> mEvents = new ArrayList<>(events);
        // Browse events to get only events where user is
        currentUser = UserManager.getInstance().getUser();
        myEvents = new ArrayList<>();
        for (Event event : events) {
            HashMap<String, User> participants = event.getParticipants();
            if (event.getCreator().getUID().equals(currentUser.getUID())) {
                myEvents.add(event);
            } else {
                if (participants != null) {
                    for (Map.Entry<String, User> entry : participants.entrySet()) {
                        if (entry.getKey().equals(currentUser.getUID())) {
                            myEvents.add(event);
                        }
                    }
                }
            }
        }
        mAdapter = new EventListAdapter(getApplicationContext(), myEvents);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onFail(FirebaseError error) {

    }

    @Override
    public void onUserIsLogged(boolean isLogged) {
        if (!isLogged) {
            Toast.makeText(this, "Vous êtes déconnecté", Toast.LENGTH_LONG).show();
            Intent newIntent = new Intent(MyEventsActivity.this, StartActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
