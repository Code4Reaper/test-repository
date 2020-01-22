package com.sdwnmt.smartcollector;


import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BlankFragment extends Fragment {

    private TextView name,plot,address;
    Button btn1,btn2;
    ConstraintLayout layout;
    public BlankFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_blank, container, false);

        layout = view.findViewById(R.id.layout);
        name = view.findViewById(R.id.name);
        plot = view.findViewById(R.id.plot);
        address = view.findViewById(R.id.address);
        btn1 = view.findViewById(R.id.button4);
        btn2 = view.findViewById(R.id.button5);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),getArguments().getString("Name"), Toast.LENGTH_SHORT).show();
//                layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.collectedcard));

            }
        });



        assert getArguments() != null;
        name.setText(getArguments().getString("Name"));
        plot.setText(getArguments().getString("Plot"));
        address.setText(getArguments().getString("Address"));
        return view;
    }
}
