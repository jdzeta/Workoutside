package com.ecolem.workoutside.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.activities.StartActivity;
import com.ecolem.workoutside.model.User;

import java.util.Date;

/**
 * Created by snabou on 02/11/2015.
 */
public class AccountStep1Fragment extends Fragment implements View.OnClickListener {

    private EditText mFirstnameEditText = null;
    private EditText mLastnameEditText = null;
    private EditText mBirthdateEditText = null;
    private EditText mCityEditText = null;
    private RelativeLayout mDateButton = null;
    private Button mNextButton = null;

    private Date mBirthDate;

    private StartActivity mParentActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_step1, container, false);

        mFirstnameEditText = (EditText) view.findViewById(R.id.account_firstname);
        mLastnameEditText = (EditText) view.findViewById(R.id.account_lastname);
        mBirthdateEditText = (EditText) view.findViewById(R.id.account_birthdate);
        mCityEditText = (EditText) view.findViewById(R.id.account_city);
        mDateButton = (RelativeLayout) view.findViewById(R.id.account_birthdate_button);
        mDateButton.setOnClickListener(this);
        mNextButton = (Button) view.findViewById(R.id.account_next_button);
        mNextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof StartActivity) {
            this.mParentActivity = (StartActivity) activity;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_birthdate_button:
                //TODO open datePicker
                break;
            case R.id.account_next_button:
                if (mParentActivity != null) {
                    User user = mParentActivity.getUser();
                    user.setFirstname(mFirstnameEditText.getText().toString());
                    user.setLastname(mLastnameEditText.getText().toString());
                    user.setBirthdate(mBirthDate);
                    user.setCity(mCityEditText.getText().toString());

                    mParentActivity.setUser(user);
                    mParentActivity.nextStep();
                }
                break;
            default:
                break;
        }
    }
}
