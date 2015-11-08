package com.ecolem.workoutside.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.helpers.TimeHelper;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.Event;
import com.ecolem.workoutside.model.User;

import java.util.ArrayList;
import java.util.Map;

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
        if (position > 0) {
            Event previousEvent = getItem(position - 1);
            if (previousEvent != null && event.hasSameDate(previousEvent)) {
                showDate = false;
            }
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow_event, parent, false);
        }

        TextView evDate = (TextView) convertView.findViewById(R.id.event_date);
        TextView evName = (TextView) convertView.findViewById(R.id.event_name);
        //TextView evCity = (TextView) convertView.findViewById(R.id.event_city);
        TextView evHour = (TextView) convertView.findViewById(R.id.event_hour);
        TextView evDesc = (TextView) convertView.findViewById(R.id.event_description);
        TextView evNbParticipants = (TextView) convertView.findViewById(R.id.event_nb_participants);

        ImageView adminIcon = (ImageView) convertView.findViewById(R.id.admin_icon);
        LinearLayout participation = (LinearLayout) convertView.findViewById(R.id.participation);

        participation.setVisibility(View.GONE);

        // Date
        evDate.setText(TimeHelper.getEventDateStr(event.getDateStart(), false));
        evDate.setVisibility(showDate ? View.VISIBLE : View.GONE);

        // Hour
        evHour.setText(TimeHelper.getEventHourStr(event.getDateStart()));

        // Name
        evName.setText(event.getName());

        // City
        // too lazy : evCity.setText(GeolocHelper.getCityFromLatitudeLongitude(getContext(), event.getLatitude(), event.getLongitude()));


        // Description
        evDesc.setText(event.getDescription());

        // Showing admin_icon if current user is creator
        User currentUser = UserManager.getInstance().getUser();
        if (currentUser.getUID().equals(event.getCreator().getUID())){
            adminIcon.setVisibility(View.VISIBLE);
        }
        else {
            adminIcon.setVisibility(View.GONE);
        }

        // Showing if user is participating in event
        for (Map.Entry<String, User> entry : event.getParticipants().entrySet()){
            if (entry.getKey().equals(currentUser.getUID())){
                participation.setVisibility(View.VISIBLE);
                break;
            }
        }

        evNbParticipants.setText((event.getParticipants() != null ? event.getParticipants().size() : 0) + "");


        return convertView;
    }

}
