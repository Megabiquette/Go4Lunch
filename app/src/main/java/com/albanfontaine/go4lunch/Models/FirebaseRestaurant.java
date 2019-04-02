package com.albanfontaine.go4lunch.Models;

import java.util.List;

public class FirebaseRestaurant {

    private String mName;
    private List<String> mUsersWhoLiked;
    private List<String> mUsersWhoJoined;

    public FirebaseRestaurant(String name, List<String> usersWhoLiked, List<String> usersWhoJoined){
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

    public List<String> getUsersWhoLiked() {
        return mUsersWhoLiked;
    }

    public void setUsersWhoLiked(List<String> usersWhoLiked) {
        mUsersWhoLiked = usersWhoLiked;
    }

    public List<String> getUsersWhoJoined() {
        return mUsersWhoJoined;
    }

    public void setUsersWhoJoined(List<String> usersWhoJoined) {
        mUsersWhoJoined = usersWhoJoined;
    }
}
