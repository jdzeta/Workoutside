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

    public static Address getAddressFromLocation(Context context, long longitude, long latitude) {
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

    public static String getCityFromLatitudeLongitude(Context context, double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); // 1 to 5
            Address addr = addresses.get(0);

            if (addr != null) {
                return addr.getAddressLine(0) + ", " + addr.getPostalCode() + " " + addr.getLocality();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
