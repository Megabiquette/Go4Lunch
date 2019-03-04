package com.albanfontaine.go4lunch.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fragment_list_item_address) TextView mAddress;
    @BindView(R.id.fragment_list_item_opening_hours) TextView mHours;
    @BindView(R.id.fragment_list_item_distance) TextView mDistance;
    @BindView(R.id.fragment_list_item_people_count) TextView mPeopleCount;
    @BindView(R.id.fragment_list_item_people_icon) ImageView mPeopleIcon;
    @BindView(R.id.fragment_list_item_rating) ImageView mRating;
    @BindView(R.id.fragment_list_item_photo) ImageView mPhoto;


    public RestaurantViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithRestaurant(Restaurant restaurant){
        this.mAddress.setText(restaurant.getAddress());
        this.mHours.setText(restaurant.getHours());
        this.mDistance.setText(restaurant.getDistance());
    }
}
