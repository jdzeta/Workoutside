package com.ecolem.workoutside.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.model.Movement;

import java.util.ArrayList;

/**
 * Created by akawa_000 on 31/10/2015.
 */
public class MovementListAdapter extends ArrayAdapter<Movement> {

    public MovementListAdapter(Context context, ArrayList<Movement> movements) {
        super(context, 0, movements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movement movement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mouvement_list_row, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.mouvement_title);

        tvName.setText(movement.getNom());

        return convertView;
    }
}
