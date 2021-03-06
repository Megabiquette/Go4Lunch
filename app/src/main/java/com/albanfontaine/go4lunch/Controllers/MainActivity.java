package com.albanfontaine.go4lunch.Controllers;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.main_activity_button_login_email) Button mEmailButton;
    @BindView(R.id.main_activity_button_login_facebook) Button mFacebookButton;
    @BindView(R.id.main_activity_button_login_google) Button mGoogleButton;
    @BindView(R.id.main_activity_button_login_twitter) Button mTwitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mEmailButton.setOnClickListener(this);
        mFacebookButton.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        mTwitterButton.setOnClickListener(this);

        this.createNotificationChannel();
        this.getLocationPermission();

        // If user is already authenticated, go to the base activity
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            launchBaseActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_activity_button_login_email:
                startSignInActivityWithEmail();
                break;
            case R.id.main_activity_button_login_facebook:
                startSignInActivityWithFacebook();
                break;
            case R.id.main_activity_button_login_google:
                startSignInActivityWithGoogle();
                break;
            case R.id.main_activity_button_login_twitter:
                startSignInActivityWithTwitter();
                break;
        }
    }

    private void startSignInActivityWithEmail(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(),
                Constants.RC_SIGN_IN);
    }

    private void startSignInActivityWithFacebook(){
        startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                            Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build()))
                    .setIsSmartLockEnabled(false)
                    .build(),
                Constants.RC_SIGN_IN);
    }

    private void startSignInActivityWithGoogle(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(),
                Constants.RC_SIGN_IN);
    }

    private void startSignInActivityWithTwitter(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(),
                Constants.RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if(requestCode == Constants.RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, getResources().getString(R.string.authentication_success), Toast.LENGTH_SHORT).show();
                launchBaseActivity();
            }else {
                // Error
                if(response == null){
                    Toast.makeText(this, getResources().getString(R.string.authentication_canceled), Toast.LENGTH_LONG).show();
                }else if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                    Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }else if(response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                    Toast.makeText(this, getResources().getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void launchBaseActivity(){
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
    }

    private void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void createNotificationChannel() {
        // Create the Notification Channel for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Lunch notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription("Sends notifications at noon to remember the restaurant to take lunch at");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }
}
