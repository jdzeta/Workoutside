package com.ecolem.workoutside.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.adapter.UserListAdapter;
import com.ecolem.workoutside.helpers.GeolocHelper;
import com.ecolem.workoutside.helpers.TimeHelper;
import com.ecolem.workoutside.manager.EventManager;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


    // Participation buttons
    private Button event_detail_button_participate;
    private boolean participate;

    // Participants number and list
    private TextView event_detail_nb_participants;
    private ListView event_detail_participant_list;

    private ActionBar mActionBar = null;

    // Current user
    private User currentUser;

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

        // Creator name
        this.event_detail_creator.setText(this.myEvent.getCreator().getFirstname() + " " + this.myEvent.getCreator().getLastname());
        // Event name
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

        // Counting participants and filling listView
        this.event_detail_button_participate = (Button) findViewById(R.id.event_detail_button_participate);
        int pSize = 0;
        if (this.myEvent.getParticipants() != null){
            pSize = this.myEvent.getParticipants().size();
        }
        this.event_detail_nb_participants.setText( pSize + " Participant(s)");
        initParticipantsList();

        // Setting participation to false, true if user is organizer
        UserManager userManager = UserManager.getInstance();
        this.currentUser = userManager.getUser();
        if (currentUser.getUID().equals(this.myEvent.getCreator().getUID()) || this.isParticipate()) {
            this.participate = true;
            this.event_detail_button_participate.setText(getString(R.string.event_detail_button_desistate));
        } else {
            this.participate = false;
        }
    }

    public boolean isParticipate(){
        HashMap<String, User> participants = this.myEvent.getParticipants();
        if (participants != null) {
            // Setting participants listView
            // Setting participants in Arraylist
            ArrayList<User> users = new ArrayList<>();
            for (Map.Entry<String, User> entry : participants.entrySet()) {
                if (entry.getKey().equals(this.currentUser.getUID())){
                    return true;
                }
            }
        }
        return false;
    }

    public void initParticipantsList() {
        // Getting participants
        HashMap<String, User> participants = this.myEvent.getParticipants();
        if (participants != null) {
            // Setting participants listView
            // Setting participants in Arraylist
            ArrayList<User> users = new ArrayList<>();
            for (Map.Entry<String, User> entry : participants.entrySet()) {
                users.add(entry.getValue());
            }
            UserListAdapter adapter = new UserListAdapter(this, users);
            this.event_detail_participant_list.setAdapter(adapter);
        }
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

    // Click on participate button
    public void participateClick(View view) {
        if (this.participate == true) {
            this.participate = false;
            this.event_detail_button_participate.setText(getString(R.string.event_detail_button_participate));
        } else {
            this.participate = true;
            this.event_detail_button_participate.setText(getString(R.string.event_detail_button_desistate));
        }
        participate();
    }

    // Sending participation to Firebase
    public void participate(){
        EventManager eventManager = EventManager.getInstance();

        if (this.participate) {
            // Updating event
            eventManager.pushParticipant(this.myEvent, this.currentUser);
        }
        else {
            eventManager.removeParticipant(this.myEvent, this.currentUser);
        }
    }

}
