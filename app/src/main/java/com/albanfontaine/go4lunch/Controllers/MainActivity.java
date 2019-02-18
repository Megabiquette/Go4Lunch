package com.albanfontaine.go4lunch.Controllers;

import android.content.Intent;
import android.support.annotation.Nullable;
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

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.main_activity_button_login_facebook) Button mFacebookButton;
    @BindView(R.id.main_activity_button_login_google) Button mGoogleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFacebookButton.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.main_activity_button_login_facebook:
                startSignInActivityWithFacebook();
                break;
            case R.id.main_activity_button_login_google:
                startSignInActivityWithGoogle();
                break;
        }
    }

    private void startSignInActivityWithFacebook(){
        startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                            Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build()))
                    .setIsSmartLockEnabled(false, true)
                    .build(),
                Constants.RC_SIGN_IN);
    }

    private void startSignInActivityWithGoogle(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
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
                // Start central activity
                Intent intent = new Intent(this, BaseActivity.class);
                startActivity(intent);
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
}
