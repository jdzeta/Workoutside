package com.ecolem.workoutside.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MyEventDetailsActivity extends ActionBarActivity  implements EventManager.EventListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    // Temporary event for testing
    private Event myEvent;

    // Event details displayed
    private TextView my_event_details_creator;
    private Spinner my_event_details_min_level;
    private EditText my_event_details_date;
    private EditText my_event_details_hour;
    private EditText my_event_details_location;
    private EditText my_event_details_description;


    // Participation buttons
    private Button my_event_details_button_participate;
    private boolean participate;

    // Participants number and list
    private TextView my_event_details_nb_participants;
    private ListView my_event_details_participant_list;

    private ActionBar mActionBar = null;

    // Current user
    private User currentUser;
    private Calendar my_event_details_cal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_details);

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
        this.my_event_details_creator = (TextView) findViewById(R.id.my_event_details_creator);
        this.my_event_details_min_level = (Spinner) findViewById(R.id.my_event_details_min_level);
        this.my_event_details_date = (EditText) findViewById(R.id.my_event_details_date);
        this.my_event_details_hour = (EditText) findViewById(R.id.my_event_details_hour);
        this.my_event_details_location = (EditText) findViewById(R.id.my_event_details_location);
        this.my_event_details_description = (EditText) findViewById(R.id.event_details_description);
        this.my_event_details_nb_participants = (TextView) findViewById(R.id.my_event_details_nb_participants);
        this.my_event_details_participant_list = (ListView) findViewById(R.id.my_event_details_participant_list);

        // Setting onLcickListener
        this.my_event_details_date.setOnClickListener(this);
        this.my_event_details_hour.setOnClickListener(this);
    }

    public void settingViews() {

        // Creator name
        this.my_event_details_creator.setText(this.myEvent.getCreator().getFirstname() + " " + this.myEvent.getCreator().getLastname());
        // Event description
        this.my_event_details_description.setText(this.myEvent.getDescription());

        // Defining min level
        String minLevel; //the value you want the position for
        // Getting minLevel
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
        ArrayAdapter myAdap = (ArrayAdapter) this.my_event_details_min_level.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(minLevel);
        //set the default according to value
        this.my_event_details_min_level.setSelection(spinnerPosition);

        // Turning Time to Datetime
        this.my_event_details_date.setText(TimeHelper.getEventDateStr(this.myEvent.getDate(), true));
        this.my_event_details_hour.setText(TimeHelper.getEventHourStr(this.myEvent.getDate()));

        // Turning location to address
        String address = GeolocHelper.getCityFromLatitudeLongitude(this, this.myEvent.getLatitude(), this.myEvent.getLongitude());
        this.my_event_details_location.setText(address);

        // Counting participants and filling listView
        this.my_event_details_button_participate = (Button) findViewById(R.id.my_event_details_button_participate);
        int pSize = 0;
        if (this.myEvent.getParticipants() != null){
            pSize = this.myEvent.getParticipants().size();
        }
        this.my_event_details_nb_participants.setText( pSize + " Participant(s)");
        initParticipantsList();

        // Setting participation to false, true if user is organizer
        UserManager userManager = UserManager.getInstance();
        this.currentUser = userManager.getUser();
        if (currentUser.getUID().equals(this.myEvent.getCreator().getUID()) || this.isParticipate()) {
            this.participate = true;
            this.my_event_details_button_participate.setText(getString(R.string.event_detail_button_desistate));
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
            this.my_event_details_participant_list.setAdapter(adapter);
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
            this.my_event_details_button_participate.setText(getString(R.string.event_detail_button_participate));
        } else {
            this.participate = true;
            this.my_event_details_button_participate.setText(getString(R.string.event_detail_button_desistate));
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        if(this.my_event_details_cal == null){
            this.my_event_details_cal = Calendar.getInstance();
        }

        my_event_details_cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        my_event_details_cal.set(Calendar.MONTH, monthOfYear);
        my_event_details_cal.set(Calendar.YEAR, year);

        // this.new_event_date = selectedCal.getTime();

        this.my_event_details_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if(this.my_event_details_cal == null){
            this.my_event_details_cal = Calendar.getInstance();
        }

        my_event_details_cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        my_event_details_cal.set(Calendar.MINUTE, minute);
        //this.new_event_time = selectedCal.getTime();

        this.my_event_details_hour.setText(TimeHelper.getEventHourStr(my_event_details_cal.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_event_details_date:
                if(this.my_event_details_cal == null){
                    this.my_event_details_cal = Calendar.getInstance(TimeZone.getDefault());
                }
                DatePickerDialog datePicker = new DatePickerDialog(this, this,
                        this.my_event_details_cal.get(Calendar.YEAR),
                        this.my_event_details_cal.get(Calendar.MONTH),
                        this.my_event_details_cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle(getResources().getString(R.string.select_event_date));
                datePicker.show();
                break;

            case R.id.my_event_details_hour:
                if(this.my_event_details_cal == null){
                    this.my_event_details_cal = Calendar.getInstance(TimeZone.getDefault());
                }
                TimePickerDialog timePicker = new TimePickerDialog(this, this,
                        this.my_event_details_cal.get(Calendar.HOUR_OF_DAY),
                        this.my_event_details_cal.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(this));
                timePicker.setCancelable(false);
                timePicker.setTitle(getResources().getString(R.string.select_event_hour));
                timePicker.show();
                break;

            case R.id.new_event_description:
                Toast.makeText(getApplicationContext(), "Description", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
