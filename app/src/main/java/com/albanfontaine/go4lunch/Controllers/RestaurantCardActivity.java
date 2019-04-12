package com.albanfontaine.go4lunch.Controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.restaurant_card_choose_button) ImageButton mChooseButton;
    @BindView(R.id.restaurant_card_recycler_view) RecyclerView mRecyclerView;

    private Restaurant mRestaurant;
    private List<User> mWorkmates;
    private WorkmateAdapter mAdapter;
    private int numberWorkmates = 0;
    private boolean mChosen = false;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_card);
        ButterKnife.bind(this);
        mWorkmates = new ArrayList<>();
        this.getUser();

        mCallIcon.setOnClickListener(this);
        mLikeIcon.setOnClickListener(this);
        mWebsiteIcon.setOnClickListener(this);
        mChooseButton.setOnClickListener(this);

        this.getRestaurant(savedInstanceState);
        this.getWorkmates();
        this.displayRestaurantInfos();
    }

    private void getRestaurant(Bundle bundle){
        Gson gson = new Gson();
        Type restaurantType = new TypeToken<Restaurant>() { }.getType();
        String restaurant = getIntent().getStringExtra(Constants.RESTAURANT);
        mRestaurant = gson.fromJson(restaurant, restaurantType);
    }

    private void getWorkmates(){
        UserHelper.getAllUsers().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    numberWorkmates = task.getResult().getDocuments().size();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        User workmate = document.toObject(User.class);
                        mWorkmates.add(workmate);
                        if(mWorkmates.size() == numberWorkmates){
                            configureRecyclerView();
                        }
                    }
                }else {
                    Log.e("Workmate query error", task.getException().getMessage());
                }
            }
        });
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

            case R.id.restaurant_card_choose_button:
                this.clickChoose();
                break;
        }
    }

    private void clickChoose(){
        if(!mChosen){ // not chosen at first
            mChooseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            mChosen = true;
            this.selectRestaurant();
        }else{
            mChooseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_cross));
            mChosen = false;
            this.deselectRestaurant();
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

    private void getUser(){
        UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    mUser = task.getResult().toObject(User.class);
                    setChosenOrNot();
                }else{
                    Log.e("getUser", task.getException().getMessage());
                }
            }
        });
    }

    private void selectRestaurant(){
        UserHelper.selectRestaurant(mUser.getUid(), mRestaurant.getName());
    }

    private void deselectRestaurant(){
        UserHelper.deselectRestaurant(mUser.getUid());
    }

    private void setChosenOrNot(){
        if(mUser.getRestaurantChosen()!= null && mUser.getRestaurantChosen().equals(mName.getText().toString())){
            mChooseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            mChosen = true;
        }
    }

    private void configureRecyclerView(){
        // Configures the RecyclerView and its components
        Log.e("recyclerview", mWorkmates.toString());
        this.mAdapter = new WorkmateAdapter(this.mWorkmates, this);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
