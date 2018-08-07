
package com.example.android.simpleblog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.

 */
public class StatsFragment extends Fragment {
    private String blog_user_id;


    public StatsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        if (getArguments() != null) {
            blog_user_id = getArguments().getString("blog_user_Id");
        }
        // Inflate the layout for this fragment
        return view;


        }




}
