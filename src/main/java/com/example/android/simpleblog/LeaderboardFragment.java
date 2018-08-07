package com.example.android.simpleblog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
public class LeaderboardFragment extends Fragment {


    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    private List<Users> user_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private LeaderboardRecyclerAdapter LeaderboardRecyclerAdapter;


    public LeaderboardFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


            View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

            blog_list = new ArrayList<>();
            user_list = new ArrayList<>();
            blog_list_view = view.findViewById(R.id.blog_list_view);

            firebaseAuth = FirebaseAuth.getInstance();
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(container.getContext(),2 );


        blog_list_view.setLayoutManager(layoutManager);
            LeaderboardRecyclerAdapter = new  LeaderboardRecyclerAdapter (blog_list, user_list);

            blog_list_view.setAdapter( LeaderboardRecyclerAdapter);
            blog_list_view.setHasFixedSize(true);

            if (firebaseAuth.getCurrentUser() != null) {


                firebaseFirestore = FirebaseFirestore.getInstance();

                Query firstQuery = firebaseFirestore.collection("GreenVibesPosts").orderBy("likes",Query.Direction.DESCENDING);
                firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (!documentSnapshots.isEmpty()) {
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED  ) {

                                    String blogPostId = doc.getDocument().getId();
                                    final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                    blog_list.add(blogPost);

                                    LeaderboardRecyclerAdapter.notifyDataSetChanged();



                                }else if(doc.getType() == DocumentChange.Type.MODIFIED ){
                                    String blogPostId = doc.getDocument().getId();
                                    final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                    blog_list.remove(blogPost);
                                    blog_list.add(blogPost);
                                    LeaderboardRecyclerAdapter.notifyDataSetChanged();

                                }else if(doc.getType() == DocumentChange.Type.REMOVED){
                                    String blogPostId = doc.getDocument().getId();
                                    final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                    blog_list.remove(blogPost);
                                    LeaderboardRecyclerAdapter.notifyDataSetChanged();

                                }

                            }
                        }
                    }
                });

            }

            // Inflate the layout for this fragment
            return view;
        }



}
