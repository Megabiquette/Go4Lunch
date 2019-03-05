package com.albanfontaine.go4lunch.Models;

public class User {
    private String mUid;
    private String mUsername;
    private String mAvatar;
    private boolean mHasChosen;
    private Restaurant mRestaurantChosen;

    public User(String uid, String username, String avatar, boolean hasChosen, Restaurant restaurantChosen){
        this.mUid = uid;
        this.mUsername = username;
        this.mAvatar = avatar;
        this.mHasChosen = hasChosen;
        this.mRestaurantChosen = restaurantChosen;
    }

    public String getUid() {
        return mUid;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public boolean isHasChosen() {
        return mHasChosen;
    }

    public Restaurant getRestaurantChosen() {
        return mRestaurantChosen;
    }
}
