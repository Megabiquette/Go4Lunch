package com.albanfontaine.go4lunch.Utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.albanfontaine.go4lunch.Controllers.RestaurantCardActivity;
import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    String mRestaurantDetails = "";
    String mRestaurantName = "";
    String mCoworkersJoining = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    String formattedDateNow = Utils.getFormattedDate(new Date());
                    if(task.getResult().get("restaurantChosen") != null && formattedDateNow.equals(Utils.getFormattedDate(user.getDateChosen()))){
                        mRestaurantName = task.getResult().get("restaurantChosen").toString();
                        RestaurantHelper.getRestaurantsCollection().document(mRestaurantName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    mRestaurantDetails = task.getResult().get("mRestaurantDetailsGSON").toString();
                                }
                            }
                        });
                        RestaurantHelper.getWhoJoinedRestaurant(mRestaurantName).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        User workmate = document.toObject(User.class);
                                        String formattedDateChosen = Utils.getFormattedDate(workmate.getDateChosen());
                                        if(formattedDateChosen.equals(formattedDateNow) && !workmate.getUid().equals(user.getUid())){
                                            mCoworkersJoining += workmate.getUsername();
                                            mCoworkersJoining += " ";
                                        }
                                    }
                                    mCoworkersJoining.trim();
                                    sendNotification(context);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void sendNotification(Context context){
        Intent intent = new Intent(context, RestaurantCardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.RESTAURANT, mRestaurantDetails);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = context.getResources().getString(R.string.notification_today) + " " + mRestaurantName + ".";
        String message2 = !mCoworkersJoining.isEmpty() ? context.getResources().getString(R.string.notification_coworkers) + " " + mCoworkersJoining + "." : context.getResources().getString(R.string.notification_alone);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.InboxStyle().addLine(message).addLine(message2))
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(0, builder.build());
    }
}
