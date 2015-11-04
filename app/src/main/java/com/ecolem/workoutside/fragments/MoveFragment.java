package com.ecolem.workoutside.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.model.Movement;
import com.ecolem.workoutside.tools.ImageLoadTask;

/**
 * Created by snabou on 04/11/2015.
 */
public class MoveFragment extends Fragment {

    private TextView mNom;
    private TextView mDescription;
    private ImageView mImage;

    private String mMoveName;

    private Movement mMove;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_move, container, false);


        mNom = (TextView) rootView.findViewById(R.id.movement_nom);
        mDescription = (TextView) rootView.findViewById(R.id.movement_description);
        mImage = (ImageView) rootView.findViewById(R.id.movement_image);

        return rootView;
    }


    public void setMove(Movement m) {
        this.mMove = m;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        mNom.setText(mMove.getNom());
        mDescription.setText(mMove.getDescription());

        new ImageLoadTask(mMove.getImage(), mImage).execute();
    }


}
