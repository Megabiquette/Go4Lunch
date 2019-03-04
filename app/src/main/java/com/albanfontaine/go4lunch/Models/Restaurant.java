package com.albanfontaine.go4lunch.Models;

public class Restaurant {
    private String mName;
    private String mDistance;
    private String mAddress;
    private String mHours;
    private int mPeopleCount;
    private int mRating;
    private String mPhoto;

    public Restaurant(String name, String distance, String address, String hours, int peopleCount, int rating, String photo){
        this.mName = name;
        this.mDistance = distance;
        this.mAddress = address;
        this.mHours = hours;
        this.mPeopleCount = peopleCount;
        this.mRating = rating;
        this.mPhoto = photo;
    }

    public String getName() {
        return mName;
    }

    public String getDistance() {
        return mDistance;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getHours() {
        return mHours;
    }

    public int getPeopleCount() {
        return mPeopleCount;
    }

    public int getRating() {
        return mRating;
    }

    public String getPhoto() {
        return mPhoto;
    }
}
