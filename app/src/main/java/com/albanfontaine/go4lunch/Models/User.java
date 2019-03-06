package com.albanfontaine.go4lunch.Models;

public class User {
    private String mUid;
    private String mUsername;
    private String mAvatar;
    private Restaurant mRestaurantChosen;

    public User(String uid, String username, String avatar, Restaurant restaurantChosen){
        this.mUid = uid;
        this.mUsername = username;
        this.mAvatar = avatar;
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

    public Restaurant getRestaurantChosen() {
        return mRestaurantChosen;
    }
}
