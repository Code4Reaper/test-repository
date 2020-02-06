package com.sdwnmt.smartcollector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.sdwnmt.smartcollector.Modal.PlotList;

import java.util.List;

import static com.sdwnmt.smartcollector.R.*;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mcontext;
    private  List<PlotList> plotList;
    String TAG = "hello";



    public DataAdapter(Context mcontext,List<PlotList> plotList) {
        this.plotList = plotList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout.card,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        Log.d(TAG,"called");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.tv2.setText("Address :- "+plotList.get(i).getAddress());
        viewHolder.tv1.setText("Plot No. :- "+plotList.get(i).getPlotNo());
        viewHolder.tv3.setText("Owner :- "+plotList.get(i).getPlotOwner());

    }

    @Override
    public int getItemCount() {
        return plotList.size();
    }





    public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv1,tv2,tv3;
            ConstraintLayout li;
            ImageView img;
            CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(id.plot_no);
            tv2= itemView.findViewById(id.plot);
            tv3 = itemView.findViewById(id.name);
//            li = itemView.findViewById(id.li);
            img = itemView.findViewById(id.img);
            cardView = itemView.findViewById(id.Card);
        }
    }

    public void removeItem(int pos){
        plotList.remove(pos);
        notifyItemRemoved(pos);
        notifyDataSetChanged();
    }
}
