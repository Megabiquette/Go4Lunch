package com.albanfontaine.go4lunch.Controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.UserHelper;
import com.albanfontaine.go4lunch.Utils.Utils;
import com.albanfontaine.go4lunch.Views.RestaurantAdapter;
import com.albanfontaine.go4lunch.Views.WorkmateAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestaurantCardActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.restaurant_card_name) TextView mName;
    @BindView(R.id.restaurant_card_address) TextView mAddress;
    @BindView(R.id.restaurant_card_photo) ImageView mPhoto;
    @BindView(R.id.restaurant_card_rating1) ImageView mRating1;
    @BindView(R.id.restaurant_card_rating2) ImageView mRating2;
    @BindView(R.id.restaurant_card_rating3) ImageView mRating3;
    @BindView(R.id.restaurant_card_rating4) ImageView mRating4;
    @BindView(R.id.restaurant_card_rating5) ImageView mRating5;
    @BindView(R.id.restaurant_card_icon_call) ImageView mCallIcon;
    @BindView(R.id.restaurant_card_icon_like) ImageView mLikeIcon;
    @BindView(R.id.restaurant_card_icon_website) ImageView mWebsiteIcon;
    @BindView(R.id.restaurant_card_recycler_view) RecyclerView mRecyclerView;

    private Restaurant mRestaurant;
    private List<User> mWorkmates;
    private WorkmateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_card);
        ButterKnife.bind(this);

        mCallIcon.setOnClickListener(this);
        mLikeIcon.setOnClickListener(this);
        mWebsiteIcon.setOnClickListener(this);

        this.getRestaurant(savedInstanceState);
        this.getWorkmates();
        this.displayRestaurantInfos();
        this.configureRecyclerView();
    }

    private void getRestaurant(Bundle bundle){
        Gson gson = new Gson();
        Type restaurantType = new TypeToken<Restaurant>() { }.getType();
        String restaurant = getIntent().getStringExtra(Constants.RESTAURANT);
        mRestaurant = gson.fromJson(restaurant, restaurantType);
    }

    private void getWorkmates(){
        //TODO
    }

    private void displayRestaurantInfos(){
        mName.setText(mRestaurant.getName());
        mAddress.setText(mRestaurant.getAddress());

        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=700&photoreference=" + mRestaurant.getPhotoRef() + "&key=" + Constants.API_KEY;
        Picasso.with(this).load(photoUrl).fit().centerCrop().into(mPhoto);

        Utils.showRatingStars(mRestaurant, mRating1, mRating2, mRating3, mRating4, mRating5);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restaurant_card_icon_call:
                this.callRestaurant();
                break;

            case R.id.restaurant_card_icon_like:

                break;

            case R.id.restaurant_card_icon_website:
                if(mRestaurant.getWebsite() != null){
                    Intent webIntent = new Intent(this, WebViewActivity.class);
                    webIntent.putExtra(Constants.RESTAURANT_URL, mRestaurant.getWebsite());
                    startActivity(webIntent);
                }else {
                    Toast.makeText(this,getResources().getString(R.string.no_website), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void callRestaurant() {
        // Checks permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mRestaurant.getPhone()));
            startActivity(callIntent);
        }
    }

    private void configureRecyclerView(){
        // Configures the RecyclerView and its components
        this.mAdapter = new WorkmateAdapter(generateOptionsForAdapter(UserHelper.getUsersCollection()));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();
    }
}
