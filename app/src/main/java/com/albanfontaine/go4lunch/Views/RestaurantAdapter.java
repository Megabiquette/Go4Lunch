package com.albanfontaine.go4lunch.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albanfontaine.go4lunch.Models.Restaurant;
import com.albanfontaine.go4lunch.R;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<Restaurant> mRestaurants;
    private Context mContext;

    public RestaurantAdapter(List<Restaurant> restaurants, Context context){
        this.mRestaurants = restaurants;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create ViewHolder and inflate its layout
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_list_item, parent, false);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder viewHolder, int position) {
        viewHolder.updateWithRestaurant(this.mRestaurants.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return this.mRestaurants.size();
    }

    public Restaurant getRestaurant(int position){
        return this.mRestaurants.get(position);
    }
}
