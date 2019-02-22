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

import com.albanfontaine.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_base_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_base_nav_view) NavigationView mNavigationView;
    @BindView(R.id.activity_base_bottom_navigation) BottomNavigationView mBottomNavigationView;
    ImageView mAvatar;
    TextView mUsername;
    TextView mEmail;

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

        configureToolbar();
        configureDrawerLayout();
        configureNavigationView();
        setUserInfos();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_base_frame_layout, new MapFragment());
        transaction.commit();
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected boolean isCurrentUserLoggedIn(){ return this.getCurrentUser() != null; }

    private void setUserInfos(){
        if(getCurrentUser() != null){
            mUsername.setText(getCurrentUser().getDisplayName());
            mEmail.setText(getCurrentUser().getEmail());

            String facebookUserId = "";
            for(UserInfo profile : getCurrentUser().getProviderData()) {
                facebookUserId = profile.getUid();
            }
            String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=75";
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
                showFragment(new MapFragment());
                break;
            case R.id.bottom_nav_list:
                showFragment(new ListFragment());
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

    private void showFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_base_frame_layout, fragment);
        transaction.commit();
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
