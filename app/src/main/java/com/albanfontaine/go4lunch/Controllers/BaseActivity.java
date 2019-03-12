package com.albanfontaine.go4lunch.Controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.UserHelper;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_base_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_base_nav_view) NavigationView mNavigationView;
    @BindView(R.id.activity_base_bottom_navigation) BottomNavigationView mBottomNavigationView;
    // Drawer menu header
    ImageView mAvatar;
    TextView mUsername;
    TextView mEmail;

    private Disposable mDisposable;
    private List<Restaurant> mRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        // Adds the header to the menu drawer
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, mNavigationView, false);
        mNavigationView.addHeaderView(headerView);

        mAvatar =  headerView.findViewById(R.id.drawer_avatar);
        mUsername  = headerView.findViewById(R.id.drawer_username);
        mEmail = headerView.findViewById(R.id.drawer_email);

        mRestaurants = new ArrayList<Restaurant>();

        configureToolbar();
        configureDrawerLayout();
        configureNavigationView();
        setUserInfos();

        this.createUserInFirestore();
        this.showFragmentWithList(new MapFragment());
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected boolean isCurrentUserLoggedIn(){ return this.getCurrentUser() != null; }

    private void setUserInfos(){
        if(getCurrentUser() != null){
            mUsername.setText(getCurrentUser().getDisplayName());
            mEmail.setText(getCurrentUser().getEmail());

            // Get the provider for the user's avatar url
            String provider = this.getCurrentUser().getProviders().get(0);
            String photoUrl;
            if (provider.equals("facebook.com")){ // Facebook
                String facebookUserId = "";
                for(UserInfo profile : getCurrentUser().getProviderData()) {
                    facebookUserId = profile.getUid();
                }
                photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=75";
            }else{ // Google
                photoUrl = this.getCurrentUser().getPhotoUrl().toString();
            }

            Picasso.with(this).load(photoUrl).transform(new CropCircleTransformation()).into(mAvatar);
        }
    }

    /////////////
    // BUTTONS //
    /////////////

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            // Menu drawer buttons
            case R.id.drawer_lunch:

                break;
            case R.id.drawer_settings:

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
                showFragment(new WorkmatesFragment());
                break;
            case R.id.bottom_nav_chat:
                showFragment(new ChatFragment());
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

        }
        return true;
    }

    // Show fragment and send the list of restaurant with a bundle
    private void showFragmentWithList(Fragment fragment){
        Type arrayType = new TypeToken<ArrayList<Restaurant>>(){
        }.getType();
        Gson gson = new Gson();
        String restaurantList = gson.toJson(mRestaurants, arrayType);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESTAURANT_LIST, restaurantList);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_base_frame_layout, fragment);
        transaction.commit();
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_base_frame_layout, fragment);
        transaction.commit();
    }

    private void createUserInFirestore(){
        if(this.getCurrentUser() != null){
            String uid = this.getCurrentUser().getUid();
            String username = this.getCurrentUser().getDisplayName();
            String avatar = (this.getCurrentUser().getPhotoUrl() != null) ?
                    this.getCurrentUser().getPhotoUrl().toString() : null;

            UserHelper.createUser(uid, username, avatar, null)
            .addOnFailureListener(this.onFailureListener());
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

    private void configureNavigationView(){
        ButterKnife.bind(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }
}
