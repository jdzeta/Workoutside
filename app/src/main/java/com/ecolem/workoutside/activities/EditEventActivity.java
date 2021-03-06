package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.helpers.EventHelper;
import com.ecolem.workoutside.helpers.GeolocHelper;
import com.ecolem.workoutside.helpers.TimeHelper;
import com.ecolem.workoutside.manager.EventManager;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EditEventActivity extends ActionBarActivity implements FirebaseManager.AuthenticationListener, DatePickerDialog.OnDateSetListener, EventManager.EventListener, AdapterView.OnItemSelectedListener, View.OnClickListener, GeoQueryEventListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private Event mEvent = null;

    private EditText event_name = null;
    private EditText event_description = null;
    private EditText event_max_participants = null;
    private EditText event_date_editText = null;
    private EditText event_time_start_editText = null;
    private EditText event_time_end_editText = null;
    private LinearLayout event_date_button = null;
    private LinearLayout event_time_start_button = null;
    private LinearLayout event_time_end_button = null;
    private Button event_submit_Button = null;

    private GoogleMap mMap;
    private Geocoder geocoder;
    private SupportMapFragment mMapFragment = null;
    private LocationManager mLocationManager;
    private boolean mFirstMoveCamera = true;
    private Marker mMarker;
    private List<Address> addresses;

    private Spinner event_location_spinner = null;
    private Spinner event_min_level = null;

    private Calendar event_cal_start = null;
    private Calendar event_cal_end = null;

    private ActionBar mActionBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Setting activity title
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

        // Setting date field
        this.event_date_editText = (EditText) findViewById(R.id.event_date);
        this.event_date_button = (LinearLayout) findViewById(R.id.event_date_button);
        this.event_date_button.setOnClickListener(this);

        this.event_time_start_button = (LinearLayout) findViewById(R.id.event_time_start_button);
        this.event_time_start_button.setOnClickListener(this);

        this.event_time_end_button = (LinearLayout) findViewById(R.id.event_time_end_button);
        this.event_time_end_button.setOnClickListener(this);

        this.event_time_start_editText = (EditText) findViewById(R.id.event_time_start);
        this.event_time_end_editText = (EditText) findViewById(R.id.event_time_end);

        this.event_submit_Button = (Button) findViewById(R.id.event_submit);
        this.event_submit_Button.setText(getResources().getString(R.string.event_update));
        this.event_submit_Button.setOnClickListener(this);

        // Map settings
        geocoder = new Geocoder(getApplicationContext(), Locale.FRANCE);
        mMapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.event_map);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initMap();

        // Spinner setting
        this.event_location_spinner = (Spinner) findViewById(R.id.event_location_spinner);
        this.event_min_level = (Spinner) findViewById(R.id.event_min_level);

        //Setting remaining fields
        this.event_name = (EditText) findViewById(R.id.event_name);

        this.event_description = (EditText) findViewById(R.id.event_description);
        this.event_max_participants = (EditText) findViewById(R.id.event_max_participants);

        initMap();


        this.mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }


    private void initMap() {

        this.mMap = mMapFragment.getMap();
        this.mMap.setMyLocationEnabled(true);
        this.mMap.setOnCameraChangeListener(this);
        // Setting onMapClickListeners
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_date_button:
                if (this.event_cal_start == null) {
                    this.event_cal_start = Calendar.getInstance(TimeZone.getDefault());
                }
                DatePickerDialog datePicker = new DatePickerDialog(this, this,
                        this.event_cal_start.get(Calendar.YEAR),
                        this.event_cal_start.get(Calendar.MONTH),
                        this.event_cal_start.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMinDate(new Date().getTime());
                datePicker.setCancelable(false);
                datePicker.setTitle(getResources().getString(R.string.select_event_date));
                datePicker.show();
                break;

            case R.id.event_time_start_button:
                if (this.event_cal_start == null) {
                    this.event_cal_start = Calendar.getInstance(TimeZone.getDefault());
                }
                TimePickerDialog timePicker1 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (EditEventActivity.this.event_cal_start == null) {
                            EditEventActivity.this.event_cal_start = Calendar.getInstance();
                        }

                        event_cal_start.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        event_cal_start.set(Calendar.MINUTE, minute);
                        //this.new_event_time = selectedCal.getTime();

                        EditEventActivity.this.event_time_start_editText.setText(TimeHelper.getEventHourStr(event_cal_start.getTime()));
                    }
                },
                        this.event_cal_start.get(Calendar.HOUR_OF_DAY),
                        this.event_cal_start.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(this));
                timePicker1.setCancelable(false);
                timePicker1.setTitle(getResources().getString(R.string.select_event_hour));
                timePicker1.show();
                break;

            case R.id.event_time_end_button:
                if (this.event_cal_end == null) {
                    this.event_cal_end = Calendar.getInstance(TimeZone.getDefault());
                }
                TimePickerDialog timePicker2 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (EditEventActivity.this.event_cal_end == null) {
                            EditEventActivity.this.event_cal_end = Calendar.getInstance();
                        }

                        event_cal_end.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        event_cal_end.set(Calendar.MINUTE, minute);
                        //this.new_event_time = selectedCal.getTime();

                        EditEventActivity.this.event_time_end_editText.setText(TimeHelper.getEventHourStr(event_cal_end.getTime()));
                    }
                },
                        this.event_cal_end.get(Calendar.HOUR_OF_DAY),
                        this.event_cal_end.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(this));
                timePicker2.setCancelable(false);
                timePicker2.setTitle(getResources().getString(R.string.select_event_hour));
                timePicker2.show();
                break;
            case R.id.event_submit:
                updateEvent();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseManager.getInstance().register(this);

        // Getting selected event
        Bundle bundle = getIntent().getExtras();
        String evUuid = bundle.getString("eventUUID");
        EventManager eventManager = EventManager.getInstance();
        eventManager.getEvent(evUuid, this);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        cleanMap();
    }

    // Remove marker
    private void cleanMap() {
        if (this.mMarker != null) {
            this.mMarker.remove();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        if (this.event_cal_start == null) {
            this.event_cal_start = Calendar.getInstance();
        }

        if (this.event_cal_end == null) {
            this.event_cal_end = Calendar.getInstance();
        }

        event_cal_start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        event_cal_start.set(Calendar.MONTH, monthOfYear);
        event_cal_start.set(Calendar.YEAR, year);

        event_cal_end.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        event_cal_end.set(Calendar.MONTH, monthOfYear);
        event_cal_end.set(Calendar.YEAR, year);

        // this.new_event_date = selectedCal.getTime();

        this.event_date_editText.setText(TimeHelper.getEventShortDateStr(event_cal_start.getTime()));

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        // Add a new marker to the map
        this.mMarker = this.mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
    }

    @Override
    public void onKeyExited(String key) {
        // Remove any old marker
        if (mMarker != null) {
            mMarker.remove();
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        // Move the marker
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(FirebaseError error) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        addMarker(latLng.latitude, latLng.longitude);
    }

    private void addMarker(double latitude, double longitude) {
        //Toast.makeText(getApplicationContext(), "Tapped point=" + latLng, Toast.LENGTH_SHORT).show();
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cleanMap();
        this.mMarker = this.mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));

        /*GroupListAdapter adapter = new GroupListAdapter(this, groups);

        ListView listView = (ListView) findViewById(R.id.groupListView);
        listView.setAdapter(adapter);*/

        // Setting addresses list
        ArrayAdapter<String> addressListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        for (Address address : this.addresses) {
            // Don't include addresses with null value
            if (address.getAddressLine(0) != null && address.getAddressLine(1) != null && address.getAddressLine(2) != null) {
                String fullAddress = address.getAddressLine(0) + " " + address.getAddressLine(1) + " " + address.getAddressLine(2);
                addressListAdapter.add(fullAddress);
            }
        }

        // Setting addresses in spinner
        event_location_spinner.setAdapter(addressListAdapter);

        //System.out.println("Address list = " + addressList);
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        //Toast.makeText(getApplicationContext(), "Long tapped point=" + latLng, Toast.LENGTH_SHORT).show();
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(addresses);
    }

    public void updateEvent() {
        if (this.event_name != null &&
                this.event_cal_start != null &&
                this.event_cal_end != null &&
                this.mMarker != null &&
                this.event_description != null) {
            // Name
            String name = this.event_name.getText().toString();
            // Location
            LatLng latLng = this.mMarker.getPosition();
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;
            // Description
            String description = this.event_description.getText().toString();

            // Min level
            Integer minLevel = this.event_min_level.getSelectedItemPosition();

            // Max users
            Integer maxParticipants = -1;
            if (this.event_max_participants.getText().toString() != null && !this.event_max_participants.getText().toString().isEmpty()) {
                maxParticipants = Integer.parseInt(this.event_max_participants.getText().toString());
            }

            // Finishing setting date
            //date.setTime(time.getTime());
            Date startDate = this.event_cal_start.getTime();
            Date endDate = this.event_cal_end.getTime();

            // Preparing event
            EventManager eventManager = new EventManager();
            UserManager userManager = UserManager.getInstance();
            // Creator
            User creator = userManager.getUser();

            // Event
            Event event = new Event(this.mEvent.getUID(), name, startDate, endDate, latitude, longitude, description, minLevel, maxParticipants, creator);

            // Sending to Firebase
            eventManager.saveEvent(this, event);
            Toast.makeText(this, getResources().getString(R.string.event_update_success), Toast.LENGTH_LONG).show();

            goBack();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.missing_fields_warning), Toast.LENGTH_SHORT).show();
        }
    }


    public void settingViews() {

        // Event description
        this.event_name.setText(this.mEvent.getName());

        // Event description
        this.event_description.setText(this.mEvent.getDescription());

        // Defining min level
        String minLevel = EventHelper.getLevelStr(this, this.mEvent.getMinLevel());

        ArrayAdapter myAdap = (ArrayAdapter) this.event_min_level.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(minLevel);
        //set the default according to value
        this.event_min_level.setSelection(spinnerPosition);

        // Turning Time to Datetime
        this.event_cal_start = Calendar.getInstance();
        this.event_cal_start.setTime(this.mEvent.getDateStart());
        this.event_cal_end = Calendar.getInstance();
        this.event_cal_end.setTime(this.mEvent.getDateEnd());

        this.event_date_editText.setText(TimeHelper.getEventShortDateStr(this.mEvent.getDateStart()));
        this.event_time_start_editText.setText(TimeHelper.getEventHourStr(this.mEvent.getDateStart()));
        this.event_time_end_editText.setText(TimeHelper.getEventHourStr(this.mEvent.getDateEnd()));

        this.event_max_participants.setText(this.mEvent.getMaxParticipants() + "");


        // Turning location to address
        LatLng latlng = new LatLng(this.mEvent.getLatitude(), this.mEvent.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 15);

        if (this.mMap != null) {
            this.mMap.animateCamera(cameraUpdate);
        }

        addMarker(this.mEvent.getLatitude(), this.mEvent.getLongitude());
    }


    @Override
    public void onGetEventSuccess(Event event) {
        if (event != null) {
            this.mEvent = event;
            mActionBar.setTitle(this.mEvent.getName());

            settingViews();
        }
    }

    @Override
    public void onGetEventsSuccess(ArrayList<Event> events) {

    }

    @Override
    public void onFail(FirebaseError error) {

    }


    /* MENU */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (this.mEvent != null) {
                goBack();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goBack() {
        if (this.mEvent != null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("eventUUID", this.mEvent.getUID());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @Override
    public void onUserIsLogged(boolean isLogged) {
        if (!isLogged) {
            Toast.makeText(this, "Vous êtes déconnecté", Toast.LENGTH_LONG).show();
            Intent newIntent = new Intent(EditEventActivity.this, StartActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        }
    }
}
