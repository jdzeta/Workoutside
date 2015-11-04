package com.ecolem.workoutside.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.activities.HomeActivity;
import com.ecolem.workoutside.activities.StartActivity;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.User;
import com.firebase.client.FirebaseError;

/**
 * Created by snabou on 02/11/2015.
 */
public class AccountStep2Fragment extends Fragment implements View.OnClickListener, UserManager.UserListener {

    private RelativeLayout mLevel0Button = null;
    private RelativeLayout mLevel1Button = null;
    private RelativeLayout mLevel2Button = null;
    private RelativeLayout mLevel3Button = null;
    private ImageView mLevel0Check = null;
    private ImageView mLevel1Check = null;
    private ImageView mLevel2Check = null;
    private ImageView mLevel3Check = null;
    private Button mFinishButton = null;
    private ProgressBar mProgressBar = null;

    private StartActivity mParentActivity = null;
    private int mSelecteLevel = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_step2, container, false);

        mLevel0Check = (ImageView) v.findViewById(R.id.check_level_0);
        mLevel1Check = (ImageView) v.findViewById(R.id.check_level_1);
        mLevel2Check = (ImageView) v.findViewById(R.id.check_level_2);
        mLevel3Check = (ImageView) v.findViewById(R.id.check_level_3);

        mLevel0Button = (RelativeLayout) v.findViewById(R.id.account_level_0_button);
        mLevel0Button.setOnClickListener(this);
        mLevel1Button = (RelativeLayout) v.findViewById(R.id.account_level_1_button);
        mLevel1Button.setOnClickListener(this);
        mLevel2Button = (RelativeLayout) v.findViewById(R.id.account_level_2_button);
        mLevel2Button.setOnClickListener(this);
        mLevel3Button = (RelativeLayout) v.findViewById(R.id.account_level_3_button);
        mLevel3Button.setOnClickListener(this);

        mFinishButton = (Button) v.findViewById(R.id.account_finish_button);
        mFinishButton.setOnClickListener(this);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof StartActivity) {
            this.mParentActivity = (StartActivity) activity;
        }
    }

    private void unselectAll() {
        mLevel0Check.setVisibility(View.GONE);
        mLevel1Check.setVisibility(View.GONE);
        mLevel2Check.setVisibility(View.GONE);
        mLevel3Check.setVisibility(View.GONE);
    }

    private void login() {
        if (mParentActivity != null) {
            mFinishButton.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            User user = mParentActivity.getUser();
            UserManager.getInstance().login(user.getEmail(), user.getPassword(), this);
        }

    }

    private void createUserWithUID(String uid) {
        if (mParentActivity != null) {
            User user = mParentActivity.getUser();
            user.setUID(uid);
            user.setLevel(mSelecteLevel);

            UserManager.getInstance().saveUser(user);
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        unselectAll();
        switch (v.getId()) {
            case R.id.account_level_0_button:
                mSelecteLevel = 0;
                mLevel0Check.setVisibility(View.VISIBLE);
                break;
            case R.id.account_level_1_button:
                mSelecteLevel = 1;
                mLevel1Check.setVisibility(View.VISIBLE);
                break;
            case R.id.account_level_2_button:
                mSelecteLevel = 2;
                mLevel2Check.setVisibility(View.VISIBLE);
                break;
            case R.id.account_level_3_button:
                mSelecteLevel = 3;
                mLevel3Check.setVisibility(View.VISIBLE);
                break;
            case R.id.account_finish_button:
                login();
                break;
        }
    }

    @Override
    public void onLoginSuccess(String uid) {
        createUserWithUID(uid);
    }

    @Override
    public void onGetUserSuccess(User user) {

    }

    @Override
    public void onAccountSuccess(String email, String password) {

    }

    @Override
    public void onFail(FirebaseError error) {
        mProgressBar.setVisibility(View.GONE);
        mFinishButton.setVisibility(View.VISIBLE);
    }

}
