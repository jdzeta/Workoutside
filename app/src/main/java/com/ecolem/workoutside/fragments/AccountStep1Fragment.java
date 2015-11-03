package com.ecolem.workoutside.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.activities.StartActivity;
import com.ecolem.workoutside.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by snabou on 02/11/2015.
 */
public class AccountStep1Fragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private EditText mFirstnameEditText = null;
    private EditText mLastnameEditText = null;
    private EditText mBirthdateEditText = null;
    private EditText mCityEditText = null;
    private RelativeLayout mDateButton = null;
    private Button mNextButton = null;
    private Spinner mGenderSpinner = null;

    private Date mBirthDate;
    private int mGender = 0;

    private StartActivity mParentActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_step1, container, false);

        mFirstnameEditText = (EditText) view.findViewById(R.id.account_firstname);
        mLastnameEditText = (EditText) view.findViewById(R.id.account_lastname);
        mBirthdateEditText = (EditText) view.findViewById(R.id.account_birthdate);
        mBirthdateEditText.setOnClickListener(this);
        mCityEditText = (EditText) view.findViewById(R.id.account_city);
        mDateButton = (RelativeLayout) view.findViewById(R.id.account_birthdate_button);
        mDateButton.setOnClickListener(this);
        mGenderSpinner = (Spinner) view.findViewById(R.id.account_gender_spinner);
        mGenderSpinner.setOnItemSelectedListener(this);
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
    public void onStart() {
        super.onStart();
        initSpinner();
    }

    private void initSpinner() {
        List<String> genders = new ArrayList<>();
        genders.add(getActivity().getResources().getString(R.string.account_gender_m));
        genders.add(getActivity().getResources().getString(R.string.account_gender_f));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genders);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_birthdate_button:
            case R.id.account_birthdate:

                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle(getActivity().getResources().getString(R.string.select_birthdate));
                datePicker.show();

                break;
            case R.id.account_next_button:
                if (mParentActivity != null) {
                    User user = mParentActivity.getUser();
                    user.setFirstname(mFirstnameEditText.getText().toString());
                    user.setLastname(mLastnameEditText.getText().toString());
                    user.setBirthdate(mBirthDate);
                    user.setGender(mGender);
                    user.setCity(mCityEditText.getText().toString());

                    mParentActivity.setUser(user);
                    mParentActivity.nextStep();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Date picker callback
     **/

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(year, monthOfYear, dayOfMonth);
        mBirthDate = selectedCal.getTime();

        mBirthdateEditText.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
    }

    /**
     * Gender sner callbacks
     **/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mGender = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
