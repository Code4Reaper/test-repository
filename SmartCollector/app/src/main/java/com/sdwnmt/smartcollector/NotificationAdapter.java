package com.sdwnmt.smartcollector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context mcontext;
    private List<String> notifyList;
    String TAG = "hello";


    public NotificationAdapter(Context mcontext, List<String> notifyList) {
        this.notifyList = notifyList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notificationcard, viewGroup, false);
        final NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(view);

        Log.d(TAG, "called");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tv.setText(notifyList.get(i));
    }


    @Override
    public int getItemCount() {
        return notifyList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.name);

        }
    }

}