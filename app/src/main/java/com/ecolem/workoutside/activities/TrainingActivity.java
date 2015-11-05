package com.ecolem.workoutside.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.fragments.MoveFragment;
import com.ecolem.workoutside.manager.TrainingManager;
import com.ecolem.workoutside.model.Movement;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class TrainingActivity extends ActionBarActivity implements TrainingManager.MoveListener {

    private ViewPager mViewPager = null;
    private PagerAdapter mPagerAdapter;

    private ArrayList<Movement> mMovesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.menu_training));
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
        actionbar.setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        TrainingManager.getInstance().startGetMoves(this);
    }


    // REQUEST CALLBACKS
    @Override
    public void onGetMoveSuccess(Movement m) {

    }

    @Override
    public void onGetMovesSuccess(ArrayList<Movement> moves) {
        this.mMovesList = moves;

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), moves);
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onFail(FirebaseError error) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Movement> moves = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<Movement> moves) {
            super(fm);
            this.moves = moves;
        }

        @Override
        public Fragment getItem(int position) {
            MoveFragment frag = new MoveFragment();
            frag.setMove(moves.get(position));
            return frag;
        }

        @Override
        public int getCount() {
            return moves.size();
        }
    }


}
