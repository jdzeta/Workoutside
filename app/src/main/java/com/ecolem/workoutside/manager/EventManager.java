package com.ecolem.workoutside.manager;

import com.ecolem.workoutside.comparators.EventDateComparator;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

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
        if (event.getParticipants() == null) {
            FirebaseManager.getInstance().getFirebaseRef().child("events").child(event.getUID()).child("participants").child(user.getUID()).setValue(user);
        } else {
            FirebaseManager.getInstance().getFirebaseRef().child("events").child(event.getUID()).child("participants").push().setValue(user.getUID(), user);
        }
    }

    public void removeParticipant(Event event, User user) {
        FirebaseManager.getInstance().getFirebaseRef().child("events").child(event.getUID()).child("participants").child(user.getUID()).setValue(null);
    }

    public void deleteEvent(String uuid) {
        FirebaseManager.getInstance().getFirebaseRef().child("events").child(uuid).setValue(null);
    }

    public interface EventListener {
        public void onGetEventSuccess(Event m);

        public void onGetEventsSuccess(ArrayList<Event> events);

        public void onFail(FirebaseError error);
    }


}
