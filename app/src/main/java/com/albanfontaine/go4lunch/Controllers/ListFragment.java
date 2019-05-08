package com.albanfontaine.go4lunch.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.ItemClickSupport;
import com.albanfontaine.go4lunch.Views.RestaurantAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    @BindView(R.id.fragment_list_recycler_view) RecyclerView mRecyclerView;

    private RestaurantAdapter mAdapter;
    private ArrayList<Restaurant> mRestaurants;
    private Gson mGson;

    public ListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, result);

        mGson = new Gson();
        this.getRestaurantList();
        this.configureRecyclerView();
        this.configureOnClickRecyclerView();

        // sorts the restaurants by distance
        Collections.sort(mRestaurants, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                return Integer.valueOf(o1.getDistance()).compareTo(Integer.valueOf(o2.getDistance()));
            }
        });

        return result;
    }

    public void getRestaurantList(){
        Type arrayType = new TypeToken<ArrayList<Restaurant>>(){ }.getType();
        String restaurantList = getArguments().getString(Constants.RESTAURANT_LIST);
        mRestaurants = mGson.fromJson(restaurantList, arrayType);
    }


    ////////////////////
    // CONFIGURATIONS //
    ////////////////////

    private void configureRecyclerView(){
        // Configures the RecyclerView and its components
        this.mAdapter = new RestaurantAdapter(this.mRestaurants, getContext());
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // Click on an article in the list
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent intent = new Intent(getContext(), RestaurantCardActivity.class);
                        Type restaurantType = new TypeToken<Restaurant>() { }.getType();
                        String restaurantGSON = mGson.toJson(mAdapter.getRestaurant(position), restaurantType);
                        intent.putExtra(Constants.RESTAURANT, restaurantGSON);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
