package com.albanfontaine.go4lunch.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.RestaurantHelper;
import com.albanfontaine.go4lunch.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fragment_list_item_name) TextView mName;
    @BindView(R.id.fragment_list_item_address) TextView mAddress;
    @BindView(R.id.fragment_list_item_opening_hours) TextView mOpeningHours;
    @BindView(R.id.fragment_list_item_distance) TextView mDistance;
    @BindView(R.id.fragment_list_item_people_count) TextView mPeopleCount;
    @BindView(R.id.fragment_list_item_people_icon) ImageView mPeopleIcon;
    @BindView(R.id.fragment_list_item_rating1) ImageView mRating1;
    @BindView(R.id.fragment_list_item_rating2) ImageView mRating2;
    @BindView(R.id.fragment_list_item_rating3) ImageView mRating3;
    @BindView(R.id.fragment_list_item_rating4) ImageView mRating4;
    @BindView(R.id.fragment_list_item_rating5) ImageView mRating5;
    @BindView(R.id.fragment_list_item_photo) ImageView mPhoto;
    int mPeopleJoining = 0;

    public RestaurantViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithRestaurant(Restaurant restaurant, Context context){
        this.mName.setText(restaurant.getName());
        this.mAddress.setText(restaurant.getAddress());
        this.mOpeningHours.setText(restaurant.getOpeningHours());
        this.mDistance.setText(context.getResources().getString(R.string.restaurant_distance, restaurant.getDistance()));

        // How many people are going to this restaurant today
        RestaurantHelper.getWhoJoinedRestaurant(restaurant.getName()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Date dateNow = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDateNow = dateFormat.format(dateNow);
                    for (QueryDocumentSnapshot document : task.getResult()){
                        User workmate = document.toObject(User.class);
                        Date dateChosen = workmate.getDateChosen();
                        String formattedDateChosen = dateFormat.format(dateChosen);
                        if(formattedDateChosen.equals(formattedDateNow)){
                            mPeopleJoining++;
                        }
                    }
                    if(mPeopleJoining != 0){
                        mPeopleIcon.setVisibility(View.VISIBLE);
                        mPeopleCount.setText(context.getResources().getString(R.string.restaurant_people_count, mPeopleJoining));
                    }
                }else{
                    Log.e("Request error", task.getException().getMessage());
                }
            }
        });

        // Restaurant photo
        if(restaurant.getPhotoRef() != null){
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=" + restaurant.getPhotoRef() + "&key=" + Constants.API_KEY;
            Picasso.with(context).load(photoUrl).fit().centerCrop().into(mPhoto);
        }

        // Restaurant rating
        Utils.showRatingStars(restaurant, mRating1, mRating2, mRating3);
        if(restaurant.getRating() >= 1){
            mRating1.setVisibility(View.VISIBLE);
        }
        if(restaurant.getRating() >= 2){
            mRating2.setVisibility(View.VISIBLE);
        }
        if(restaurant.getRating() >= 3){
            mRating3.setVisibility(View.VISIBLE);
        }

        // Restaurant opening hours
        StringBuilder sb = new StringBuilder();
        if(restaurant.getOpeningHours() == null || restaurant.getClosingHours() == null){
            sb.append(context.getResources().getString(R.string.restaurant_hours_no_info));
        } else {
            if(restaurant.isOpenNow()){
                sb.append(context.getResources().getString(R.string.restaurant_hours_open));
                if(restaurant.getOpeningHours().equals("0000")){
                    sb.append("24/7");
                } else {
                    sb.append(context.getResources().getString(R.string.restaurant_hours_until));
                    sb.append(Utils.formatHours(restaurant.getClosingHours()));
                }
            } else {
                sb.append(context.getResources().getString(R.string.restaurant_hours_closed));
            }
        }
        mOpeningHours.setText(sb.toString());
    }
}
