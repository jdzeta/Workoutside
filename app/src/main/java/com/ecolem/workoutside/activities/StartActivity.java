package com.ecolem.workoutside.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.adapter.StartPageAdapter;
import com.ecolem.workoutside.fragments.AccountStep1Fragment;
import com.ecolem.workoutside.fragments.AccountStep2Fragment;
import com.ecolem.workoutside.fragments.LoginFragment;
import com.ecolem.workoutside.model.User;
import com.ecolem.workoutside.views.StartViewPager;
import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends FragmentActivity implements Firebase.ResultHandler {

    private StartViewPager mViewPager = null;
    private StartPageAdapter mPageAdapter = null;

    private int mCurrentIndex = 0;
    private User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FacebookSdk.sdkInitialize(getApplicationContext());

        mPageAdapter = new StartPageAdapter(getSupportFragmentManager(), getFragments());

        mViewPager = (StartViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPageAdapter);
    }

    public User getUser() {
        if (mUser == null) {
            mUser = new User();
        }
        return this.mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public void nextStep() {
        mCurrentIndex++;
        mViewPager.setCurrentItem(mCurrentIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mViewPager.setCurrentItem(mCurrentIndex);
    }

    private List<Fragment> getFragments() {

        List<Fragment> fragments = new ArrayList<Fragment>();

        fragments.add(new LoginFragment());
        fragments.add(new AccountStep1Fragment());
        fragments.add(new AccountStep2Fragment());

        return fragments;

    }

    @Override
    public void onSuccess() {
        Toast.makeText(getApplicationContext(), "Un lien pour réinitialiser votre mot de passe vous a été envoyé sur votre boîte mail.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FirebaseError firebaseError) {
        Log.d("Reset password", "Failing reseting password from StartActivity");
    }
}
