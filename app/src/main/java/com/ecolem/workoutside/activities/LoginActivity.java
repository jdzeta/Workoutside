package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.User;
import com.facebook.FacebookSdk;
import com.firebase.client.FirebaseError;


public class LoginActivity extends Activity implements View.OnClickListener, UserManager.LoginListener {

    private EditText mEmailEditText = null;
    private EditText mPasswordEditText = null;
    private Button mLoginButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
    }


    private void login() {
        UserManager.getInstance().login(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                login();
                break;
            default:
                break;
        }
    }


    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFail(FirebaseError error) {
        String msg = "";
        switch (error.getCode()){
            case -15:
                msg = getResources().getString(R.string.invalid_email);
                break;
            case -16:
                msg = getResources().getString(R.string.invalid_password);
                break;
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
