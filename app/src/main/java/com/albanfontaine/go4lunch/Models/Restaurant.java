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

    // GETTERS AND SETTERS

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getHours() {
        return mHours;
    }

    public void setHours(String hours) {
        mHours = hours;
    }

    public int getPeopleCount() {
        return mPeopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        mPeopleCount = peopleCount;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getFoodStyle() {
        return mFoodStyle;
    }

    public void setFoodStyle(String foodStyle) {
        mFoodStyle = foodStyle;
    }
}
