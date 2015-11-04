package com.ecolem.workoutside.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class NewEventActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, View.OnClickListener, GeoQueryEventListener, GoogleMap.OnCameraChangeListener, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private EditText newEventName = null;
    private EditText new_event_description = null;
    private EditText new_event_max_participants = null;

    private LinearLayout new_event_calendar = null;
    private EditText new_event_date_editText = null;

    private TextView new_event_location = null;

    private GoogleMap mMap;
    private GeoFire mGeoFire;
    private SupportMapFragment mMapFragment = null;
    private LocationManager mLocationManager;
    private Map<String, Marker> mMarkers;

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
        new_event_date_editText = (EditText) findViewById(R.id.new_event_date);
        new_event_calendar = (LinearLayout) findViewById(R.id.new_event_calendar);
        new_event_calendar.setOnClickListener(this);

        // Map settings
        mMapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.new_event_map);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        new_event_location = (TextView) findViewById(R.id.new_event_location);
        initMap();

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
        this.mMarkers = new HashMap<String, Marker>();
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

    private void cleanMap() {
        //this.mGeoQuery.removeAllListeners();
        for (Marker marker : this.mMarkers.values()) {
            marker.remove();
        }
        this.mMarkers.clear();

        if (this.mMap != null) {
            //this.getFragmentManager().beginTransaction().remove(this.getFragmentManager().findFragmentById(R.id.map)).commit();
            this.mMap = null;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(year, monthOfYear, dayOfMonth);
        new_event_date = selectedCal.getTime();

        new_event_date_editText.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

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

    }

    @Override
    public void onKeyExited(String key) {

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

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

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }
}
