package com.albanfontaine.go4lunch.Models;

public class User {
    private String mUid;
    private String mUsername;
    private String mAvatar;
    private String mRestaurantChosen;

    public User(){ }

    public User(String uid, String username, String avatar, String restaurantChosen){
        this.mUid = uid;
        this.mUsername = username;
        this.mAvatar = avatar;
        this.mRestaurantChosen = restaurantChosen;
    }

    // GETTERS AND SETTERS
    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getRestaurantChosen() {
        return mRestaurantChosen;
    }

    public void setRestaurantChosen(String restaurantChosen) {
        mRestaurantChosen = restaurantChosen;
    }
}
