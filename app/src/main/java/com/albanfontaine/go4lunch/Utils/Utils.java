package com.albanfontaine.go4lunch.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Calendar;
import java.util.List;

public class Utils {

    /**
     * Converts Location to a String in of both latitude and longitude separated by a comma in "DDD.DDDDD,DDD.DDDDD" format
     *
     * @param location A Location object
     * @return A String of the latitude and longitude of the location
     */
    public static String convertLocationToString(Location location){
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + "," + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
    }

    /**
     * Creates a definition of a Bitmap image, used for marker icons
     *
     * @param context A Context
     * @param vectorResId A ressource bitmap
     * @return A BitmapDescriptor
     */
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    /**
     * Calculates the distance in meters between two locations
     *
     * @param startLocation The first location
     * @param lat The latitude of the second location
     * @param lng The longitude of the second location
     * @return The distance in meters, as a String object
     */
    public static String calculateDistanceBetweenLocations(Location startLocation, float lat, float lng){
        Location endLocation = new Location("");
        endLocation.setLatitude(lat);
        endLocation.setLongitude(lng);
        float distance = startLocation.distanceTo(endLocation);
        return Integer.toString((int) distance);
    }

    /**
     * Returns the day of the week
     *
     * @return An int representing the day of the week, starting at 0 for sunday
     */
    public static int getTheDayOfTheWeek(){
        return Calendar.getInstance().DAY_OF_WEEK -1;
    }

    public static String formatHours(String hours){
        return hours.substring(0,2) + ":" + hours.substring(2,4);
    }

    /**
     * Displays a number of yellow star icons according to the restaurant's rating
     *
     * @param restaurant The restaurant
     * @param rating1 A yellow star to make visible (as an ImageView object), same goes for the other stars
     */
    public static void showRatingStars(Restaurant restaurant, ImageView rating1, ImageView rating2, ImageView rating3){
        if(restaurant.getRating() >= 1){
            rating1.setVisibility(View.VISIBLE);
        }
        if(restaurant.getRating() >= 2){
            rating2.setVisibility(View.VISIBLE);
        }
        if(restaurant.getRating() >= 3){
            rating3.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Returns a Restaurant object from a restaurant list using its name
     *
     * @param name name of the restaurant to return
     * @param restaurants list of Restaurant objects
     * @return
     */
    public static Restaurant getRestaurantChosen(String name, List<Restaurant> restaurants){
        for(Restaurant restaurant : restaurants){
            if(restaurant.getName().equals(name)){
                return restaurant;
            }
        }
        return null;
    }

    /**
     * Checks the internet connection
     *
     * @param context the current context
     * @return a boolean set to true if the device is connected to the internet
     */
    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        return network != null && network.isConnectedOrConnecting();
    }

}
