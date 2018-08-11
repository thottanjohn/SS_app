package com.example.android.simpleblog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.

 */
public class Eventsfragment extends Fragment {
    private RecyclerView events_list_view;
    private List<Events> events_list;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private EventsRecyclerAdapter EventsRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    private Button search_btn;
    private EditText search_text;
    private String search;
    private List<String> myList = new ArrayList<>();

    private Button enterbtn, explorebtn;

    public Eventsfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eventsfragment, container, false);

        events_list = new ArrayList<>();

        events_list_view = view.findViewById(R.id.event_list_view);


        firebaseAuth = FirebaseAuth.getInstance();

        EventsRecyclerAdapter = new EventsRecyclerAdapter(events_list);
        events_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        events_list_view.setAdapter(EventsRecyclerAdapter);
        events_list_view.setHasFixedSize(true);

        if (firebaseAuth.getCurrentUser() != null) {


            firebaseFirestore = FirebaseFirestore.getInstance();


            Query firstQuery = firebaseFirestore.collection("Events").orderBy("start_date", Query.Direction.DESCENDING);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (!documentSnapshots.isEmpty()) {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String event_Id = doc.getDocument().getId();
                                final Events events = doc.getDocument().toObject(Events.class).withId(event_Id);

                                events_list.add(events);

                                EventsRecyclerAdapter.notifyDataSetChanged();


                            } else if (doc.getType() == DocumentChange.Type.MODIFIED) {
                                String event_Id = doc.getDocument().getId();
                                final Events events = doc.getDocument().toObject(Events.class).withId(event_Id);
                                events_list.remove(events);
                                events_list.add(events);

                                EventsRecyclerAdapter.notifyDataSetChanged();

                            } else if (doc.getType() == DocumentChange.Type.REMOVED) {
                                String event_Id = doc.getDocument().getId();
                                final Events events = doc.getDocument().toObject(Events.class).withId(event_Id);
                                events_list.remove(events);


                                EventsRecyclerAdapter.notifyDataSetChanged();

                            }

                        }
                    }
                }
            });  }

       /* enterbtn  = view.findViewById(R.id.btn_enter);
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
        }); */
            return view;


    }
}
