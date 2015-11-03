package com.ecolem.workoutside.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.model.Event;

import java.util.ArrayList;
import java.util.Date;

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

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow_event, parent, false);
        }

        TextView evName = (TextView) convertView.findViewById(R.id.event_name);
        TextView evCity= (TextView) convertView.findViewById(R.id.event_city);
        TextView evHour = (TextView) convertView.findViewById(R.id.event_hour);
        TextView evDesc = (TextView) convertView.findViewById(R.id.event_description);
        TextView evNbParticipants = (TextView) convertView.findViewById(R.id.event_nb_participants);

        // @TODO evName.setText(event.getName());
        // @TODO evCity.setText(event.getCity());

        Date eventDate = event.getDate();
        evHour.setText((int) eventDate.getTime());

        // @TODO evHour.setText(event.get());
        evDesc.setText(event.getDescription());
        evNbParticipants.setText(event.getLimiteP());

        return convertView;
    }
}
