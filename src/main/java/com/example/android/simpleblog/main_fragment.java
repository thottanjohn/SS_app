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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static java.lang.Math.abs;
import static java.lang.Math.min;


/**
 * A simple {@link Fragment} subclass.

 */
public class main_fragment extends Fragment {

    private Button start_btn;
    private FirebaseAuth mAuth;
    private static ViewPager mPager;
    private MyAdapter myAdapter;
    public Date mDate;
    private TextView event_title;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.automn,R.drawable.christmas_composition,R.drawable.girlsoloselfie};
    private ArrayList<String> XMENArray = new ArrayList<String>();
    private ArrayList<Integer> XMENintegerArray = new ArrayList<Integer>();
    public main_fragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {

            sendToLogin();
        } else {

            start_btn = view.findViewById(R.id.btn_start);
            mPager =  view.findViewById(R.id.pager);
            myAdapter= new MyAdapter(getActivity(),XMENArray,XMENintegerArray);
            event_title =view.findViewById(R.id.event_title);
            event_title.setVisibility(View.VISIBLE);
            mPager.setAdapter(myAdapter);
            CircleIndicator indicator =  view.findViewById(R.id.indicator);
            indicator.setViewPager(mPager);
            final Date current_date =new Date(System.currentTimeMillis());
            mDate =new Date(current_date.getTime()+ 2628000000L);
            final Date Jdate =mDate;


            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final Query firstQuery = firebaseFirestore.collection("Events").orderBy("start_date");
            try {
                firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        try {
                            if (!documentSnapshots.isEmpty()) {
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                                    boolean over = doc.getDocument().getBoolean("over");
                                    if (over) {

                                        Date end_date = doc.getDocument().getDate("end_date");


                                        long diff = abs(current_date.getTime() - end_date.getTime());

                                        if (mDate.getTime() > diff) {

                                            mDate = end_date;

                                        }
                                    }
                                }

                            }
                        }


                            catch (Exception e1){
                            sendToLogin();
                        }


                        if(Jdate != mDate) {


                            try {
                                Query StQuery = firebaseFirestore.collection("Events").whereEqualTo("end_date", mDate);
                                StQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                        try {

                                            if (!documentSnapshots.isEmpty()) {
                                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                                    String event_name = doc.getDocument().getString("event_name");
                                                    event_title.setText(event_name + " Winners");
                                                    Query SecondQuery = firebaseFirestore.collection(event_name + "Posts").orderBy("likes", Query.Direction.DESCENDING).limit(3);
                                                    SecondQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                                            if (!documentSnapshots.isEmpty()) {
                                                                for (int i = 0; i < 3; i++) {
                                                                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                                                                        String image_url = doc.getDocument().getString("image_url");

                                                                        XMENArray.add(image_url);
                                                                        myAdapter.notifyDataSetChanged();


                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });

                                                }
                                            }
                                        }catch (Exception e3){

                                        }
                                    }
                                });

                            }catch(Exception e2){

                            }
                        }

                        else {

                            event_title.setVisibility(View.INVISIBLE);
                            for (int i = 0; i < XMEN.length; i++) {

                                XMENintegerArray.add(XMEN[i]);
                                myAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });
            }catch (Exception e){
                sendToLogin();
            }





            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == XMEN.length) {
                        currentPage = 0;
                    }

                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 2500, 2500);





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
