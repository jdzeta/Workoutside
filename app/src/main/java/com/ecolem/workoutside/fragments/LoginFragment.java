package com.ecolem.workoutside.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.activities.ChangePasswordActivity;
import com.ecolem.workoutside.activities.HomeActivity;
import com.ecolem.workoutside.activities.StartActivity;
import com.ecolem.workoutside.manager.SharedPreferenceManager;
import com.ecolem.workoutside.manager.UserManager;
import com.ecolem.workoutside.model.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.FirebaseError;

/**
 * Created by snabou on 02/11/2015.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, UserManager.UserListener {

    private final int STEP = 0;

    private TextView mTabLogin = null;
    private TextView mTabRegister = null;
    private EditText mEmailEditText = null;
    private EditText mPasswordEditText = null;
    private LinearLayout mLoginButtonsPanel = null;
    private LinearLayout mAccountButtonsPanel = null;
    private Button mLoginButton = null;
    private LoginButton mFBLoginButton = null;
    private Button mCreateAccountButton = null;
    private Button mChangePassword = null;
    private ProgressBar mProgressBar = null;

    private StartActivity mParentActivity = null;
    private CallbackManager mCallbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        mSelectedTab = Tab.LOGIN;

        mTabLogin = (TextView) view.findViewById(R.id.tab_login);
        mTabLogin.setOnClickListener(this);
        mTabRegister = (TextView) view.findViewById(R.id.tab_register);
        mTabRegister.setOnClickListener(this);
        mEmailEditText = (EditText) view.findViewById(R.id.email);
        mPasswordEditText = (EditText) view.findViewById(R.id.password);
        mLoginButton = (Button) view.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
        mCreateAccountButton = (Button) view.findViewById(R.id.create_button);
        mCreateAccountButton.setOnClickListener(this);
        mChangePassword = (Button) view.findViewById(R.id.reset_password_button);
        mChangePassword.setOnClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mLoginButtonsPanel = (LinearLayout) view.findViewById(R.id.login_buttons_panel);
        mAccountButtonsPanel = (LinearLayout) view.findViewById(R.id.account_buttons_panel);


        mFBLoginButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        mFBLoginButton.setFragment(this);
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("sandra", "logged in");
            }

            @Override
            public void onCancel() {
                Log.i("sandra", "logged in");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("sandra", "error : " + e.getMessage());

            }
        });


        return view;
    }


    private Toast mToast = null;

    private enum Tab {
        LOGIN, REGISTER
    }

    private Tab mSelectedTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof StartActivity) {
            mParentActivity = (StartActivity) activity;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateDisplay();
    }

    private void updateDisplay() {

        mProgressBar.setVisibility(View.GONE);
        switch (mSelectedTab) {
            case LOGIN:
                mTabRegister.setTextColor(WorkoutSide.APP_RESOURCES.getColor(R.color.gray));
                mTabLogin.setTextColor(WorkoutSide.APP_RESOURCES.getColor(R.color.primary));
                mTabLogin.setTypeface(null, Typeface.BOLD);
                mTabRegister.setTypeface(null, Typeface.NORMAL);
                mLoginButtonsPanel.setVisibility(View.VISIBLE);
                mAccountButtonsPanel.setVisibility(View.GONE);
                break;
            case REGISTER:
                mTabLogin.setTextColor(WorkoutSide.APP_RESOURCES.getColor(R.color.gray));
                mTabRegister.setTextColor(WorkoutSide.APP_RESOURCES.getColor(R.color.primary));
                mTabLogin.setTypeface(null, Typeface.NORMAL);
                mTabRegister.setTypeface(null, Typeface.BOLD);
                mLoginButtonsPanel.setVisibility(View.GONE);
                mAccountButtonsPanel.setVisibility(View.VISIBLE);

                break;
            default:
                break;

        }

        mEmailEditText.setText(SharedPreferenceManager.getInstance(mParentActivity).getSavedEmail());
        mPasswordEditText.setText(SharedPreferenceManager.getInstance(mParentActivity).getSavedPassword());
    }

    private void showToast(String msg) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        mToast.show();
    }

    private void login() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLoginButtonsPanel.setVisibility(View.GONE);

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        SharedPreferenceManager.getInstance(mParentActivity).saveLogin(email, password);
        UserManager.getInstance().login(email, password, this);
    }

    private void createAccount() {
        mProgressBar.setVisibility(View.VISIBLE);
        mAccountButtonsPanel.setVisibility(View.GONE);

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        SharedPreferenceManager.getInstance(mParentActivity).saveLogin(email, password);
        UserManager.getInstance().createAccount(email, password, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                login();
                break;
            case R.id.create_button:
                createAccount();
                break;
            case R.id.tab_login:
                mSelectedTab = Tab.LOGIN;
                updateDisplay();
                break;
            case R.id.tab_register:
                mSelectedTab = Tab.REGISTER;
                updateDisplay();
                break;
            case R.id.reset_password_button:
                onClickChangePassword();
                break;
            default:
                break;
        }
    }


    /**
     * USER CALLBACKS
     */

    @Override
    public void onLoginSuccess(String uid) {
        if(isAdded() && isVisible()) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        }
    }

    @Override
    public void onGetUserSuccess(User user) {

    }

    @Override
    public void onAccountSuccess(String email, String password) {
        if (mParentActivity != null) {
            User user = mParentActivity.getUser();
            user.setEmail(email);
            user.setPassword(password);
            mParentActivity.setUser(user);

            mParentActivity.nextStep();
        }
    }

    @Override
    public void onResetPasswordSuccess() {

    }

    @Override
    public void onFail(FirebaseError error) {
        String msg = "";
        switch (error.getCode()) {
            case FirebaseError.INVALID_EMAIL:
                msg = getResources().getString(R.string.invalid_email);
                break;
            case FirebaseError.INVALID_PASSWORD:
                msg = getResources().getString(R.string.invalid_password);
                break;
            case FirebaseError.EMAIL_TAKEN:
                msg = getResources().getString(R.string.email_already_used);
                break;
            case FirebaseError.NETWORK_ERROR:
                msg = getResources().getString(R.string.network_error);
                break;
            case FirebaseError.USER_DOES_NOT_EXIST:
                msg = getResources().getString(R.string.user_does_not_exists);
                break;
            default:
                msg = error.getCode() + "";
                break;
        }
        showToast(msg);
        updateDisplay();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickChangePassword() {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        EditText email = (EditText) getView().findViewById(R.id.email);
        if (email.getText() != null) {
            intent.putExtra("email", email.getText().toString());
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

}
