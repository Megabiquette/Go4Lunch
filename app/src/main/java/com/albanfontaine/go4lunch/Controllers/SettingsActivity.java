package com.albanfontaine.go4lunch.Controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.AlarmReceiver;
import com.albanfontaine.go4lunch.Utils.Constants;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.notification_switch) Switch mSwitch;

    SharedPreferences mSharedPreferences;
    ArrayList<Restaurant> mRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mRestaurants = new ArrayList<>();
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        mSwitch.setOnCheckedChangeListener(this);
        this.restorePreferences();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.notification_switch){
            if(isChecked){
                setAlarm();
            }else{
                disableAlarm();
            }
        }
    }

    private void setAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void disableAlarm(){
        Intent intent = new Intent(getApplicationContext()  , AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onStop() {
        // Save the notification criteria
        savePreferences();
        super.onStop();
    }

    private void savePreferences(){
        mSharedPreferences.edit().putBoolean(Constants.SWITCH, mSwitch.isChecked()).apply();
    }

    private void restorePreferences(){
        mSwitch.setChecked(mSharedPreferences.getBoolean(Constants.SWITCH, false));
    }
}
