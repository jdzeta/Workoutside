package com.ecolem.workoutside.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.manager.UserManager;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, GeoQueryEventListener, GoogleMap.OnCameraChangeListener, LocationListener {

    //private static final GeoLocation INITIAL_CENTER = new GeoLocation(37.7789, -122.4017);
    private static final int INITIAL_ZOOM_LEVEL = 14;


    private RelativeLayout mAgendaMenuButton;
    private RelativeLayout mCatalogMenuButton;
    private RelativeLayout mLogoutMenuButton;

    private SupportMapFragment mMapFragment = null;

    private GoogleMap mMap;
    private GeoFire mGeoFire;
    private Circle mSearchCircle;
    private GeoQuery mGeoQuery;
    private Map<String, Marker> mMarkers;

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.action_bar_title));
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

        mMapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);

        mAgendaMenuButton = (RelativeLayout) findViewById(R.id.menu_events);
        mAgendaMenuButton.setOnClickListener(this);
        mCatalogMenuButton = (RelativeLayout) findViewById(R.id.menu_catalog);
        mCatalogMenuButton.setOnClickListener(this);
        mLogoutMenuButton = (RelativeLayout) findViewById(R.id.menu_logout);
        mLogoutMenuButton.setOnClickListener(this);


        initMap();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

    @Override
    protected void onStart() {
        super.onStart();

        //initMap();
    }


    private void initMap() {

        this.mMap = mMapFragment.getMap();
        this.mMap.setMyLocationEnabled(true);
        // LatLng latLngCenter = new LatLng(INITIAL_CENTER.latitude, INITIAL_CENTER.longitude);
        // this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
        this.mMap.setOnCameraChangeListener(this);
        this.mGeoFire = new GeoFire(new Firebase(WorkoutSide.FIREBASE_GEOFIRE_URL));
        // this.mGeoQuery = this.mGeoFire.queryAtLocation(INITIAL_CENTER, 1);
        //this.mGeoQuery.addGeoQueryEventListener(this);
        //this.mSearchCircle = this.mMap.addCircle(new CircleOptions().center(latLngCenter).radius(1000));
        //this.mSearchCircle.setFillColor(Color.argb(66, 255, 0, 255));
        //this.mSearchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
        this.mMarkers = new HashMap<String, Marker>();
        // this.mMap.addMarker(new MarkerOptions().position(new LatLng(INITIAL_CENTER.latitude, INITIAL_CENTER.longitude)).title("My Home").snippet("Home Address"));


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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.menu_catalog:
                intent = new Intent(MainActivity.this, CatalogActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_events:
                intent = new Intent(getApplication(), AgendaActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_logout:
                showLogoutAlert();
                break;
            default:
                break;
        }
    }

    private void showLogoutAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(getResources().getString(R.string.menu_logout));

        // set dialog message
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.logout_alert))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.leave), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UserManager.getInstance().logout();
                        MainActivity.this.finish();
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


    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed / DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000 / Math.pow(2, zoomLevel);
    }


    /**
     * Map callbacks
     **/

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        Marker marker = this.mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
        this.mMarkers.put(key, marker);
    }

    @Override
    public void onKeyExited(String key) {
        Marker marker = this.mMarkers.get(key);
        if (marker != null) {
            marker.remove();
            this.mMarkers.remove(key);
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        Marker marker = this.mMarkers.get(key);
        if (marker != null) {
            this.animateMarkerTo(marker, location.latitude, location.longitude);
        }
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(FirebaseError error) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        //this.mSearchCircle.setCenter(center);
        //this.mSearchCircle.setRadius(radius);
        //this.mGeoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        //this.mGeoQuery.setRadius(radius / 1000);
    }

    /**
     * Location listener
     **/

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
}
