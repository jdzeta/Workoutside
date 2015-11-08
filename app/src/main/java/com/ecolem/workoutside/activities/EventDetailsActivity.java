package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.adapter.UserListAdapter;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.helpers.EventHelper;
import com.ecolem.workoutside.helpers.GeolocHelper;
import com.ecolem.workoutside.helpers.TimeHelper;
import com.ecolem.workoutside.helpers.UserHelper;
import com.ecolem.workoutside.manager.EventManager;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventDetailsActivity extends ActionBarActivity implements FirebaseManager.AuthenticationListener, EventManager.EventListener, View.OnClickListener {

    // Temporary event for testing
    private Event myEvent;

    // Event details displayed
    private TextView event_detail_creator;
    private TextView event_detail_min_level;
    private TextView event_detail_date;
    private TextView event_detail_time_start;
    private TextView event_detail_time_end;
    private TextView event_detail_location;
    private TextView event_detail_description;
    private ImageView event_detail_map;

    private String eventUUID = null;

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
        this.eventUUID = bundle.getString("eventUUID");
        EventManager eventManager = EventManager.getInstance();
        eventManager.getEvent(this.eventUUID, this);

        // INIT EVENT DETAILS
        this.event_detail_creator = (TextView) findViewById(R.id.event_detail_creator);
        this.event_detail_min_level = (TextView) findViewById(R.id.event_detail_min_level);
        this.event_detail_date = (TextView) findViewById(R.id.event_detail_date);
        this.event_detail_time_start = (TextView) findViewById(R.id.event_detail_time_start);
        this.event_detail_time_end = (TextView) findViewById(R.id.event_detail_time_end);
        this.event_detail_location = (TextView) findViewById(R.id.event_detail_location);
        this.event_detail_location.setOnClickListener(this);
        this.event_detail_description = (TextView) findViewById(R.id.event_details_description);
        this.event_detail_button_participate = (Button) findViewById(R.id.event_detail_button_participate);
        this.event_detail_nb_participants = (TextView) findViewById(R.id.event_detail_nb_participants);
        this.event_detail_participant_list = (ListView) findViewById(R.id.event_detail_participant_list);
        this.event_detail_map = (ImageView) findViewById(R.id.map_icon);
        this.event_detail_map.setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseManager.getInstance().register(this);
    }

    private void openGoogleMap() {
        String uri = "http://maps.google.co.in/maps?q=" + event_detail_location.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    public void settingViews() {

        // Creator name
        this.event_detail_creator.setText(this.myEvent.getCreator().getFirstname() + " " + this.myEvent.getCreator().getLastname());

        // Event description
        String description = this.myEvent.getDescription().isEmpty() ? getResources().getString(R.string.event_detail_no_description) : this.myEvent.getDescription();
        this.event_detail_description.setText(description);

        // Defining min level
        event_detail_min_level.setText(EventHelper.getLevelStr(this, this.myEvent.getMinLevel()));


        // Turning Time to Datetime
        this.event_detail_date.setText(TimeHelper.getEventDateStr(this.myEvent.getDateStart(), true));
        this.event_detail_time_start.setText(TimeHelper.getEventHourStr(this.myEvent.getDateStart()));
        this.event_detail_time_end.setText(TimeHelper.getEventHourStr(this.myEvent.getDateEnd()));


        // Turning location to address
        String address = GeolocHelper.getCityFromLatitudeLongitude(this, this.myEvent.getLatitude(), this.myEvent.getLongitude());
        this.event_detail_location.setText(address);

        // Setting current user
        UserManager userManager = UserManager.getInstance();
        this.currentUser = userManager.getUser();

        // Counting participants and filling listView
        int pSize = 0;
        if (this.myEvent.getParticipants() != null) {
            pSize = this.myEvent.getParticipants().size();

            // Disable participate button if maxParticipant and user is not in participant list
            if (this.myEvent.getParticipants().size() == this.myEvent.getMaxParticipants() && !isParticipate()) {

                this.event_detail_button_participate.setEnabled(false);
                this.event_detail_button_participate.setText(getString(R.string.event_detail_button_full));
            } else {
                // Setting participation to false, true if user is organizer or participant
                if (currentUser.getUID().equals(this.myEvent.getCreator().getUID()) || !isParticipate()) {
                    this.participate = true;
                    this.event_detail_button_participate.setEnabled(true);
                    this.event_detail_button_participate.setText(getString(R.string.event_detail_button_desistate));
                } else {
                    this.participate = false;
                }
            }
        }
        this.event_detail_nb_participants.setText(pSize + " Participant(s)");
        initParticipantsList();

    }

    public boolean isParticipate() {
        HashMap<String, User> participants = this.myEvent.getParticipants();
        if (participants != null) {
            // Setting participants listView
            // Setting participants in Arraylist
            for (Map.Entry<String, User> entry : participants.entrySet()) {
                if (entry.getKey().equals(this.currentUser.getUID())) {
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
        if (mActionBar != null && this.myEvent != null) {
            mActionBar.setTitle(this.myEvent.getName());
            settingViews();
        }
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
    public void participate() {
        EventManager eventManager = EventManager.getInstance();

        if (this.participate) {
            // Updating event
            eventManager.pushParticipant(this.myEvent, this.currentUser);
        } else {
            eventManager.removeParticipant(this.myEvent, this.currentUser);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_icon:
            case R.id.event_detail_location:
                openGoogleMap();
                break;
            default:
                break;
        }
    }



     /* MENU */

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editActionItem = menu.findItem(R.id.action_edit_event);

        MenuItem deleteActionItem = menu.findItem(R.id.action_delete_event);
        MenuItem shareActionItem = menu.findItem(R.id.action_share_event);

        editActionItem.setVisible(UserHelper.currentUserIsCreator(myEvent));
        deleteActionItem.setVisible(UserHelper.currentUserIsCreator(myEvent));

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_event) {
            Bundle bundle = new Bundle();
            bundle.putString("eventUUID", this.eventUUID);
            Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete_event) {
            showDeleteAlert();
            return true;
        }
        if (id == R.id.action_share_event) {
            //TODO
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showDeleteAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.delete));

        alertDialogBuilder
                .setMessage(getResources().getString(R.string.delete_event_alert))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.action_delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (myEvent != null) {
                            EventManager.getInstance().deleteEvent(EventDetailsActivity.this, myEvent);
                            Toast.makeText(EventDetailsActivity.this, getResources().getString(R.string.delete_event_success), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                this.eventUUID = data.getStringExtra("eventUUID");
                EventManager eventManager = EventManager.getInstance();
                eventManager.getEvent(this.eventUUID, this);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public void onUserIsLogged(boolean isLogged) {
        if (!isLogged) {
            Toast.makeText(this, "Vous êtes déconnecté", Toast.LENGTH_LONG).show();
            Intent newIntent = new Intent(EventDetailsActivity.this, StartActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        }
    }
}
