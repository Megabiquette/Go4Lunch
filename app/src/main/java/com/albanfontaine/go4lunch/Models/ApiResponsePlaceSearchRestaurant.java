package com.albanfontaine.go4lunch.Models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponsePlaceSearchRestaurant {

    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public List<Result> getResults() {
        return results;
    }

    public class Result {

        @SerializedName("place_id")
        @Expose
        private String placeId;

        public String getPlaceId() {
            return placeId;
        }
    }

}
