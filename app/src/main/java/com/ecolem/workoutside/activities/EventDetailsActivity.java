package com.ecolem.workoutside.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecolem.workoutside.R;
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
    private TextView event_detail_datetime;
    private TextView event_detail_location;

    private LinearLayout event_detail_buttons;
    private Button event_detail_button_participate;
    private Button event_detail_button_desistate;

    private TextView event_detail_nb_participants;
    private ListView event_detail_participant_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        /* Setting tempEvent data
        String name = "My Event";
        Date date = Calendar.getInstance(Locale.FRANCE).getTime();
        String description = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Voluptatibus distinctio omnis inventore, quidem magnam eaque voluptates optio cum ullam autem natus nisi mollitia quae deleniti, ab fugiat iusto itaque. Minus.";
        Integer minLevel = 1;
        Integer maxParticipants = 3;
        User creator = UserManager.getInstance().getUser();
        this.myEvent = new Event(name, date, 12.3, -0.3, description, minLevel, maxParticipants, creator);*/

        // Getting selected event
        Bundle bundle = getIntent().getExtras();
        String evUuid = bundle.getString("eventUUID");
        EventManager eventManager = EventManager.getInstance();
        eventManager.getEvent(evUuid, this);

        // INIT EVENT DETAILS
        this.event_detail_creator  = (TextView) findViewById(R.id.event_detail_creator);
        this.event_detail_name  = (TextView) findViewById(R.id.event_detail_name);
        this.event_detail_min_level  = (TextView) findViewById(R.id.event_detail_min_level);
        this.event_detail_datetime  = (TextView) findViewById(R.id.event_detail_datetime);
        this.event_detail_location  = (TextView) findViewById(R.id.event_detail_location);
        this.event_detail_nb_participants  = (TextView) findViewById(R.id.event_detail_nb_participants);
        this.event_detail_participant_list = (ListView) findViewById(R.id.event_detail_participant_list);

    }

    public void settingViews() {
        this.event_detail_creator.setText(this.myEvent.getCreator().getFirstname() + " " + this.myEvent.getCreator().getLastname());
        this.event_detail_name.setText(this.myEvent.getName());
        // Defining min level
        String minLevel;
        switch (this.myEvent.getMinLevel()){
            case 0:
            default:
                minLevel = "Débutant";
                break;
            case 1:
                minLevel = "Intermédiaire";
                break;
            case 2:
                minLevel = "Avancé";
                break;
            case 3:
                minLevel = "Expert";
                break;
        }
        this.event_detail_min_level.setText(minLevel);

        // Turning Time to Datetime
        Date evDate = this.myEvent.getDate();
        DateFormat dateFormat = DateFormat.getDateInstance();
        this.event_detail_datetime.setText(dateFormat.format(evDate));

        // Turning location to address
        double latitude = this.myEvent.getLatitude();
        double longitude = this.myEvent.getLongitude();
        GeoLocation evLocation = new GeoLocation(latitude, longitude);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(evLocation.latitude, evLocation.longitude, 1);
            System.out.println(addresses);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.event_detail_location.setText(addresses.get(0).toString());

        // Counting participants
        HashMap<Integer, User> listParticipants = this.myEvent.getParticipants();
        this.event_detail_nb_participants.setText(listParticipants + "");
    }

    @Override
    public void onGetEventSuccess(Event event) {
        this.myEvent = event;
        // Setting event name in actionbar
        setTitle(this.myEvent.getName());
        settingViews();
    }

    @Override
    public void onGetEventsSuccess(ArrayList<Event> events) {

    }

    @Override
    public void onFail(FirebaseError error) {

    }
}
