package com.ecolem.workoutside.activities;

import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.manager.EventManager;
import com.ecolem.workoutside.model.Event;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NewEventActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, View.OnClickListener, GeoQueryEventListener, GoogleMap.OnCameraChangeListener, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private EditText new_event_name = null;
    private EditText new_event_description = null;
    private EditText new_event_max_participants = null;

    private LinearLayout new_event_calendar = null;
    private EditText new_event_date_editText = null;

    private GoogleMap mMap;
    private Geocoder geocoder;
    private GeoFire mGeoFire;
    private SupportMapFragment mMapFragment = null;
    private LocationManager mLocationManager;
    private Marker mMarker;
    private List<Address> addresses;

    private Spinner new_event_location_spinner = null;
    private Spinner new_event_min_level = null;

    private Date new_event_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Setting activity title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.new_event_title));
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

        // Setting date field
        this.new_event_date_editText = (EditText) findViewById(R.id.new_event_date);
        this.new_event_calendar = (LinearLayout) findViewById(R.id.new_event_calendar);
        this.new_event_calendar.setOnClickListener(this);

        // Map settings
        geocoder = new Geocoder(getApplicationContext(), Locale.FRANCE);
        mMapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.new_event_map);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initMap();

        // Spinner setting
        this.new_event_location_spinner = (Spinner) findViewById(R.id.new_event_location_spinner);
        this.new_event_min_level = (Spinner) findViewById(R.id.new_event_min_level);

        //Setting remaining fields
        this.new_event_name = (EditText) findViewById(R.id.new_event_name);
        this.new_event_description = (EditText) findViewById(R.id.new_event_description);
        this.new_event_max_participants = (EditText) findViewById(R.id.new_event_max_participants);

        if (mLocationManager != null) {
            Criteria criteria = new Criteria();
            String provider = mLocationManager.getBestProvider(criteria, true);
            Location location = mLocationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    private void initMap() {

        this.mMap = mMapFragment.getMap();
        this.mMap.setMyLocationEnabled(true);
        this.mMap.setOnCameraChangeListener(this);
        //
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        this.mGeoFire = new GeoFire(new Firebase(WorkoutSide.FIREBASE_GEOFIRE_URL));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_event_calendar:
            case R.id.new_event_date:
            case R.id.new_event_date_button:

                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePicker = new DatePickerDialog(this, this,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle(getResources().getString(R.string.select_birthdate));
                datePicker.show();

                break;
            case R.id.new_event_description:
                Toast.makeText(getApplicationContext(), "Description", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(year, monthOfYear, dayOfMonth);
        this.new_event_date = selectedCal.getTime();

        this.new_event_date_editText.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("sandra", "location changed");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        this.mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(INITIAL_CENTER.latitude,INITIAL_CENTER.longitude), INITIAL_ZOOM_LEVEL));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
        //Toast.makeText(getApplicationContext(), "Tapped point=" + latLng, Toast.LENGTH_SHORT).show();
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cleanMap();
        this.mMarker = this.mMap.addMarker(new MarkerOptions().position(latLng));

        /*GroupListAdapter adapter = new GroupListAdapter(this, groups);

        ListView listView = (ListView) findViewById(R.id.groupListView);
        listView.setAdapter(adapter);*/

        ArrayAdapter<String> addressListAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item);

        for (Address address : this.addresses) {
            String fullAddress = address.getAddressLine(0) + " " + address.getAddressLine(1) + " " + address.getAddressLine(2);
            addressListAdapter.add(fullAddress);
        }

        new_event_location_spinner.setAdapter(addressListAdapter);

        //System.out.println("Address list = " + addressList);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(), "Long tapped point=" + latLng, Toast.LENGTH_SHORT).show();
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(addresses);
    }

    public void saveNewEvent(View view){
        String name = this.new_event_name.getText().toString();
        Date date = this.new_event_date;
        LatLng latLng = this.mMarker.getPosition();
        GeoLocation location = new GeoLocation(latLng.latitude, latLng.longitude);
        String description = this.new_event_description.getText().toString();

        Integer minLevel;
        String minLevelString = this.new_event_min_level.getSelectedItem().toString();
        switch(minLevelString){
            case "Débutant":
                minLevel = 0;
                break;
            case "Intermédiaire":
                minLevel = 1;
                break;
            case "Avancé":
                minLevel = 2;
                break;
            case "Expert":
                minLevel = 3;
                break;
            default:
                minLevel = 0;
                break;
        }

        Integer maxParticipants = Integer.parseInt(this.new_event_max_participants.getText().toString());

        if (checkFieldsBeforeSend(name, date, location, description, minLevel, maxParticipants) != true){
            Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs svp", Toast.LENGTH_SHORT).show();
        }
        else {
            Event event = new Event(name, date, location, description, minLevel, maxParticipants);

            EventManager eventManager = new EventManager();
            eventManager.sendData(event);

            Intent intent = new Intent(getApplicationContext(), EventsListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean checkFieldsBeforeSend(String name, Date date, GeoLocation location, String description, Integer minLevel, Integer maxParticipants) {
        if (name.equals(null) ||
                date.equals(null) ||
                location.equals(null) ||
                description.equals(null) ||
                minLevel.equals(null) ||
                maxParticipants.equals(null)) {
            return false;
        }
        return true;
    }
}
