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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.RestaurantHelper;
import com.albanfontaine.go4lunch.Utils.UserHelper;
import com.albanfontaine.go4lunch.Utils.Utils;
import com.albanfontaine.go4lunch.Views.WorkmateAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @BindView(R.id.restaurant_card_icon_call) ImageView mCallIcon;
    @BindView(R.id.restaurant_card_icon_like) ImageView mLikeIcon;
    @BindView(R.id.restaurant_card_like_text) TextView mLikeText;
    @BindView(R.id.restaurant_card_icon_website) ImageView mWebsiteIcon;
    @BindView(R.id.restaurant_card_choose_button) ImageButton mChooseButton;
    @BindView(R.id.restaurant_card_recycler_view) RecyclerView mRecyclerView;

    private Restaurant mRestaurant;
    private List<User> mWorkmates;
    private WorkmateAdapter mAdapter;
    private boolean mChosen = false;
    private boolean mLiked = false;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_card);
        ButterKnife.bind(this);
        mWorkmates = new ArrayList<>();
        this.getUser();
        this.getRestaurant();

        mCallIcon.setOnClickListener(this);
        mLikeIcon.setOnClickListener(this);
        mWebsiteIcon.setOnClickListener(this);
        mChooseButton.setOnClickListener(this);

        this.displayRestaurantInfos();
    }

    private void getRestaurant(){
        Gson gson = new Gson();
        Type restaurantType = new TypeToken<Restaurant>() { }.getType();
        String restaurant = getIntent().getExtras().getString(Constants.RESTAURANT);
        Log.e("string resto", restaurant);
        mRestaurant = gson.fromJson(restaurant, restaurantType);
        Log.e("object resto", mRestaurant.toString());
    }

    // Get workmates and configure RecyclerView
    private void getWorkmates(){
        RestaurantHelper.getWhoJoinedRestaurant(mRestaurant.getName()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                            mWorkmates.add(workmate);
                        }
                        if(mUser.getUid().equals(workmate.getUid()))
                            mUser = workmate;
                    }
                    configureRecyclerView();
                }else {
                    Log.e("Workmate query error", task.getException().getMessage());
                }
            }
        }).addOnFailureListener(this.onFailureListener());
    }

    private void displayRestaurantInfos(){
        Log.e("resto name", mRestaurant.getName());
        mName.setText(mRestaurant.getName());
        mAddress.setText(mRestaurant.getAddress());

        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=700&photoreference=" + mRestaurant.getPhotoRef() + "&key=" + Constants.API_KEY;
        Picasso.with(this).load(photoUrl).fit().centerCrop().into(mPhoto);

        Utils.showRatingStars(mRestaurant, mRating1, mRating2, mRating3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restaurant_card_icon_call:
                this.callRestaurant();
                break;

            case R.id.restaurant_card_icon_like:
                this.clickLike();
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
            this.selectRestaurant();
        }else{
            this.deselectRestaurant();
        }
    }

    private void clickLike(){
        if(!mLiked){ // not liked at first
            this.likeRestaurant();
        }else{
            this.unlikeRestaurant();
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
                    setChosenAndLiked();
                    getWorkmates();
                }else{
                    Log.e("getUser", task.getException().getMessage());
                }
            }
        }).addOnFailureListener(this.onFailureListener());
    }

    private void selectRestaurant(){
        mUser.setDateChosen(new Date());
        if(mUser.getRestaurantChosen() != null){
            // User already chose another restaurant, we have to deselect it
            RestaurantHelper.removeUserToJoinList(mUser.getRestaurantChosen(), mUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mUser.setRestaurantChosen(mRestaurant.getName());
                    UserHelper.selectRestaurant(mUser.getUid(), mRestaurant.getName(), new Date()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                RestaurantHelper.addUserToJoinList(mRestaurant.getName(), mUser.getUid(), mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mChooseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
                                            mChosen = true;
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }).addOnFailureListener(this.onFailureListener());
        }else{
            mUser.setRestaurantChosen(mRestaurant.getName());
            UserHelper.selectRestaurant(mUser.getUid(), mRestaurant.getName(), new Date()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        RestaurantHelper.addUserToJoinList(mRestaurant.getName(), mUser.getUid(), mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mChooseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
                                    mChosen = true;
                                }
                            }
                        });
                    }
                }
            });
        }
        mWorkmates.add(mUser);
        mAdapter.notifyDataSetChanged();
    }

    private void deselectRestaurant(){
        RestaurantHelper.removeUserToJoinList(mUser.getRestaurantChosen(), mUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                UserHelper.deselectRestaurant(mUser.getUid());
                mChooseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_cross));
                mChosen = false;
            }
        }).addOnFailureListener(this.onFailureListener());
        mWorkmates.remove(mUser);
        mAdapter.notifyDataSetChanged();
    }

    private void likeRestaurant(){
        RestaurantHelper.addUserToLikedList(mRestaurant.getName(), mUser.getUid(), mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_green));
                mLiked = true;
                mLikeText.setText(getResources().getString(R.string.restaurant_card_liked));
            }
        }).addOnFailureListener(this.onFailureListener());
    }

    private void unlikeRestaurant(){
        RestaurantHelper.removeUserToLikedList(mRestaurant.getName(), mUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                mLiked = false;
                mLikeText.setText(getResources().getString(R.string.restaurant_card_like));
            }
        });

    }

    private void setChosenAndLiked(){
        // see if the restaurant was already chosen today
        String formattedDateNow = Utils.getFormattedDate(new Date());
        String formattedDateChosen = Utils.getFormattedDate(mUser.getDateChosen());
        if(mUser.getRestaurantChosen()!= null && mUser.getDateChosen() != null && mUser.getRestaurantChosen().equals(mName.getText().toString()) && formattedDateChosen.equals(formattedDateNow)){
            mChooseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
            mChosen = true;
        }

        // See if the restaurant was liked
        RestaurantHelper.getWhoLikedRestaurant(mRestaurant.getName()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        if(document.get("uid").equals(mUser.getUid())){
                            mLiked = true;
                            mLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_green));
                            mLikeText.setText(getResources().getString(R.string.restaurant_card_liked));
                        }
                    }
                }else{
                    Log.e("Restaurant query error", task.getException().getMessage());
                }
            }
        });
    }

    private void configureRecyclerView(){
        // Configures the RecyclerView and its components
        this.mAdapter = new WorkmateAdapter(this.mWorkmates, this);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

}
