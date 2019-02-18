package com.albanfontaine.go4lunch.Controllers;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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
            String linkfoto = "https://graph.facebook.com/" + facebookUserId + "/picture?height=75";
            Picasso.with(this).load(linkfoto).transform(new CropCircleTransformation()).into(mAvatar);
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //case
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
