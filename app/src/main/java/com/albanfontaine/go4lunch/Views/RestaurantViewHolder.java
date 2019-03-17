package com.albanfontaine.go4lunch.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.Utils;
import com.squareup.picasso.Picasso;

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

    public RestaurantViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithRestaurant(Restaurant restaurant, Context context){
        this.mName.setText(restaurant.getName());
        this.mAddress.setText(restaurant.getAddress());
        this.mOpeningHours.setText(restaurant.getOpeningHours());
        this.mDistance.setText(context.getResources().getString(R.string.restaurant_distance, restaurant.getDistance()));

        // How many people are going to this restaurant
        if(restaurant.getPeopleCount() == 0){
            mPeopleCount.setVisibility(View.GONE);
            mPeopleIcon.setVisibility(View.GONE);
        }else{
            mPeopleCount.setText(context.getResources().getString(R.string.restaurant_people_count, restaurant.getPeopleCount()));
        }

        // Restaurant photo
        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=" + restaurant.getPhotoRef() + "&key=" + Constants.API_KEY;
        Picasso.with(context).load(photoUrl).fit().centerCrop().into(mPhoto);

        // Restaurant rating
        Utils.showRatingStars(restaurant, mRating1, mRating2, mRating3, mRating4, mRating5);
        if(restaurant.getRating() >= 1){
            mRating1.setVisibility(View.VISIBLE);
        }
        if(restaurant.getRating() >= 2){
            mRating2.setVisibility(View.VISIBLE);
        }
        if(restaurant.getRating() >= 3){
            mRating3.setVisibility(View.VISIBLE);
        }
        if(restaurant.getRating() >= 4){
            mRating4.setVisibility(View.VISIBLE);
        }
        if(restaurant.getRating() >= 5){
            mRating5.setVisibility(View.VISIBLE);
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
