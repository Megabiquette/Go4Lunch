package com.albanfontaine.go4lunch.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.albanfontaine.go4lunch.Controllers.RestaurantCardActivity;
import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class WorkmateViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.workmates_item_avatar) ImageView mAvatar;
    @BindView(R.id.workmates_item_name) TextView mText;

    public WorkmateViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithWorkmate(User workmate, Context context){
        if(workmate.getAvatar() != null){
            Picasso.with(context).load(workmate.getAvatar()).transform(new CropCircleTransformation()).into(mAvatar);

        }
        // Display text according to the activity
        if(context instanceof RestaurantCardActivity){ // Restaurant card
            mText.setText(context.getResources().getString(R.string.restaurant_card_recycler_item_workmate_name_is_joining, workmate.getUsername()));
        } else { // Workmates fragment
            if(workmate.getRestaurantChosen() != null){
                mText.setText(context.getResources().getString(R.string.workmate_recycler_view_eating_at, workmate.getUsername(), workmate.getRestaurantChosen()));
            } else {
                mText.setText(context.getResources().getString(R.string.workmate_hasnt_decided, workmate.getUsername()));
                mText.setTypeface(null, Typeface.ITALIC);
                mText.setTextColor(Color.parseColor("#D3D3D3"));
            }
        }
    }

}
