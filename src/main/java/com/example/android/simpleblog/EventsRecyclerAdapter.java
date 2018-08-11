package com.example.android.simpleblog;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {

    private List<Events> events_list;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Context context;

    EventsRecyclerAdapter(List<Events> events_list){

        this.events_list = events_list;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_item, parent, false);

        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (firebaseAuth.getCurrentUser() != null) {

            holder.setIsRecyclable(false);
            holder.event_enter_btn.setEnabled(false);
            holder.event_enter_btn.setVisibility(View.GONE);
            holder.event_explore_btn.setEnabled(false);
            holder.event_explore_btn.setVisibility(View.GONE);
            final String eventId = events_list.get(position).EventId;
            final String currentUserId = firebaseAuth.getCurrentUser().getUid();


            final String event_name = events_list.get(position).getEvent_name();


            String event_image = events_list.get(position).getEvent_image();


            Date start_date = events_list.get(position).getStart_date();
            Date end_date = events_list.get(position).getEnd_date();
            Date current_date =new Date(System.currentTimeMillis());

            long dif =start_date.getTime() - current_date.getTime();
            if(dif<=0) {
                holder.event_enter_btn.setEnabled(true);
                holder.event_enter_btn.setVisibility(View.VISIBLE);
                holder.event_explore_btn.setVisibility(View.VISIBLE);
                holder.event_explore_btn.setEnabled(true);
                holder.event_enter_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    Intent intent = new Intent(context, NewPostActivity.class);
                    intent.putExtra("event_name", event_name);
                    context.startActivity(intent);
                }
            });
                holder.event_explore_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PageActivity.class);
                        intent.putExtra("event_name", event_name);
                        context.startActivity(intent);
                    }
                });






                holder.setevent(event_name);
                holder.seteventimage(event_image);
            }else{
                holder.event_enter_btn.setEnabled(false);
                holder.event_enter_btn.setVisibility(View.GONE);
                holder.event_explore_btn.setEnabled(false);
                holder.event_explore_btn.setVisibility(View.GONE);
                event_image="no_image";
                holder.setevent("COMING SOON");
                holder.seteventimage(event_image);




            }


        }
    }


    @Override
    public int getItemCount() {
        return events_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private View mView;
        private Button event_enter_btn,event_explore_btn;
        private TextView event_name;
        private ImageView event_image;



        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            event_enter_btn =mView.findViewById(R.id.event_btn_enter);
            event_explore_btn =mView.findViewById(R.id.event_btn_explore);


        }

        void setevent(String eventname) {
            event_name = mView.findViewById(R.id.event_name);
            event_name.setText(eventname);







        }
        void seteventimage(String eventimage){
            if(eventimage.equals("no_image")){

            }
            else{
                event_image=mView.findViewById(R.id.event_image);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.image_placeholder);

                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(eventimage).into(event_image);



            }

        }
    }
}

