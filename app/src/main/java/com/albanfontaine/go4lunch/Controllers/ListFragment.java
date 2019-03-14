package com.albanfontaine.go4lunch.Controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    @BindView(R.id.fragment_list_recycler_view) RecyclerView mRecyclerView;

    private RestaurantAdapter mAdapter;
    private ArrayList<Restaurant> mRestaurants;

    public ListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, result);

        this.getRestaurantList();
        this.configureRecyclerView();
        this.configureOnClickRecyclerView();

        return result;
    }

    public void getRestaurantList(){
        Gson gson = new Gson();
        Type arrayType = new TypeToken<ArrayList<Restaurant>>(){ }.getType();
        String restaurantList = getArguments().getString(Constants.RESTAURANT_LIST);
        mRestaurants = gson.fromJson(restaurantList, arrayType);
    }


    ////////////////////
    // CONFIGURATIONS //
    ////////////////////

    private void configureRecyclerView(){
        // Configures the RecyclerView and its components
        this.mAdapter = new RestaurantAdapter(this.mRestaurants);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    // Click on an article in the list
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                    }
                });
    }

}
