package com.sdwnmt.smartcollector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sdwnmt.smartcollector.Modal.ACK.Acknowledgement;
import com.sdwnmt.smartcollector.Modal.PlotList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sdwnmt.smartcollector.R.*;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mcontext;
    private  List<PlotList> plotList;
    String TAG = "hello";
    UserSes userSes;
    private AlertDialog dialog;



    public DataAdapter(Context mcontext,List<PlotList> plotList) {
        this.plotList = plotList;
        this.mcontext = mcontext;
        userSes = new UserSes(mcontext);
        setHasStableIds(true);

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
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.tv4.getText().equals("1")){
                        revertChanges(i, viewHolder);
                }else{
                    Toast.makeText(mcontext, "Not yet Visited", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return plotList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv1,tv2,tv3,tv4;
            ConstraintLayout li;
            ImageView img;
            CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(id.plot_no);
            tv2= itemView.findViewById(id.plot);
            tv3 = itemView.findViewById(id.name);
            tv4 = itemView.findViewById(id.cardFlag);
//            li = itemView.findViewById(id.li);
            img = itemView.findViewById(id.img);
            cardView = itemView.findViewById(id.Card);
        }
    }

    private void revertChanges(final int pos, final ViewHolder viewHolder){
        Button b1,b2,b3;
        android.app.AlertDialog.Builder malert = new android.app.AlertDialog.Builder(mcontext);
        LayoutInflater layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v1 = layoutInflater.inflate(R.layout.fragment_revert_modal, null);
        b1 = v1.findViewById(R.id.collect);
        b2 = v1.findViewById(R.id.notcollected);
        b3 = v1.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Time",getCurrDate());
//                Log.e("Time",time.toString());
                if(BlankFragment.isConnected) {
                    updateData("1", pos, viewHolder);
                }else{
                    revertOffline(plotList.get(pos).getId(),"1");
                    viewHolder.img.setImageResource(R.drawable.greendust);
                    viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#CAFFCA"));
                    dialog.dismiss();
                    Log.e("revertOffline","saved offline");
                }
//                layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.collectedcard));
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BlankFragment.isConnected) {
                    updateData("0", pos, viewHolder);
                }else{
                    revertOffline(plotList.get(pos).getId(),"0");
                    viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFD2D2"));
                    viewHolder.img.setImageResource(drawable.reddust);
                    dialog.dismiss();
                    Log.e("revertOffline","saved offline");
                }
            }
        });
        malert.setView(v1);
        dialog = malert.create();
        dialog.show();
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void removeItem(int pos){
        plotList.remove(pos);
        notifyItemRemoved(pos);
        notifyDataSetChanged();
    }


    private void updateData(final String resp, int pos, final ViewHolder viewHolder){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Acknowledgement> call = apiInterface.collectedGarbage(userSes.getWorkerid(), userSes.getToken(),plotList.get(pos).getId(), resp, BlankFragment.lat.toString(), BlankFragment.log.toString(),getCurrDate(),getCurrTime());
        call.enqueue(new Callback<Acknowledgement>() {
            @Override
            public void onResponse(Call<Acknowledgement> call, Response<Acknowledgement> response) {
                try{
//                        assert response.body() != null;
//                        Log.e("response", response.body().getResponse());
                    try {
                        if(response.body().getSuccess().equals("1")){
                            if(resp.equals("1")){
                                viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#CAFFCA"));
                                viewHolder.img.setImageResource(drawable.greendust);
                            }
                            if(resp.equals("0")){
                                viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFD2D2"));
                                viewHolder.img.setImageResource(drawable.reddust);
                            }
                            dialog.dismiss();
                            Toast.makeText(mcontext, "Changes saved.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mcontext,"Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(mcontext, "something went wrong..please try again", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.e("collectData",e.toString());
                }
            }

            @Override
            public void onFailure(Call<Acknowledgement> call, Throwable t) {
                Log.e("GarbageCollectedFailed", t.toString());
//               Toast.makeText(getContext(), "Please Check your Internet Connection and try again..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void revertOffline(String pid, String res){
        FileOutputStream fileoutputStream = null;
        try {
            fileoutputStream = mcontext.openFileOutput("revertData.txt", Context.MODE_APPEND);
            fileoutputStream.write((pid + "`" + res + "`" + BlankFragment.lat.toString() + "`" + BlankFragment.log.toString() + "`" + getCurrTime() + "~").getBytes());
            fileoutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String d = simpleDateFormat.format(date);
        return d;
    }

    private String getCurrTime(){
        Date time = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        String Time = sdf.format(time);
        return Time;
    }
}
