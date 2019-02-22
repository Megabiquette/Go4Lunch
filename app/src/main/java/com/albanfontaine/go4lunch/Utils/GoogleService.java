package com.albanfontaine.go4lunch.Utils;

import com.albanfontaine.go4lunch.Models.ApiResponsePlaceDetails;
import com.albanfontaine.go4lunch.Models.ApiResponsePlaceSearchRestaurant;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleService {

    String API_KEY = "AIzaSyBzJZaFSqc8RFu29z9R_0n12pGjsE-7BrA";

    @GET("nearbysearch/json?type=restaurant&radius=1000&key=" + API_KEY)
    Observable<ApiResponsePlaceSearchRestaurant> fetchNearbyRestaurants(@Query("location") String location);

    @GET("details/json?key=" + API_KEY)
    Observable<ApiResponsePlaceDetails> fetchPlaceDetails(@Query("placeid") String placeId);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
