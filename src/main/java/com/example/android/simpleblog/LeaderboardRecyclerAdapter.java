package com.example.android.simpleblog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerAdapter.ViewHolder> {

    private List<BlogPost> blog_list;
    private List<Users> user_list;
    public Context context;
    public  int place=1;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    LeaderboardRecyclerAdapter(List<BlogPost> blog_list, List<Users> user_list){

        this.blog_list = blog_list;
        this.user_list = user_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if (firebaseAuth.getCurrentUser() != null) {

            holder.setIsRecyclable(false);

            final String blogPostId = blog_list.get(position).BlogPostId;


            final String event_name =blog_list.get(position).getEvent_name();

            String image_url = blog_list.get(position).getImage_url();
            String thumbUri = blog_list.get(position).getImage_thumb();

            String userid= blog_list.get(position).getUser_id();
            String username = blog_list.get(position).getUser_name();




            holder.setUserData(username,userid);
            holder.setBlogImage(image_url, thumbUri);
            holder.setplace(position);





            //User Data will be retrieved here...




            //Get Likes Count

    firebaseFirestore.collection(event_name+"Posts").document(blogPostId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {


            float score = 0;

            score = documentSnapshot.getLong("likes");


            holder.updateLikesCount(score);


        }


    });






            //Get Likes


            //Likes Feature




        }
    }


    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;


        private ImageView blogImageView,blogPlaceimage;


        private TextView blogUserName,blogPlacecount;

        private TextView blogLikeCount;



        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }



        void setBlogImage(String downloadUri, String thumbUri){

            blogImageView = mView.findViewById(R.id.blog_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(blogImageView);

        }




        void setUserData(String name, final String userid){


            blogUserName = mView.findViewById(R.id.user_name);

            blogUserName.setText(name);





            blogUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewAccountActivity.class);
                    intent.putExtra("userid", userid);
                    context.startActivity(intent);
                }
            });
        }


        void setplace(int position){

            blogPlacecount= mView.findViewById(R.id.place);
            blogPlaceimage= mView.findViewById(R.id.place_image);



            switch (position){
                case 0:
                    String k;
                    k = position+1 + "st place ";
                    blogPlacecount.setText( k );
                    blogPlaceimage.setImageResource(R.drawable.gold_crown_android);
                    break;
                case 1:
                    String i = position+1 + "nd place ";
                    blogPlacecount.setText( i);
                    blogPlaceimage.setImageResource(R.drawable.silver_crown_android);
                    break;
                case 2:
                    String j = position+1 + "rd place ";
                    blogPlacecount.setText( j );
                    blogPlaceimage.setImageResource(R.drawable.bronze_crown_android);
                    break;
                default:
                    String l = position+1 + "th place ";
                    blogPlacecount.setText( l );

            }


        }

        public void updateLikesCount(float count){

            blogLikeCount = mView.findViewById(R.id.likes_count);
            blogLikeCount.setText( "Score:"+count );



        }

    }

}