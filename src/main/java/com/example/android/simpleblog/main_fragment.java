package com.example.android.simpleblog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.

 */
public class main_fragment extends Fragment {

    private Button start_btn;
    private FirebaseAuth mAuth;

    public main_fragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser().getUid() == null) {

            sendToLogin();
        } else {

            start_btn = view.findViewById(R.id.btn_start);

            start_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new Eventsfragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });



        }
        return view;
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);


    }

}
