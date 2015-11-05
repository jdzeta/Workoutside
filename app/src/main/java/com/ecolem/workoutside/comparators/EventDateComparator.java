package com.ecolem.workoutside.comparators;

import com.ecolem.workoutside.model.Event;

import java.util.Comparator;

/**
 * Created by snabou on 05/11/2015.
 */
public class EventDateComparator implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        if (e1.getDate().before(e2.getDate())) {
            return -1;
        } else if (e1.getDate().after(e2.getDate())) {
            return 1;
        } else {
            return 0;
        }
    }
}

