package com.ecolem.workoutside.manager;

import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Event;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class EventManager {

    public static EventManager sInstance = null;
    public EventListener mListener;

    public static EventManager getInstance(){
        if(sInstance == null){
            sInstance = new EventManager();
        }

        return sInstance;
    }

    public void setListener(EventListener listener){
        this.mListener = listener;
    }

    public void sendData(Event Event){
        FirebaseManager.getInstance().getFirebaseRef().setValue(Event);
    }

    public void getEvent(String eventName){

        Firebase movRef = FirebaseManager.getInstance().getFirebaseRef().child("events").child(eventName);

        movRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " évènement(s)");
                Event event = snapshot.getValue(Event.class);
                if(mListener != null){
                    mListener.onSuccess(event);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                if(mListener != null){
                    mListener.onFail(firebaseError);
                }
            }
        });
    }

    public interface EventListener {
        public void onSuccess(Event m);
        public void onFail(FirebaseError error);
    }
}
