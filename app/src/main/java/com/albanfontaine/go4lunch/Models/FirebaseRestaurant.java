package com.albanfontaine.go4lunch.Models;

public class FirebaseRestaurant {

    private String mName;
    private String mRestaurantDetailsGSON;

    public FirebaseRestaurant(String name, String restaurantDetailsGSON){
        this.mName = name;
        this.mRestaurantDetailsGSON = restaurantDetailsGSON;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmRestaurantDetailsGSON() {
        return mRestaurantDetailsGSON;
    }

    public void setmRestaurantDetailsGSON(String mRestaurantDetailsGSON) {
        this.mRestaurantDetailsGSON = mRestaurantDetailsGSON;
    }
}
