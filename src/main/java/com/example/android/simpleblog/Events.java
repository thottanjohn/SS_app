package com.example.android.simpleblog;

import java.util.ArrayList;
import java.util.Date;

public class Events extends EventId{
    public String event_name,event_image;
    private Date start_date,end_date;
    private int contestant_count;
    private ArrayList<String> Rules ;
    private boolean over;
    public Events() {}


    public Events(String event_name) {
        this.event_name = event_name;
    }


    public ArrayList<String> getRules() {
        return Rules;
    }

    public void setRules(ArrayList<String> rules) {
        Rules = rules;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public Events(String event_name, String event_image, Date start_date, Date end_date, int contestant_count, ArrayList<String> Rules, boolean over){
        this.event_name=event_name;
        this.event_image=event_image;
        this.start_date=start_date;
        this.end_date=end_date;
        this.Rules=Rules;

        this.contestant_count=contestant_count;


    }
    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public int getContestant_count() {
        return contestant_count;
    }

    public void setContestant_count(int contestant_count) {
        this.contestant_count = contestant_count;
    }
/*String dateStart = "11/03/14 09:29:58";
    String dateStop = "11/03/14 09:33:43";

    Custom date format
    SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

    Date d1 = null;
    Date d2 = null;
    try {
        d1 = format.parse(dateStart);
        d2 = format.parse(dateStop);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    // Get msec from each, and subtract.
    long diff = d2.getTime() - d1.getTime();

    */
}
