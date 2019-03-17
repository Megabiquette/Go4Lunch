package com.albanfontaine.go4lunch.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albanfontaine.go4lunch.Models.User;
import com.albanfontaine.go4lunch.R;

import java.util.List;

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateViewHolder> {
    private List<User> mWorkmates;
    private Context mContext;

    public WorkmateAdapter(List<User> workmates, Context context){
        this.mWorkmates = workmates;
        this.mContext = context;
    }

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create ViewHolder and inflate its layout
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.workmates_item, parent, false);
        return new WorkmateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder viewHolder, int position) {
        viewHolder.updateWithWorkmate(this.mWorkmates.get(position), mContext);
    }

    @Override
    public int getItemCount() { return this.mWorkmates.size(); }

    public User getUser(int position){ return this.mWorkmates.get(position); }
}
