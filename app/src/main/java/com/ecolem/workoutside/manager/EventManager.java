package com.ecolem.workoutside.manager;

import com.ecolem.workoutside.comparators.EventDateComparator;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.Movement;
import com.ecolem.workoutside.model.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.internal.e;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class EventManager {

    public static EventManager sInstance = null;
    public EventListener mListener;

    public static EventManager getInstance() {
        if (sInstance == null) {
            sInstance = new EventManager();
        }

        return sInstance;
    }

    public void setListener(EventListener listener) {
        this.mListener = listener;
    }

    public void sendData(Event Event) {
        FirebaseManager.getInstance().getFirebaseRef().setValue(Event);
    }

    public void getEvent(String eventName) {

        Firebase movRef = FirebaseManager.getInstance().getFirebaseRef().child("events").child(eventName);

        movRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " évènement(s)");
                Event event = snapshot.getValue(Event.class);
                if (mListener != null) {
                    mListener.onGetEventSuccess(event);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if (mListener != null) {
                    mListener.onFail(firebaseError);
                }
            }
        });
    }

    public void startGetEventsComing(final EventListener listener) {
        Date today = new Date();
        Firebase eventsRef = FirebaseManager.getInstance().getFirebaseRef().child("events");
       /* Query query = eventsRef.startAt(today.getTime());

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("Found " + dataSnapshot.getChildrenCount() + " events");

                ArrayList<Event> events = new ArrayList<Event>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    events.add(postSnapshot.getValue(Event.class));
                }

                Collections.sort(events, new EventDateComparator());

                if (listener != null) {
                    listener.onGetEventsSuccess(events);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/

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

    public interface EventListener {
        public void onGetEventSuccess(Event m);

        public void onGetEventsSuccess(ArrayList<Event> events);

        public void onFail(FirebaseError error);
    }


}
