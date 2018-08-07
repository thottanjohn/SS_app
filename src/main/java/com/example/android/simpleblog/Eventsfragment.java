package com.example.android.simpleblog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.

 */
public class Eventsfragment extends Fragment {


    private Button enterbtn,explorebtn;
    public Eventsfragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_eventsfragment, container, false);
        enterbtn  = view.findViewById(R.id.btn_enter);
        explorebtn =  view.findViewById(R.id.btn_explore);

        enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( getActivity(),NewPostActivity.class));
            }
        });

        explorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PageActivity.class));
            }
        });
        return  view;
    }

}
