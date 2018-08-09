package com.example.android.simpleblog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comments> commentsList;
    private List<Users> user_list;
    private TextView CommentUserName;
    private CircleImageView CommentUserImage;

    public Context context;

    public CommentsRecyclerAdapter(List<Comments> commentsList){

        this.commentsList = commentsList;


    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String commentMessage = commentsList.get(position).getMessage();
        String username = commentsList.get(position).getUser_name();
        String image = commentsList.get(position).getUser_image();
        String time_stamp =commentsList.get(position).getTimestamp();
        holder.setUserData(username, image,time_stamp);
        holder.setComment_message(commentMessage);

    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message,comment_time;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setComment_message(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }
        public void setUserData(String name, String image,String time_stamp){

            CommentUserImage = mView.findViewById(R.id.comment_image);
            CommentUserName = mView.findViewById(R.id.comment_username);
            comment_time = mView.findViewById(R.id.time_stamp);

            CommentUserName.setText(name);
            comment_time.setText("Posted at "+time_stamp);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(CommentUserImage);

        }

    }

}
