package com.albanfontaine.go4lunch.Models;

import java.util.List;

public class FirebaseRestaurant {

    private String mName;
    private List<String> muUsersWhoLiked;
    private List<String> mUsersWhoJoined;

    public FirebaseRestaurant(String name, List<String> usersWhoLiked, List<String> usersWhoJoined){
        this.mName = name;
        this.muUsersWhoLiked = usersWhoLiked;
        this.mUsersWhoJoined = usersWhoJoined;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<String> getMuUsersWhoLiked() {
        return muUsersWhoLiked;
    }

    public void setMuUsersWhoLiked(List<String> muUsersWhoLiked) {
        this.muUsersWhoLiked = muUsersWhoLiked;
    }

    public List<String> getUsersWhoJoined() {
        return mUsersWhoJoined;
    }

    public void setUsersWhoJoined(List<String> usersWhoJoined) {
        mUsersWhoJoined = usersWhoJoined;
    }
}
