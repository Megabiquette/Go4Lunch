package com.albanfontaine.go4lunch.Models;

import java.util.List;

public class FirebaseRestaurant {

    private String mName;
    private List<User> mUsersWhoLiked;
    private List<User> mUsersWhoJoined;

    public FirebaseRestaurant(String name, List<User> usersWhoLiked, List<User> usersWhoJoined){
        this.mName = name;
        this.mUsersWhoLiked = usersWhoLiked;
        this.mUsersWhoJoined = usersWhoJoined;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<User> getUsersWhoLiked() {
        return mUsersWhoLiked;
    }

    public void setUsersWhoLiked(List<User> usersWhoLiked) {
        mUsersWhoLiked = usersWhoLiked;
    }

    public List<User> getUsersWhoJoined() {
        return mUsersWhoJoined;
    }

    public void setUsersWhoJoined(List<User> usersWhoJoined) {
        mUsersWhoJoined = usersWhoJoined;
    }
}
