package com.albanfontaine.go4lunch.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class WorkmateAdapter extends FirestoreRecyclerAdapter<User, WorkmateViewHolder> {
    private List<User> mWorkmates;
    private Context mContext;

    public WorkmateAdapter(@NonNull FirestoreRecyclerOptions<User> options){
        super(options);
    }

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new WorkmateViewHolder(LayoutInflater.from(mContext).inflate(R.layout.workmates_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkmateViewHolder viewHolder, int position, @NonNull User workmate) {
        viewHolder.updateWithWorkmate(workmate, mContext);
    }

    @Override
    public int getItemCount() {
        //return this.mWorkmates.size();
        return 0;
    }

    public User getUser(int position){ return this.mWorkmates.get(position); }
}
