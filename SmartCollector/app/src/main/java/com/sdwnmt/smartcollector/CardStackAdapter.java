package com.sdwnmt.smartcollector;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;

import com.sdwnmt.smartcollector.Modal.PlotList;

import java.util.List;

public class CardStackAdapter extends FragmentStatePagerAdapter {
    Context context;
    FragmentManager fm;
    LayoutInflater inflater;
    List<PlotList> plotList;

    public CardStackAdapter(FragmentManager fm, Context context, List<PlotList> plotList) {
        super(fm);
        this.context = context;
        this.fm = fm;
        this.plotList = plotList;

    }

    @Override
    public Fragment getItem(int position) {
        BlankFragment blankFragment = new BlankFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pid", plotList.get(position).getId());
        bundle.putString("Name", plotList.get(position).getPlotOwner());
        bundle.putString("Plot", plotList.get(position).getPlotNo());
        bundle.putString("Address", plotList.get(position).getAddress());
        blankFragment.setArguments(bundle);
        position += 1;

        return blankFragment;
    }


    @Override
    public int getCount() {
        return plotList.size();
    }
}
