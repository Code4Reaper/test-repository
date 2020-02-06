package com.sdwnmt.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdwnmt.demo.Modal.Coordinates;
import com.sdwnmt.demo.Modal.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Query;

import static android.content.Context.LOCATION_SERVICE;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    public static GoogleMap mMap;
    private FragmentActivity myContext;
    public LocationManager locationManager;
    public static double lon, lat;
    private UserSes userSes;
    private ScheduledExecutorService executorService;
    private Response resp;
    private String address, address2;
    private Bitmap smallMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_location, container, false);
        userSes = new UserSes(getContext());
        Gson gson = new Gson();
        Type type = new TypeToken<Response>(){}.getType();
        resp = gson.fromJson(userSes.getResp(),type);

        locationManager = (LocationManager) myContext.getSystemService(LOCATION_SERVICE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        } catch (NullPointerException npe) {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.location_icon);
        Bitmap b = bitmapDrawable.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        plotMarker();
    }
    private void plotMarker() {
        mMap.clear();
        try {
            final ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
             executorService = new ScheduledThreadPoolExecutor(1);
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Call<Coordinates> call = apiInterface.acquireCoordinates(resp.getToken(),resp.getLaneId(),resp.getPid());
                    call.enqueue(new Callback<Coordinates>() {
                        @Override
                        public void onResponse(Call<Coordinates> call, retrofit2.Response<Coordinates> response) {
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(Double.valueOf(response.body().getLatitude()),Double.valueOf(response.body().getLongitude()), 1);
                                address = addresses.get(0).getSubLocality();
                                address2 = addresses.get(0).getLocality();
                                //Toast.makeText(getContext(), address+" "+address2, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            mMap.getUiSettings().setAllGesturesEnabled(true);
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.valueOf(response.body().getLatitude()),Double.valueOf(response.body().getLongitude())))
                                    .title(response.body().getWname())
                                    .snippet(response.body().getMobile() + "\n" + address + "," + address2)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)))
                                    .showInfoWindow();
                            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(Double.valueOf(response.body().getLatitude()),Double.valueOf(response.body().getLongitude())),10),
                                    2000, null);
                        }

                        @Override
                        public void onFailure(Call<Coordinates> call, Throwable t) {
                            Toast.makeText(getContext(), "Please check your internet connection..", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            },0,60000, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            executorService.shutdown();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            executorService.shutdown();
        } catch (Exception e) {
        }
    }
}
