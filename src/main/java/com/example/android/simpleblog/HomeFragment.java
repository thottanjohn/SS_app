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
import android.widget.Toast;

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
public class HomeFragment extends Fragment {

    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    private List<Users> user_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private BlogRecyclerAdapter blogRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    private Button search_btn;
    private EditText search_text;
    private String search,event_name;
    private List<String> myList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        blog_list = new ArrayList<>();
        user_list = new ArrayList<>();
        blog_list_view = view.findViewById(R.id.blog_list_view);
        search_btn = view.findViewById(R.id.search_btn);
        search_text = view.findViewById(R.id.search_txt);


        firebaseAuth = FirebaseAuth.getInstance();

        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list,user_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        blog_list_view.setAdapter(blogRecyclerAdapter);
        blog_list_view.setHasFixedSize(true);


        if (firebaseAuth.getCurrentUser() != null) {

            if (getArguments() != null) {
                event_name = getArguments().getString("current_event_name");

            }



            firebaseFirestore = FirebaseFirestore.getInstance();


            Query firstQuery = firebaseFirestore.collection(event_name+"Posts").orderBy("timestamp", Query.Direction.DESCENDING);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (!documentSnapshots.isEmpty()) {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED  ) {

                                String blogPostId = doc.getDocument().getId();
                                final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                blog_list.add(blogPost);

                                blogRecyclerAdapter.notifyDataSetChanged();



                            }else if(doc.getType() == DocumentChange.Type.MODIFIED ){
                                String blogPostId = doc.getDocument().getId();
                                final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                blog_list.remove(blogPost);
                                blog_list.add(blogPost);
                                blogRecyclerAdapter.notifyDataSetChanged();

                            }else if(doc.getType() == DocumentChange.Type.REMOVED){
                                String blogPostId = doc.getDocument().getId();
                                final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                blog_list.remove(blogPost);
                                blogRecyclerAdapter.notifyDataSetChanged();

                            }

                        }
                    }
                }
            });
           search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    blog_list.clear();
                    search = search_text.getText().toString();
                    Query secondQuery = firebaseFirestore.collection(event_name+"Posts").whereGreaterThanOrEqualTo("user_name",search);




                    secondQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            if (!documentSnapshots.isEmpty()) {
                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                    if (doc.getType() == DocumentChange.Type.ADDED  ) {

                                        String blogPostId = doc.getDocument().getId();
                                        final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                        blog_list.add(blogPost);

                                        blogRecyclerAdapter.notifyDataSetChanged();



                                    }else if(doc.getType() == DocumentChange.Type.MODIFIED ){
                                        String blogPostId = doc.getDocument().getId();
                                        final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                        blog_list.remove(blogPost);
                                        blog_list.add(blogPost);
                                        blogRecyclerAdapter.notifyDataSetChanged();

                                    }else if(doc.getType() == DocumentChange.Type.REMOVED){
                                        String blogPostId = doc.getDocument().getId();
                                        final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                        blog_list.remove(blogPost);
                                        blogRecyclerAdapter.notifyDataSetChanged();

                                    }

                                }
                            }
                        }
                    });


                }


            });
        }
        return view;
    }
}

            // Inflate the layout for this fragment

/*
            blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {

                        loadMorePost();

                    }

                }
            });

            Query firstQuery = firebaseFirestore.collection("GreenVibesPosts").orderBy("timestamp", Query.Direction.DESCENDING);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        if (isFirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            blog_list.clear();

                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                                final String userid = doc.getDocument().getString("user_id");
                                firebaseFirestore.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()) {

                                            Users user = task.getResult().toObject(Users.class).withId(userid);

                                            if (isFirstPageFirstLoad) {

                                                user_list.add(user);

                                                blog_list.add(blogPost);

                                            } else {

                                                user_list.add(0, user);
                                                blog_list.add(0, blogPost);

                                            }


                                            blogRecyclerAdapter.notifyDataSetChanged();

                                        } else {

                                            //Firebase Exception

                                        }

                                    }
                                });


                            }
                        }

                        isFirstPageFirstLoad = false;

                    }

                }

            });

        }


        return view;
    }

    public void loadMorePost() {


        Query nextQuery = firebaseFirestore.collection("GreenVibesPosts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);

        nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId = doc.getDocument().getId();
                            final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                            final String userid = doc.getDocument().getString("user_id");
                            firebaseFirestore.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {

                                        Users user = task.getResult().toObject(Users.class).withId(userid);
                                        user_list.add(user);

                                        blog_list.add(blogPost);

                                        blogRecyclerAdapter.notifyDataSetChanged();


                                    } else {

                                        //Firebase Exception

                                    }

                                }
                            });

                        }

                    }
                }

            }
        });

    }  */







