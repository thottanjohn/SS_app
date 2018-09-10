package com.example.android.simpleblog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
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

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    private List<Users> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public int likes;

    BlogRecyclerAdapter(List<BlogPost> blog_list, List<Users> user_list){
        this.blog_list = blog_list;
        this.user_list = user_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
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
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();


        String desc_data = blog_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = blog_list.get(position).getImage_url();
        String thumbUri = blog_list.get(position).getImage_thumb();

        String dateString =blog_list.get(position).getTimestamp();
        final String userid   =blog_list.get(position).getUser_id();
        final float currenttotal =blog_list.get(position).getTotal();
        float likes =blog_list.get(position).getLikes();


        String username = blog_list.get(position).getUser_name();
        String image = blog_list.get(position).getUser_image();
        final String event_name =blog_list.get(position).getEvent_name();

        holder.setUserData(username, image,userid);

        holder.setBlogImage(image_url, thumbUri);

        holder.setTime(dateString);

       




        //User Data will be retrieved here...

/*
        try {
            long millisecond = blog_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
*/

        //Get Likes Count


                firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Comments")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                try {
                                if (!documentSnapshots.isEmpty()) {

                                    int counts = documentSnapshots.size();
                                    holder.updateCommentCount(counts);
                                } else {

                                    holder.updateCommentCount(0);

                                }
                            }catch (Exception e1){
                                  //  Toast.makeText(context, e1.getMessage(), Toast.LENGTH_LONG).show();


                                }


                        }
            });




                firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    try{
                    if (!documentSnapshots.isEmpty()) {
                        int count = documentSnapshots.size();


                        holder.updateLikesCount(count);
                        holder.updatedata(count,blogPostId,event_name);


                    }

                } catch (Exception e2){
                        // Toast.makeText(context, e2.getMessage(), Toast.LENGTH_LONG).show();

                    }
                    }


                });





            //Get Likes
            firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
    try {
        if (documentSnapshot.exists()) {

        holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_accent));

        } else {

        holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));

     }
    }catch (Exception e1){

//Toast.makeText(context, e2.getMessage(), Toast.LENGTH_LONG).show();

            }
                }
            });

            //Likes Feature
            holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult().exists()) {

                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Likes").document(currentUserId).set(likesMap);

                            } else {

                                firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Likes").document(currentUserId).delete();

                            }

                        }
                    });
                }
            });
/*
            firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Rating").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    try {
                        if (documentSnapshot.exists()) {

                            float rating = documentSnapshot.getLong("rating");

                            holder.setRating(rating);

                        } else {

                            holder.setRating(0.0f);
                        }
                    } catch (Exception e1) {

//Toast.makeText(context, e2.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });

            holder.blog_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, final float rating,
                                            boolean fromUser) {

                    firebaseFirestore.collection(event_name+"Posts/" + blogPostId + "/Rating").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.getResult().exists()) {
                                float rating_value = rating;
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());
                                likesMap.put("rating", rating_value);

                                firebaseFirestore.collection(event_name + "Posts/" + blogPostId + "/Rating").document(currentUserId).set(likesMap);
                                DocumentReference docref=  firebaseFirestore.collection(event_name+"Posts").document(blogPostId);
                                docref.update("total",currenttotal+rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                            } else {
                                float oldrating =task.getResult().getLong("rating");
                                float rating_value = rating;
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());
                                likesMap.put("rating", rating_value);

                                firebaseFirestore.collection(event_name + "Posts/" + blogPostId + "/Rating").document(currentUserId).set(likesMap);
                                firebaseFirestore.collection(event_name + "Posts/" + blogPostId + "/Rating").document(currentUserId).set(likesMap);
                                DocumentReference docref=  firebaseFirestore.collection(event_name+"Posts").document(blogPostId);
                                docref.update("total",currenttotal+rating-oldrating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                            }





                        }
                    });
                }
            });

*/

            holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent commentIntent = new Intent(context, CommentsActivity.class);
                    commentIntent.putExtra("blog_post_id", blogPostId);
                    commentIntent.putExtra("event_name", event_name);
                    context.startActivity(commentIntent);

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

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;
        private RatingBar blog_rating_bar;

        private ImageView blogCommentBtn;


        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
//            blog_rating_bar = mView.findViewById(R.id.blog_rating_bar);

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

        public void updateLikesCount(int count){

            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            String k= "Likes: "+count;
            blogLikeCount.setText(k);

        }
        public void updateCommentCount(int counts){
            String k=counts + " Comments";
            blogLikeCount = mView.findViewById(R.id.blog_comment_count);
            blogLikeCount.setText(k);

        }
       private void updatedata(int count,String blogPostId,String event_name) {
            DocumentReference docref=  firebaseFirestore.collection(event_name+"Posts").document(blogPostId);
            docref.update("likes",count).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


        }

        public void setRating(float rating) {


            blog_rating_bar.setRating(rating);


        }
    }

}
