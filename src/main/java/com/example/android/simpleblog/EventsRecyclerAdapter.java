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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {

    private List<Events> events_list;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Context context;
    private ArrayList<String> Rules;

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
            holder.rules_spinner.setVisibility(View.GONE);
            holder.rules.setVisibility(View.GONE);
            final String eventId = events_list.get(position).EventId;
            Rules =events_list.get(position).getRules();
            final String currentUserId = firebaseAuth.getCurrentUser().getUid();


            final String event_name = events_list.get(position).getEvent_name();


            String event_image = events_list.get(position).getEvent_image();


            Date start_date = events_list.get(position).getStart_date();
            Date end_date = events_list.get(position).getEnd_date();
            Date current_date =new Date(System.currentTimeMillis());
            final boolean over = events_list.get(position).isOver();
            long dif =start_date.getTime() - current_date.getTime();
            if(dif<=0) {
                holder.event_enter_btn.setEnabled(true);
                holder.event_enter_btn.setVisibility(View.VISIBLE);
                holder.event_explore_btn.setVisibility(View.VISIBLE);
                holder.event_explore_btn.setEnabled(true);
                holder.rules_spinner.setVisibility(View.VISIBLE);
                holder.rules.setVisibility(View.VISIBLE);
                holder.initUI();
                Date currentdate =new Date(System.currentTimeMillis());
                long time =end_date.getTime()-currentdate.getTime();
                if (time<0){
                    holder.setevent("Contest has ended");
                    holder.updatedata( eventId);

                }
                else {
                    holder.countDownDayStart(end_date.getTime() - currentdate.getTime());

                    holder.event_enter_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, NewPostActivity.class);
                            intent.putExtra("event_name", event_name);

                            intent.putExtra("over", over);
                            context.startActivity(intent);
                        }
                    });
                    holder.event_explore_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, PageActivity.class);
                            intent.putExtra("event_name", event_name);
                            intent.putExtra("over", over);
                            context.startActivity(intent);
                        }
                    });

                holder.setevent(event_name);
                holder.seteventimage(event_image);
                holder.setRules(Rules);


                }
            }else{
                holder.event_enter_btn.setEnabled(false);
                holder.event_enter_btn.setVisibility(View.GONE);
                holder.event_explore_btn.setEnabled(false);
                holder.event_explore_btn.setVisibility(View.GONE);
                holder.rules_spinner.setVisibility(View.GONE);
                holder.rules.setVisibility(View.GONE);
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
        private TextView rules_spinner;


        private TextView tv_days, tv_hour, tv_minute, tv_second,rules;



        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            event_enter_btn =mView.findViewById(R.id.event_btn_enter);
            event_explore_btn =mView.findViewById(R.id.event_btn_explore);
            cardview=mView.findViewById(R.id.cardView);
            rules_spinner =mView.findViewById(R.id.rules_spinner);
            rules =mView.findViewById(R.id.Rules);
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

        cardview.setVisibility(View.GONE);
        }

        public void setRules(ArrayList<String> rules) {
        StringBuilder builder  =new StringBuilder();
            rules_spinner.setLines(rules.size());
        for(String i: rules){
            builder.append("=>"+i+"\n");
        }
       rules_spinner.setText(builder);
        }

        private void updatedata(String  eventId) {
            DocumentReference docref=  firebaseFirestore.collection("Events").document( eventId);
            docref.update("over",true).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}

