package com.albanfontaine.go4lunch.Models;

public class FirebaseRestaurant {

    private String mName;

    public FirebaseRestaurant(String name){
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
