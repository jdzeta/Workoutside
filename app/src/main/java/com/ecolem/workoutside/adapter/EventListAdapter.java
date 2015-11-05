package com.ecolem.workoutside.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.helpers.TimeHelper;
import com.ecolem.workoutside.model.Event;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by akawa_000 on 31/10/2015.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event event = getItem(position);

        boolean showDate = true;
        if(position > 0){
            Event previousEvent = getItem(position - 1);
            if(previousEvent != null && event.hasSameDate(previousEvent)){
                showDate = false;
            }
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow_event, parent, false);
        }

        TextView evDate = (TextView) convertView.findViewById(R.id.event_date);
        TextView evName = (TextView) convertView.findViewById(R.id.event_name);
        TextView evCity = (TextView) convertView.findViewById(R.id.event_city);
        TextView evHour = (TextView) convertView.findViewById(R.id.event_hour);
        TextView evDesc = (TextView) convertView.findViewById(R.id.event_description);
        TextView evNbParticipants = (TextView) convertView.findViewById(R.id.event_nb_participants);

        // Date
        evDate.setText(TimeHelper.getEventDateStr(event.getDate()));
        evDate.setVisibility(showDate ? View.VISIBLE : View.GONE);

        // Hour
        evHour.setText(TimeHelper.getEventHourStr(event.getDate()));

        // Name
        evName.setText(event.getName());

        // City
        //@TODO evCity.setText(event.get());

        // Description
        evDesc.setText(event.getDescription());

        if (event.getParticipants() != null) {
            evNbParticipants.setText(event.getParticipants().size());
        }

        return convertView;
    }

}
