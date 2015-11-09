package com.ecolem.workoutside.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.helpers.UserHelper;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ChangePasswordActivity extends ActionBarActivity implements Firebase.ResultHandler{

    private EditText change_password_email;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        this.change_password_email = (EditText) findViewById(R.id.change_password_email);
        setTitle(getString(R.string.change_password_title));

        // Checking if user has put its email before
        this.email = getIntent().getExtras().getString("email", "");
    }

    public void clickResetPassword(View view){
        if (this.email.length() == 0){
            if (this.change_password_email.getText().length() == 0){
                Toast.makeText(getApplicationContext(), "Veuillez saisir une adresse email svp", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                this.email = this.change_password_email.getText().toString();
            }
        }
        UserHelper.changePassword(this.email, this);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getApplicationContext(), "Un mail permettant de réinitialiser votre mot de passe vous a été envoyé", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(FirebaseError firebaseError) {
        Toast.makeText(getApplicationContext(), "Une erreur est survenue, avez-vous saisi une adresse mail valide ?", Toast.LENGTH_SHORT).show();
        Log.d("Error chage password", firebaseError.getMessage());
    }
}
