package com.ecolem.workoutside.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by snabou on 03/11/2015.
 */
public class GeolocHelper {

    public Address getAddressFromLocation(Context context, long longitude, long latitude) {
        Address address;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // 1 to 5
            return addresses.get(0);
            /*String address = addresses.get(0).getAddressLine(0); // getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
