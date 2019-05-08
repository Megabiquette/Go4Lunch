package com.albanfontaine.go4lunch.Models;

import java.util.List;

public class Restaurant {
    private String mId;
    private double mLatitude;
    private double mLongitude;
    private String mName;
    private String mDistance;
    private String mAddress;
    private boolean mIsOpenNow;
    private String mOpeningHours;
    private String mClosingHours;
    private int mRating;
    private String mPhotoRef;
    private String mPhone;
    private String mWebsite;

    public Restaurant() { }

    public Restaurant(String id, String name, String address, double latitude, double longitude, String distance, String phone, int rating, String photoRef,
                      boolean isOpenNow, String openingHours, String closingHours, String website){
        this.mId = id;
        this.mName = name;
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mDistance = distance;
        this.mPhone = phone;
        this.mRating = rating;
        this.mPhotoRef = photoRef;
        this.mIsOpenNow = isOpenNow;
        this.mOpeningHours = openingHours;
        this.mClosingHours = closingHours;
        this.mWebsite = website;
    }

    // GETTERS AND SETTERS

    public String getId() { return mId; }

    public void setId(String id) { mId = id; }

    public double getLatitude() { return mLatitude; }

    public void setLatitude(double latitude) { mLatitude = latitude; }

    public double getLongitude() { return mLongitude; }

    public void setLongitude(double longitude) { mLongitude = longitude; }

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

    public String getOpeningHours() {
        return mOpeningHours;
    }

    public void setOpeningHours(String openingHours) {
        mOpeningHours = openingHours;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }

    public String getPhotoRef() {
        return mPhotoRef;
    }

    public void setPhotoRef(String photo) {
        mPhotoRef = photo;
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

    public boolean isOpenNow() { return mIsOpenNow; }

    public String getClosingHours() { return mClosingHours; }

    public void setOpenNow(boolean openNow) { mIsOpenNow = openNow; }

    public void setClosingHours(String closingHours) { mClosingHours = closingHours; }

}
