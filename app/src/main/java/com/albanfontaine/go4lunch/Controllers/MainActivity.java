package com.albanfontaine.go4lunch.Controllers;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startSignInActivity();
    }

    // Sign in with Facebook and Google
    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                            Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
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

            }else {
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
