package com.albanfontaine.go4lunch.Controllers;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;
import com.albanfontaine.go4lunch.Utils.Constants;
import com.albanfontaine.go4lunch.Utils.ItemClickSupport;
import com.albanfontaine.go4lunch.Utils.UserHelper;
import com.albanfontaine.go4lunch.Views.RestaurantAdapter;
import com.albanfontaine.go4lunch.Views.WorkmateAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesFragment extends Fragment {

    @BindView(R.id.fragment_workmates_recycler_view) RecyclerView mRecyclerView;

    private WorkmateAdapter mAdapter;
    private List<User> mWorkmates;

    public WorkmatesFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, result);
        mWorkmates = new ArrayList<>();

        this.getWorkmates();
        this.configureRecyclerView();
        this.configureOnClickRecyclerView();

        return result;
    }

    private void getWorkmates(){
        UserHelper.getAllUsers().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        User workmate = document.toObject(User.class);
                        mWorkmates.add(workmate);
                        Log.e("getWorkmates", workmate.getUsername());
                    }
                    configureRecyclerView();
                }else {
                    Log.e("Workmate query error", task.getException().getMessage());
                }
            }
        });
    }

    ////////////////////
    // CONFIGURATIONS //
    ////////////////////

    private void configureRecyclerView(){
        // Configures the RecyclerView and its components
        this.mAdapter = new WorkmateAdapter(mWorkmates, getContext());
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // Click on an article in the list
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        /*
                        Intent intent = new Intent(getContext(), RestaurantCardActivity.class);
                        Type restaurantType = new TypeToken<Restaurant>() { }.getType();
                        String restaurant = mGson.toJson(mAdapter.getRestaurant(position), restaurantType);
                        intent.putExtra(Constants.RESTAURANT, restaurant);
                        startActivity(intent);
                        */
                    }
                });
    }
}
