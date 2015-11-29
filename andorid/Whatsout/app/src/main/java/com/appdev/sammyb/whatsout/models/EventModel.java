package com.appdev.sammyb.whatsout.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EventModel implements Serializable {

    public String id;
    public String title;
    public String description;
    public String date;
    public String gid;
    public DateTime sdate;
    public DateTime edate;
    public String location;

    //private static final long serialVersionUID = -111312112;
    private static ArrayList<EventModel>  Events = new ArrayList<EventModel>();

    public EventModel(JSONObject object) {


        try {
            this.id = object.getString("_id");
            this.title = object.getString("title");
            this.description = object.getString("description");
            this.gid = object.has("gid") ? object.getString("gid") : null;
            this.location = object.has("location") ? object.getString("location") : "";
            this.sdate = object.has("date") ? new DateTime(object.getString("date")) : new DateTime();
            this.edate = object.has("dateEnd") ? new DateTime(object.getString("dateEnd")) : new DateTime();


            DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                    .appendMonthOfYearShortText()
                    .appendLiteral(' ')
                    .appendDayOfMonth(1)
                    .appendLiteral(',')
                    .appendLiteral(' ')
                    .appendYearOfCentury(2,4)
                    .appendLiteral(' ')
                    .appendHourOfDay(2)
                    .appendLiteral(':')
                    .appendMinuteOfHour(2)
                    .toFormatter();

            DateTime sdate = new DateTime(object.getString("date"));
            DateTime edate = new DateTime(object.getString("dateEnd"));

            this.date =  sdate.toString(fmt) + " - " + edate.toString(fmt);

        } catch (JSONException e) {
            e.printStackTrace();;
        }
    }

    public static ArrayList<EventModel> fromJson ( JSONArray jsonObjects) {
        ArrayList<EventModel>  events = new ArrayList<EventModel>();

        for (int i= 0; i < jsonObjects.length(); i++) {
            try {
                events.add(new EventModel(jsonObjects.getJSONObject(i)));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Events.clear();
        Events = events;
        return events;
    }

    public static EventModel getEvent(JSONObject jsonObject){
        return new EventModel(jsonObject);
    }

    public static ArrayList<EventModel> getEvents(){
        return Events;
    }

    public static void addEvent(JSONObject jsonObject){
        Events.add(new EventModel(jsonObject));
    }

}

