package com.ecolem.workoutside.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.ecolem.workoutside.comparators.EventDateComparator;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.helpers.EventHelper;
import com.ecolem.workoutside.helpers.GeolocHelper;
import com.ecolem.workoutside.helpers.TimeHelper;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class EventManager {

    public static EventManager sInstance = null;

    public static EventManager getInstance() {
        if (sInstance == null) {
            sInstance = new EventManager();
        }

        return sInstance;
    }

    public void sendData(Event event) {
        FirebaseManager.getInstance().getFirebaseRef().child("events").child(event.getUID()).setValue(event);
    }

    public void getEvent(String uuid, final EventListener eventListener) {

        Firebase movRef = FirebaseManager.getInstance().getFirebaseRef().child("events").child(uuid);

        movRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " évènement(s)");
                Event event = snapshot.getValue(Event.class);
                if (eventListener != null) {
                    eventListener.onGetEventSuccess(event);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if (eventListener != null) {
                    eventListener.onFail(firebaseError);
                }
            }
        });
    }


    public void startGetEventsComing(final EventListener listener) {
        Firebase eventsRef = FirebaseManager.getInstance().getFirebaseRef().child("events");

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " events");

                ArrayList<Event> events = new ArrayList<Event>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    events.add(event);
                }

                Collections.sort(events, new EventDateComparator());

                if (listener != null) {
                    listener.onGetEventsSuccess(events);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if (listener != null) {
                    listener.onFail(firebaseError);
                }
            }
        });
    }

    public void pushParticipant(Event event, User user) {

        if (event != null && user != null) {
            FirebaseManager.getInstance().getFirebaseRef().child("events").child(event.getUID()).child("participants").child(user.getUID()).setValue(user);
        }
    }

    public void removeParticipant(Event event, User user) {
        FirebaseManager.getInstance().getFirebaseRef().child("events").child(event.getUID()).child("participants").child(user.getUID()).setValue(null);
    }

    public void deleteEvent(Context context, Event event) {
        if (event != null) {
            String subject = "Annulation de l'événement : " + event.getName();
            String text = "L'événement prévu le " + TimeHelper.getEventDateStr(event.getDateStart(), false) + " a été annulé.";

            sendEmailToParticipants(context, event, subject, text);
            FirebaseManager.getInstance().getFirebaseRef().child("events").child(event.getUID()).setValue(null);
        }
    }

    public void saveEvent(Context context, Event event) {
        FirebaseManager.getInstance().getFirebaseRef().child("events").child(event.getUID()).setValue(event);
        String subject = "Mise à jour de l'événement " + event.getName();
        String text = "Nouvelles informations concernant l'événement : \n" +
                "Date de l'événement : " + TimeHelper.getEventDateStr(event.getDateStart(), true) +
                "Date de début : " + TimeHelper.getEventHourStr(event.getDateStart()) + "\n" +
                "Date de fin : " + TimeHelper.getEventHourStr(event.getDateEnd()) + "\n" +
                "Description : " + event.getDescription() + "\n" +
                "Niveau minimum : " + EventHelper.getLevelStr(context, event.getMinLevel()) + "\n" +
                "Nombre de participants maximum : " + event.getMaxParticipants() + "\n" +
                "Lieu : " + GeolocHelper.getCityFromLatitudeLongitude(context, event.getLatitude(), event.getLongitude());
        sendEmailToParticipants(context, event, subject, text);
    }

    public void sendEmailToParticipants(Context context, Event event, String subject, String text) {

        if (event.getParticipants() != null) {
            for (Map.Entry<String, User> entry : event.getParticipants().entrySet()) {
                sendEmail(context, entry.getValue().getEmail(), subject, text);
            }
        }
    }

    public void sendEmail(Context context, String email, String subject, String text) {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, "no-reply@workout-side.com");
        i.setData(Uri.parse("mailto:" + email));

        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, text);
        try {
            context.startActivity(Intent.createChooser(i, "Envoi du mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            // Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public interface EventListener {
        public void onGetEventSuccess(Event m);

        public void onGetEventsSuccess(ArrayList<Event> events);

        public void onFail(FirebaseError error);
    }


}
