package com.albanfontaine.go4lunch.Controllers;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Location mLocation;
    private ArrayList<Restaurant> mRestaurants;
    private PlacesClient mPlacesClient;

    public MapFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.getRestaurantListAndLocation();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(false);
        this.zoomOnMyLocation(mMap);
        Places.initialize(getContext(), getResources().getString(R.string.googlemaps_api));
        mPlacesClient = Places.createClient(getContext());
        mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.map_style)));
        mMap.setOnMarkerClickListener(this);
        this.addMarkers();
    }

    private void addMarkers(){
        for(Restaurant restaurant : mRestaurants){
            Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()))
                .icon(Utils.bitmapDescriptorFromVector(getContext(), R.drawable.restaurant_marker))
            );
            marker.setTag(restaurant);
        }
    }

    private void zoomOnMyLocation(GoogleMap map) {
        // Checks for permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.MY_PERMISSIONS_REQUEST_LOCATION);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            // Zoom the map on current location
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
            if (location != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(16)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    public void getRestaurantListAndLocation(){
        Gson gson = new Gson();
        Type arrayType = new TypeToken<ArrayList<Restaurant>>(){ }.getType();
        Type locationType = new TypeToken<Location>(){ }.getType();
        String restaurantList = getArguments().getString(Constants.RESTAURANT_LIST);
        mRestaurants = gson.fromJson(restaurantList, arrayType);
        String location = getArguments().getString(Constants.LOCATION);
        mLocation = gson.fromJson(location, locationType);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Gson gson = new Gson();
        Type restaurantType = new TypeToken<Restaurant>() { }.getType();
        String restaurant = gson.toJson(marker.getTag(), restaurantType);
        Intent intent = new Intent(getContext(), RestaurantCardActivity.class);
        intent.putExtra(Constants.RESTAURANT, restaurant);
        startActivity(intent);

        return false;
    }
}
