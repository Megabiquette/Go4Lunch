package com.albanfontaine.go4lunch.Controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.albanfontaine.go4lunch.Models.ApiResponsePlaceDetails;
import com.albanfontaine.go4lunch.Models.ApiResponsePlaceSearchRestaurant;
import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.GoogleStreams;
import com.albanfontaine.go4lunch.Utils.RestaurantHelper;
import com.albanfontaine.go4lunch.Utils.UserHelper;
import com.albanfontaine.go4lunch.Utils.Utils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_base_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_base_nav_view) NavigationView mNavigationView;
    @BindView(R.id.activity_base_bottom_navigation) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.toolbar_autocomplete) Toolbar mToolbarAutocomplete;
    @BindView(R.id.autocomplete_search_icon) ImageView mAutocompleteSearchIcon;
    @BindView(R.id.activity_base_no_internet) TextView mNoInternetTextView;
    @BindView(R.id.activity_base_frame_layout) FrameLayout mFrameLayout;
    AutoCompleteTextView mAutocompleteTextView;
    // Drawer menu header
    ImageView mAvatar;
    TextView mUsername;
    TextView mEmail;

    private Disposable mDisposable;
    private List<Restaurant> mRestaurants;
    private Location mLocation;
    private int nbRestaurantsFetched;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);

        mRestaurants = new ArrayList<>();
        mGson = new Gson();

        // Check internet connection
        if(Utils.isConnectedToInternet(this)){
            configureToolbar();
            configureDrawerLayout();
            configureDrawerHeader();
            configureNavigationView();
            setUserInfos();
            setErrorHandler();
            this.createUserInFirestore();
            this.getCurrentLocation();
            this.searchNearbyRestaurantsRequest();
        }else{
            mToolbar.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.GONE);
            mBottomNavigationView.setVisibility(View.GONE);
            mNoInternetTextView.setVisibility(View.VISIBLE);
        }

    }

    ///////////////////
    // HTTP REQUESTS //
    ///////////////////

    private void searchNearbyRestaurantsRequest(){
        this.mDisposable = GoogleStreams.streamFetchNearbyRestaurants(Utils.convertLocationToString(mLocation))
                .subscribeWith(new DisposableObserver<ApiResponsePlaceSearchRestaurant>(){
                    @Override
                    public void onNext(ApiResponsePlaceSearchRestaurant apiResponsePlaceSearchRestaurant) {
                        nbRestaurantsFetched = apiResponsePlaceSearchRestaurant.getResults().size();
                        // For each restaurant in the Results list returned, search for its details
                        for (ApiResponsePlaceSearchRestaurant.Result result : apiResponsePlaceSearchRestaurant.getResults()){
                            restaurantDetailRequest(result.getPlaceId());
                        }
                    }

                    @Override
                    public void onError(Throwable e) { Log.e("Request error", e.getMessage()); }

                    @Override
                    public void onComplete() { }
                });
    }

    private void restaurantDetailRequest(String placeId){
        this.mDisposable = GoogleStreams.streamFetchPlaceDetail(placeId)
                .subscribeWith(new DisposableObserver<ApiResponsePlaceDetails>(){
                    @Override
                    public void onNext(ApiResponsePlaceDetails apiResponsePlaceDetails) {
                        createRestaurants(apiResponsePlaceDetails, placeId);
                    }

                    @Override
                    public void onError(Throwable e) {  Log.e("Request error", e.getMessage()); }

                    @Override
                    public void onComplete() {
                        // Checks if all restaurants have been created
                        if(mRestaurants.size() == nbRestaurantsFetched){
                            configureAutoComplete();
                            showFragmentWithList(new MapFragment());
                        }
                    }
                });
    }

    // Create a restaurant object and add it to the restaurant list
    private void createRestaurants(ApiResponsePlaceDetails response, String placeId){
        ApiResponsePlaceDetails.Result result = response.getResult();
        String id = placeId;
        String name = result.getName();
        String address = result.getAddressComponents().get(0).getShortName() + ", " + result.getAddressComponents().get(1).getShortName();
        double latitude = result.geometry.getLocation().getLat();
        double longitude = result.geometry.getLocation().getLng();
        String distance = Utils.calculateDistanceBetweenLocations(mLocation, (float)latitude, (float)longitude);
        String phone = result.getInternationalPhoneNumber();
        int rating = (int) Math.round(result.getRating()*3/5);
        String photoRef = null;
        if(result.getPhotos() != null )
            photoRef = result.getPhotos().get(0).getPhotoReference();
        boolean isOpenNow;
        String closingHours="";
        String openingHours="";
        if(result.getOpeningHours() != null){
            isOpenNow = result.getOpeningHours().getOpenNow();
            closingHours = result.getOpeningHours().getPeriods().get(0).getClose() != null ? result.getOpeningHours().getPeriods().get(Utils.getTheDayOfTheWeek()).getClose().getTime() : null;
            if(result.getOpeningHours().getPeriods().get(0).getOpen() != null ){
                // Checks if restaurant is always open
                openingHours = result.getOpeningHours().getPeriods().get(0).getOpen().getTime().equals("0000") ? "0000" : result.getOpeningHours().getPeriods().get(Utils.getTheDayOfTheWeek()).getOpen().getTime();
            }
        }else{
            isOpenNow = false;
            closingHours = null;
            openingHours = null;
        }
        String website = result.getWebsite();
        Restaurant restaurant = new Restaurant(id, name, address, latitude, longitude, distance, phone, rating, photoRef, isOpenNow, openingHours, closingHours, website);
        mRestaurants.add(restaurant);

        // Create restaurant in Firestore
        String details = mGson.toJson(restaurant);
        RestaurantHelper.createRestaurant(name, details).addOnFailureListener(this.onFailureListener());
    }

    private void getCurrentLocation(){
        // Checks permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.MY_PERMISSIONS_REQUEST_LOCATION);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
                locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER) :
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    /////////////
    // BUTTONS //
    /////////////

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            // Menu drawer buttons
            case R.id.drawer_lunch:
                UserHelper.getUser(getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
                            if(user.getRestaurantChosen() == null){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_restaurant_chosen), Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(getApplicationContext(), RestaurantCardActivity.class);
                                Type restaurantType = new TypeToken<Restaurant>() { }.getType();
                                Restaurant restaurant = Utils.getRestaurantChosen(user.getRestaurantChosen(), mRestaurants);
                                String restaurantString = mGson.toJson(restaurant, restaurantType);
                                intent.putExtra(Constants.RESTAURANT, restaurantString);
                                startActivity(intent);
                            }
                        }else{
                            Log.e("getUser", task.getException().getMessage());
                        }
                    }
                });
                break;

            case R.id.drawer_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.drawer_logout:
                // Log out user and return to sign in screen
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(BaseActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                break;

            // Bottom nav bar buttons
            case R.id.bottom_nav_map:
                showFragmentWithList(new MapFragment());
                break;
            case R.id.bottom_nav_list:
                showFragmentWithList(new ListFragment());
                break;
            case R.id.bottom_nav_workmates:
                showFragmentWithList(new WorkmatesFragment());
                break;
            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Search button
        if(item.getItemId() == R.id.toolbar_search){
            mToolbar.setVisibility(View.GONE);
            mToolbarAutocomplete.setVisibility(View.VISIBLE);
        }
        return true;
    }

    // Show fragment and send the list of restaurant with a bundle
    private void showFragmentWithList(Fragment fragment){
        Type arrayType = new TypeToken<ArrayList<Restaurant>>(){ }.getType();
        Type locationType = new TypeToken<Location>(){ }.getType();
        String restaurantList = mGson.toJson(mRestaurants, arrayType);
        String location = mGson.toJson(mLocation, locationType);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESTAURANT_LIST, restaurantList);
        bundle.putString(Constants.LOCATION, location);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_base_frame_layout, fragment);
        transaction.commit();
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    private void createUserInFirestore(){
        if(getCurrentUser() != null){
            UserHelper.getUsersCollection().document(getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(!task.getResult().exists()){
                            // The user is not in the database yet, so we add them
                            String uid = getCurrentUser().getUid();
                            String username = getCurrentUser().getDisplayName();
                            String avatar = (getCurrentUser().getPhotoUrl() != null) ?
                                    getCurrentUser().getPhotoUrl().toString() : null;

                            UserHelper.createUser(uid, username, avatar, null, null)
                                    .addOnFailureListener(onFailureListener());
                        }
                    }else{
                        Log.e("createUserInFirestore", task.getException().getMessage());
                    }
                }
            });
        }
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    ////////////////////
    // CONFIGURATIONS //
    ////////////////////

    private void configureToolbar(){
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void configureDrawerLayout(){
        ButterKnife.bind(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureDrawerHeader(){
        // Adds the header to the menu drawer
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, mNavigationView, false);
        mNavigationView.addHeaderView(headerView);

        mAvatar =  headerView.findViewById(R.id.drawer_avatar);
        mUsername  = headerView.findViewById(R.id.drawer_username);
        mEmail = headerView.findViewById(R.id.drawer_email);
    }

    private void configureAutoComplete(){
        mAutocompleteSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.autocomplete_search_icon){
                    mToolbar.setVisibility(View.VISIBLE);
                    mToolbarAutocomplete.setVisibility(View.GONE);
                }
            }
        });
        ArrayList<String> namesArrayList = new ArrayList<>();
        for(Restaurant restaurant : mRestaurants){
            namesArrayList.add(restaurant.getName());
        }
        String[] restaurantNames = namesArrayList.toArray(new String[namesArrayList.size()]);
        ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(this, R.layout.autocomplete_adapter, restaurantNames);
        mAutocompleteTextView = findViewById(R.id.autocomplete_textview);
        mAutocompleteTextView.setAdapter(autocompleteAdapter);
        mAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                for(Restaurant restaurant : mRestaurants){
                    if(restaurant.getName().equals(name)){
                        Intent intent = new Intent(getApplicationContext(), RestaurantCardActivity.class);
                        Type restaurantType = new TypeToken<Restaurant>() { }.getType();
                        String restaurantGSON = mGson.toJson(restaurant, restaurantType);
                        intent.putExtra(Constants.RESTAURANT, restaurantGSON);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void configureNavigationView(){
        ButterKnife.bind(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setUserInfos(){
        if(getCurrentUser() != null){
            mUsername.setText(getCurrentUser().getDisplayName());
            mEmail.setText(getCurrentUser().getEmail());

            // Get the provider for the user's avatar url
            String provider = this.getCurrentUser().getProviders().get(0);
            String photoUrl;
            if(this.getCurrentUser().getPhotoUrl() == null){
                photoUrl = null;
            }else{
                if (provider.equals("facebook.com")){ // Facebook
                    String facebookUserId = "";
                    for(UserInfo profile : getCurrentUser().getProviderData()) {
                        facebookUserId = profile.getUid();
                    }
                    photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=75";
                }else{
                    photoUrl = this.getCurrentUser().getPhotoUrl().toString();
                }

                Picasso.with(this).load(photoUrl).transform(new CropCircleTransformation()).into(mAvatar);
            }
        }
    }

    private void setErrorHandler(){
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException) || (e instanceof SocketException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                return;
            }
            if (e instanceof IllegalStateException) {
                return;
            }
            Log.e("Undeliverable exception", e.getMessage());
            nbRestaurantsFetched -= 1;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy(){
        if(this.mDisposable != null && !this.mDisposable.isDisposed()){
            this.mDisposable.dispose();
        }
    }
}
