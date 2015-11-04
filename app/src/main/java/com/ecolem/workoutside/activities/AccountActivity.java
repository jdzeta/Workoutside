package com.ecolem.workoutside.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * !!M
 * todo: handle saveInstanceState
 */
public class AccountActivity extends AppCompatActivity {

    private User mUserCopy;

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private RadioGroup mGenderGroup;
    private EditText mBirthdayEditText;
    private DateFormat mDateFmt;

    private EditText mEmailEditText;
    private EditText mCityEditText;

    // To keep reference of dialog showed, to hide them if we quit (or it leaks)
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mFirstNameEditText = (EditText) findViewById(R.id.edit_first_name);
        mLastNameEditText = (EditText) findViewById(R.id.edit_last_name);
        mBirthdayEditText = (EditText) findViewById(R.id.edit_birthdate);
        mGenderGroup = (RadioGroup) findViewById(R.id.group_gender);

        mEmailEditText = (EditText) findViewById(R.id.edit_email);
        mCityEditText = (EditText) findViewById(R.id.edit_city);

        findViewById(R.id.btn_discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discardChanges();
            }
        });

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        mDateFmt = new SimpleDateFormat("dd/MM/yyyy");
        mUserCopy = new User();
        User user = UserManager.getInstance().getUser();
        if (user == null) {
            throw new RuntimeException("Well done");
        }


        final Calendar pickerBirthday = Calendar.getInstance();
        Date userBirthday = user.getBirthdate();
        if (userBirthday != null) {
            pickerBirthday.setTime(userBirthday);
        }
        mBirthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new DatePickerDialog(AccountActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(i, i1, i2);

                        Date newBirthday = new Date(newDate.getTimeInMillis());
                        mUserCopy.setBirthdate(newBirthday);
                        mBirthdayEditText.setText(
                                mDateFmt.format(newBirthday)
                        );

                        mDialog = null;
                    }
                }, pickerBirthday.get(Calendar.YEAR), pickerBirthday.get(Calendar.MONTH), pickerBirthday.get(Calendar.DAY_OF_WEEK));
                mDialog.show();
            }
        });

        populateFields(user);
    }

    @Override
    protected void onPause() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        super.onPause();
    }

    private void populateFields(User user) {
        mFirstNameEditText.setText(user.getFirstname());
        mLastNameEditText.setText(user.getLastname());
        mEmailEditText.setText(user.getEmail());
        mCityEditText.setText(user.getCity());

        mGenderGroup.check(
                user.getGender() == 1 ? R.id.female : R.id.male
        );


        Date dateToShow = user.getBirthdate();
        if (dateToShow == null) {
            dateToShow = new Date();
        }
        mBirthdayEditText.setText(
                mDateFmt.format(dateToShow)
        );

    }

    private void discardChanges() {
        // todo: confirm user if he want to quit/discard change if any
        onBackPressed();
    }

    private void updateProfile() {
        // todo: check if modification occured
        UserManager userManager = UserManager.getInstance();
        User user = userManager.getUser();
        if (user == null) {
            throw new RuntimeException("Well done, again");
        }

        mUserCopy.setUID(user.getUID());
        mUserCopy.setFirstname(mFirstNameEditText.getText().toString());
        mUserCopy.setLastname(mLastNameEditText.getText().toString());
        mUserCopy.setEmail(mEmailEditText.getText().toString());
        mUserCopy.setGender(
                mGenderGroup.getCheckedRadioButtonId() == R.id.male ? 0 : 1
        );

        mUserCopy.setCity(mCityEditText.getText().toString());

        // todo: chek for fail and do what need to be done
        userManager.saveUser(mUserCopy);
        userManager.setUser(mUserCopy);
    }
}
