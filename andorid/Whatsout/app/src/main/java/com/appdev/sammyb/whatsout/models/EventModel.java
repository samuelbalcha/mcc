package com.appdev.sammyb.whatsout.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EventModel implements Serializable {

    public String id;
    public String title;
    public String description;
    public String date;
    //private static final long serialVersionUID = -111312112;
    private static ArrayList<EventModel>  Events = new ArrayList<EventModel>();

    public EventModel(JSONObject object) {

        try {
            this.id = object.getString("_id");
            this.title = object.getString("title");
            this.description = object.getString("description");
           // this.date = object.getString("date") + " - " + object.getString("dateEnd");

            DateFormat format = new SimpleDateFormat("MMMM dd, yyyy  hh:mm aaa", Locale.ENGLISH);
            try {
                this.date = format.parse(object.getString("date")).toString() + " - "
                        + format.parse(object.getString("dateEnd")).toString();

            } catch (ParseException e) {
                e.printStackTrace();
            }

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

