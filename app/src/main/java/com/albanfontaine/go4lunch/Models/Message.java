package com.albanfontaine.go4lunch.Models;

import java.util.Date;

public class Message {
    private String mMessage;
    private Date mDateCreated;
    private User mUser;
    private String mAvatarUrl;

    public Message(){}

    public Message(String message, User user){
        this.mMessage = message;
        this.mUser = user;
    }

    public Message(String message, User user, String avatarUrl){
        this.mMessage = message;
        this.mUser = user;
        this.mAvatarUrl = avatarUrl;
    }

    // GETTERS AND SETTERS

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Date getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        mDateCreated = dateCreated;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }
}
