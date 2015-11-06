package com.ecolem.workoutside.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.model.User;

import java.util.ArrayList;

/**
 * Created by akawa_000 on 31/10/2015.
 */
public class UserListAdapter extends ArrayAdapter<User> {

    public UserListAdapter(Context context, ArrayList<User> Users) {
        super(context, 0, Users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow_user, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.user_name);
        tvName.setText(user.getFirstname() + " " + user.getLastname());

        return convertView;
    }

}
