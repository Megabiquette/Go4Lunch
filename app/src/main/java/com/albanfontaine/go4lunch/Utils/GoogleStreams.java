package com.albanfontaine.go4lunch.Utils;

import com.albanfontaine.go4lunch.Models.ApiResponsePlaceDetails;
import com.albanfontaine.go4lunch.Models.ApiResponsePlaceSearchRestaurant;
import com.albanfontaine.go4lunch.Models.Restaurant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GoogleStreams {

    // Place search API for nearby restaurants
    public static Observable<ApiResponsePlaceSearchRestaurant> streamFetchNearbyRestaurants(String location){
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);
        return googleService.fetchNearbyRestaurants(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // Place details API
    public static Observable<ApiResponsePlaceDetails> streamFetchPlaceDetail(String placeId){
        GoogleService googleService = GoogleService.retrofit.create(GoogleService.class);
        return googleService.fetchPlaceDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

}
