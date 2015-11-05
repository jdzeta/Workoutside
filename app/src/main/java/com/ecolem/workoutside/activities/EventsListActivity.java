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

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.adapter.EventListAdapter;
import com.ecolem.workoutside.comparators.EventDateComparator;
import com.ecolem.workoutside.manager.EventManager;
import com.ecolem.workoutside.model.Event;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class EventsListActivity extends ActionBarActivity implements EventManager.EventListener {

    private ListView mListView;

    private ArrayList<Event> mEvents = new ArrayList<>();
    private EventListAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mListView = (ListView) findViewById(R.id.events_listview);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.menu_events).toUpperCase());
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
            }
        });
        //EventManager.getInstance().startGetEventsComing(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventManager.getInstance().startGetEventsComing(this);
    }

    private void populateEvents() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        Collections.sort(mEvents, new EventDateComparator());

        mAdapter = new EventListAdapter(getApplicationContext(), mEvents);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_event) {
            Intent intent = new Intent(getApplication(), NewEventActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGetEventSuccess(Event m) {

    }

    @Override
    public void onGetEventsSuccess(ArrayList<Event> events) {
        mEvents = events;
        mAdapter = new EventListAdapter(getApplicationContext(), mEvents);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onFail(FirebaseError error) {

    }
}
