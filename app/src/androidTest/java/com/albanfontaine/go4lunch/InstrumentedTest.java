package com.albanfontaine.go4lunch;

import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;


import com.albanfontaine.go4lunch.Models.ApiResponsePlaceDetails;
import com.albanfontaine.go4lunch.Models.ApiResponsePlaceSearchRestaurant;
import com.albanfontaine.go4lunch.Utils.GoogleStreams;
import com.albanfontaine.go4lunch.Utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class InstrumentedTest {

    Context context = InstrumentationRegistry.getTargetContext();

    // Converts Location to a String in of both latitude and longitude separated by a comma in "DDD.DDDDD,DDD.DDDDD" format
    @Test
    public void convertLocationToStringTest(){
        Location location = new Location("provider");
        location.setLatitude(48.8758);
        location.setLongitude(2.3468);

        assertEquals("48.8758,2.3468", Utils.convertLocationToString(location));
    }

    // Calculates the distance in meters between two locations and converts it to String
    @Test
    public void calculateDistanceBetweenLocationsTest(){
        Location location = new Location("provider");
        location.setLatitude(48.8758);
        location.setLongitude(2.3468);

        double latitude = 48.876258;
        double longitude = 2.347319;

        assertEquals("63", Utils.calculateDistanceBetweenLocations(location, (float) latitude, (float) longitude));
    }

    // Searches nearby restaurants
    @Test
    public void fetchNearbyRestaurantsTest(){
        Location location = new Location("provider");
        location.setLatitude(48.8758);
        location.setLongitude(2.3468);
        String loc = Utils.convertLocationToString(location);

        Observable<ApiResponsePlaceSearchRestaurant> observableRestaurants = GoogleStreams.streamFetchNearbyRestaurants(loc);
        TestObserver<ApiResponsePlaceSearchRestaurant> testObserver = new TestObserver<>();

        observableRestaurants.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        ApiResponsePlaceSearchRestaurant restaurantsFetched = testObserver.values().get(0);

        // Checks that the responses gets at least 1 restaurant
        assertNotEquals(restaurantsFetched.getResults().size(), 0);
        // Checks that the first restaurant has a place id
        assertNotNull("restaurant has a place id", restaurantsFetched.getResults().get(0).getPlaceId());
    }

    // Fetches restaurant details
    @Test
    public void fetchPlaceDetailsTest(){
        String placeId = "ChIJidCwC0Bu5kcRhyADcTSSsrY";

        Observable<ApiResponsePlaceDetails> observableDetails = GoogleStreams.streamFetchPlaceDetail(placeId);
        TestObserver<ApiResponsePlaceDetails> testObserver = new TestObserver<>();

        observableDetails.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        ApiResponsePlaceDetails.Result restaurant = testObserver.values().get(0).getResult();

        // Checks the name
        assertEquals("Mi Ranchito Paisa", restaurant.getName());
        // Checks the website
        assertEquals("http://www.miranchitopaisa.fr/", restaurant.getWebsite());
        // Checks the phone number
        assertEquals("+33 1 48 78 45 94", restaurant.getInternationalPhoneNumber());
        // Checks the latitude
        assertEquals(48.87645699999999, restaurant.getGeometry().getLocation().getLat(),0.1);
        // Checks the longitude
        assertEquals(2.344879, restaurant.getGeometry().getLocation().getLng(),0.1);

    }


}
