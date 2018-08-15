package com.example.android.simpleblog;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
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

import java.text.SimpleDateFormat;
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
                holder.initUI();
                Date currentdate =new Date(System.currentTimeMillis());
                holder.countDownDayStart(end_date.getTime()-currentdate.getTime());

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
                holder.initUI();

                long st_time =current_date.getTime();
                long end_time =start_date.getTime();
                long diff =end_time-st_time;
                holder.countDownStart(diff);



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
        private ConstraintLayout cardview;

        private String EVENT_DATE_TIME = "2018-12-31 10:30:00";
        private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        private TextView tv_days, tv_hour, tv_minute, tv_second;
        private Handler handler = new Handler();
        private Runnable runnable;


        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            event_enter_btn =mView.findViewById(R.id.event_btn_enter);
            event_explore_btn =mView.findViewById(R.id.event_btn_explore);
    cardview=mView.findViewById(R.id.cardView);

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
        private void initUI() {

            tv_days = mView.findViewById(R.id.tv_days);
            tv_hour =  mView.findViewById(R.id.tv_hour);
            tv_minute =  mView.findViewById(R.id.tv_minute);
            tv_second =  mView.findViewById(R.id.tv_second);
        }
        private void countDownStart( long dif) {



            new CountDownTimer(dif, 1000) {

                public void onTick(long millisUntilFinished) {
                    long Days = (millisUntilFinished) / (24 * 60 * 60 * 1000);
                    long Hours = (millisUntilFinished)/ (60 * 60 * 1000) % 24;
                    long Minutes = (millisUntilFinished)/ (60 * 1000) % 60;
                    long Seconds = (millisUntilFinished)/ 1000 % 60;
                    //
                    tv_days.setText(String.format("%02d", Days));
                    tv_hour.setText(String.format("%02d", Hours));
                    tv_minute.setText(String.format("%02d", Minutes));
                    tv_second.setText(String.format("%02d", Seconds));
                }

                public void onFinish() {
cardview.setVisibility(View.GONE);
                }
            }.start();



        }


        public void countDownDayStart(long l) {


        }
    }
}

