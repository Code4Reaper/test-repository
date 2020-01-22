package com.sdwnmt.smartcollector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
    private TextView Wname,zone,route,vehicle,email;
    private Button btn1,btn2;
    public UserSes userSes;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        btn1 = view.findViewById(R.id.start_trip);
        btn2 = view.findViewById(R.id.logout);
        Wname = view.findViewById(R.id.Wname);
        zone = view.findViewById(R.id.zone);
        route = view.findViewById(R.id.route);
        vehicle = view.findViewById(R.id.vehicle);
        email = view.findViewById(R.id.email);
        try{
            userSes = new UserSes(getContext());

        }catch (NullPointerException npe){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }



        setViewData();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNewActivity();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSes.removeUser();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                try{
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                }catch (NullPointerException ntp){}
            }
        });
        return view;
    }
    private void moveToNewActivity() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

    }

    private void setViewData(){
        try {
            Wname.setText(userSes.getFullname());
            zone.setText(userSes.getZoneid());
            route.setText(userSes.getRoute());
            email.setText(userSes.getEmail());
            vehicle.setText(userSes.getVehicle());
        }catch (Exception e){}




    }

}
