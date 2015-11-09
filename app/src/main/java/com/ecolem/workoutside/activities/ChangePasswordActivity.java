package com.ecolem.workoutside.activities;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.helpers.UserHelper;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.User;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ChangePasswordActivity extends ActionBarActivity implements UserManager.UserListener {

    private EditText change_password_email;
    private ProgressBar mProgressbar;
    private Button mResetPasswordButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initActionBar();

        this.change_password_email = (EditText) findViewById(R.id.change_password_email);
        this.mProgressbar = (ProgressBar) findViewById(R.id.change_password_progressbar);
        this.mResetPasswordButton = (Button) findViewById(R.id.change_password_button);
        this.email = getIntent().getExtras().getString("email", "");
        this.change_password_email.setText(email);
    }

    private void initActionBar() {

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.change_password_title));
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
        actionbar.setDisplayHomeAsUpEnabled(true);
    }


    public void clickResetPassword(View view) {
        mProgressbar.setVisibility(View.VISIBLE);
        mResetPasswordButton.setVisibility(View.GONE);
        if (this.email.length() == 0) {
            if (this.change_password_email.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.change_password_email_not_put), Toast.LENGTH_SHORT).show();
                return;
            } else {
                this.email = this.change_password_email.getText().toString();
            }
        }
        UserManager.getInstance().startResetPassword(email, this);
    }


    @Override
    public void onResetPasswordSuccess() {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.change_password_success), Toast.LENGTH_LONG).show();
        finish();
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public void onFail(FirebaseError error) {
        mProgressbar.setVisibility(View.GONE);
        mResetPasswordButton.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.change_password_error), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onLoginSuccess(String uid) {

    }

    @Override
    public void onGetUserSuccess(User user) {

    }

    @Override
    public void onAccountSuccess(String email, String password) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
