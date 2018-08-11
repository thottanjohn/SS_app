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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class UserBlogAdapter extends RecyclerView.Adapter<UserBlogAdapter.ViewHolder> {

    private List<BlogPost> blog_list;
    private List<Users> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    UserBlogAdapter(List<BlogPost> blog_list, List<Users> user_list){

        this.blog_list = blog_list;
        this.user_list = user_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_blog_list, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (firebaseAuth.getCurrentUser() != null) {

            holder.setIsRecyclable(false);

            final String blogPostId = blog_list.get(position).BlogPostId;
            final String currentUserId = firebaseAuth.getCurrentUser().getUid();

            String desc_data = blog_list.get(position).getDesc();
            holder.setDescText(desc_data);

            String image_url = blog_list.get(position).getImage_url();
            String thumbUri = blog_list.get(position).getImage_thumb();
            final String userid= blog_list.get(position).getUser_id();


            String username = blog_list.get(position).getUser_name();
            String image = blog_list.get(position).getUser_image();
            final String event_name = blog_list.get(position).getEvent_name();

            holder.setUserData(username, image,userid);
            holder.setBlogImage(image_url, thumbUri);

            if(userid.equals(currentUserId)){

                holder.delete_btn.setEnabled(true);
                holder.delete_btn.setVisibility(View.VISIBLE);
            }else{

                holder.delete_btn.setEnabled(false);
                holder.delete_btn.setVisibility(View.INVISIBLE);

            }
            firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    try{
                        if (!documentSnapshots.isEmpty()) {

                            int count = documentSnapshots.size();

                            holder.updateLikesCount(count);



                        } else {

                            holder.updateLikesCount(0);


                        }

                    }   catch (Exception e2){
                        Toast.makeText(context, e2.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });

            //User Data will be retrieved here...


            try {
                String dateString = blog_list.get(position).getTimestamp();

                holder.setTime(dateString);
            } catch (Exception e) {

                Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            //Get Likes Count
            firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Comments")
                    .addSnapshotListener( new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if (!documentSnapshots.isEmpty()) {

                                int counts = documentSnapshots.size();
                                holder.updateCommentCount(counts);
                            }
                        }


                    });



            //Get Likes


            //Likes Feature


            holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent commentIntent = new Intent(context, CommentsActivity.class);
                    commentIntent.putExtra("blog_post_id", blogPostId);
                    commentIntent.putExtra("event_name", event_name);
                    context.startActivity(commentIntent);

                }
            });
            holder.delete_progress_bar.setVisibility(ProgressBar.INVISIBLE);

            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.delete_progress_bar.setVisibility(ProgressBar.VISIBLE);
                    firebaseFirestore.collection(event_name+"Posts").document(blogPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            blog_list.remove(position);
                            holder.delete_progress_bar.setVisibility(ProgressBar.INVISIBLE);

                        }
                    });

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;

        private TextView blogUserName;
        private CircleImageView blogUserImage;


        private TextView blogLikeCount;

        private ImageView blogCommentBtn;

        private Button delete_btn;

        private ProgressBar delete_progress_bar;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
            delete_btn =mView.findViewById(R.id.delete_btn);
            delete_progress_bar=mView.findViewById(R.id.delete_progress);

        }

        void setDescText(String descText){

            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);

        }

        void setBlogImage(String downloadUri, String thumbUri){

            blogImageView = mView.findViewById(R.id.blog_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(blogImageView);

        }

        public void setTime(String date) {

            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }

        void setUserData(String name, String image, final String userid){

            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogUserName = mView.findViewById(R.id.blog_user_name);

            blogUserName.setText(name);



            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(blogUserImage);

            blogUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewAccountActivity.class);
                    intent.putExtra("userid", userid);
                    context.startActivity(intent);
                }
            });
        }


        public void updateCommentCount(int counts){

            blogLikeCount = mView.<TextView>findViewById(R.id.blog_comment_count);
            String k=counts + " Comments";
            blogLikeCount.setText(k);

        }
        public void updateLikesCount(int count){

            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            String k=count + " Comments";
            blogLikeCount.setText(k);

        }
    }



}