package com.example.android.simpleblog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class PostFragment extends Fragment {
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    private List<Users> user_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private UserBlogAdapter userBlogAdapter;


    private  String blog_user_id;
    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        blog_list = new ArrayList<>();
        user_list = new ArrayList<>();
        blog_list_view = view.findViewById(R.id.user_blog_list_view);

        firebaseAuth = FirebaseAuth.getInstance();

        userBlogAdapter = new UserBlogAdapter(blog_list, user_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        blog_list_view.setAdapter(userBlogAdapter);
        blog_list_view.setHasFixedSize(true);

        if (firebaseAuth.getCurrentUser() != null)

        {


            if (getArguments() != null) {
            blog_user_id = getArguments().getString("blog_user_id");

        }



            firebaseFirestore = FirebaseFirestore.getInstance();

            Query firstQuery = firebaseFirestore.collection("GreenVibesPosts").whereEqualTo("user_id", blog_user_id);


          firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (!documentSnapshots.isEmpty()) {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED  ) {

                                String blogPostId = doc.getDocument().getId();
                                final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                blog_list.add(blogPost);

                               userBlogAdapter.notifyDataSetChanged();



                            }else if(doc.getType() == DocumentChange.Type.MODIFIED ){
                                String blogPostId = doc.getDocument().getId();
                                final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                blog_list.remove(blogPost);
                                blog_list.add(blogPost);
                                userBlogAdapter.notifyDataSetChanged();

                            }else if(doc.getType() == DocumentChange.Type.REMOVED){
                                String blogPostId = doc.getDocument().getId();
                                final BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                blog_list.remove(blogPost);
                                userBlogAdapter.notifyDataSetChanged();

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
