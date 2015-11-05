package com.ecolem.workoutside.activities;

import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.helpers.GeolocHelper;
import com.ecolem.workoutside.helpers.TimeHelper;
import com.ecolem.workoutside.manager.EventManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoLocation;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EventDetailsActivity extends ActionBarActivity implements EventManager.EventListener {

    // Temporary event for testing
    private Event myEvent;

    // Event details displayed
    private TextView event_detail_creator;
    private TextView event_detail_name;
    private TextView event_detail_min_level;
    private TextView event_detail_date;
    private TextView event_detail_hour;
    private TextView event_detail_location;
    private TextView event_detail_description;

    private Button event_detail_button_participate;


    private TextView event_detail_nb_participants;
    private ListView event_detail_participant_list;


    private ActionBar mActionBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // init actionbar
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
        mActionBar.setDisplayHomeAsUpEnabled(true);

        // Getting selected event
        Bundle bundle = getIntent().getExtras();
        String evUuid = bundle.getString("eventUUID");
        EventManager eventManager = EventManager.getInstance();
        eventManager.getEvent(evUuid, this);

        // INIT EVENT DETAILS
        this.event_detail_creator = (TextView) findViewById(R.id.event_detail_creator);
        this.event_detail_name = (TextView) findViewById(R.id.event_detail_name);
        this.event_detail_min_level = (TextView) findViewById(R.id.event_detail_min_level);
        this.event_detail_date = (TextView) findViewById(R.id.event_detail_date);
        this.event_detail_hour = (TextView) findViewById(R.id.event_detail_hour);
        this.event_detail_location = (TextView) findViewById(R.id.event_detail_location);
        this.event_detail_description = (TextView) findViewById(R.id.event_details_description);
        this.event_detail_nb_participants = (TextView) findViewById(R.id.event_detail_nb_participants);
        this.event_detail_participant_list = (ListView) findViewById(R.id.event_detail_participant_list);

    }

    public void settingViews() {

        this.event_detail_creator.setText(this.myEvent.getCreator().getFirstname() + " " + this.myEvent.getCreator().getLastname());
        this.event_detail_name.setText(this.myEvent.getName());
        this.event_detail_description.setText(this.myEvent.getDescription());

        // Defining min level

        switch (this.myEvent.getMinLevel()) {
            case 0:
                event_detail_min_level.setText(getResources().getString(R.string.level_0));
                break;
            case 1:
                event_detail_min_level.setText(getResources().getString(R.string.level_1));
                break;
            case 2:
                event_detail_min_level.setText(getResources().getString(R.string.level_2));
                break;
            case 3:
                event_detail_min_level.setText(getResources().getString(R.string.level_3));
                break;
            default:
                break;
        }


        // Turning Time to Datetime
        this.event_detail_date.setText(TimeHelper.getEventDateStr(this.myEvent.getDate(), true));
        this.event_detail_hour.setText(TimeHelper.getEventHourStr(this.myEvent.getDate()));

        // Turning location to address
        String address = GeolocHelper.getCityFromLatitudeLongitude(this, this.myEvent.getLatitude(), this.myEvent.getLongitude());
        this.event_detail_location.setText(address);

        // Counting participants
        HashMap<Integer, User> listParticipants = this.myEvent.getParticipants();
        this.event_detail_nb_participants.setText(listParticipants + "");
    }

    @Override
    public void onGetEventSuccess(Event event) {
        this.myEvent = event;
        // Setting event name in actionbar

        mActionBar.setTitle(this.myEvent.getName());

        settingViews();
    }

    @Override
    public void onGetEventsSuccess(ArrayList<Event> events) {

    }

    @Override
    public void onFail(FirebaseError error) {

    }
}
