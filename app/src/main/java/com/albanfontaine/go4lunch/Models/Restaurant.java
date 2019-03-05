package com.albanfontaine.go4lunch.Models;

public class Restaurant {
    private String mUid;
    private String mName;
    private String mDistance;
    private String mAddress;
    private String mHours;
    private int mPeopleCount;
    private int mRating;
    private String mPhoto;
    private String mPhone;
    private String mWebsite;
    private String mFoodStyle;

    public Restaurant(String uid, String name, String distance, String address, String hours, int peopleCount, int rating, String photo, String phone, String website, String foodStyle){
        this.mUid = uid;
        this.mName = name;
        this.mDistance = distance;
        this.mAddress = address;
        this.mHours = hours;
        this.mPeopleCount = peopleCount;
        this.mRating = rating;
        this.mPhoto = photo;
        this.mPhone = phone;
        this.mWebsite = website;
        this.mFoodStyle = foodStyle;
    }

    public String getUid(){
        return mUid;
    }

    public String getName() {
        return mName;
    }

    public String getDistance() {
        return mDistance;
    }

    public String getAddress() { return mAddress; }

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

    public String getPhone() {
        return mPhone;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public String getFoodStyle() {
        return mFoodStyle;
    }
}
