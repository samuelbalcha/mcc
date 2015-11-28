package com.example.lulit.whatsout.models;

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

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


public class EventModel implements Serializable {

    public String id;
    public String title;
    public String description;
    public String date;
    //private static final long serialVersionUID = -111312112;



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

            }catch (ParseException e) {
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
        return events;
    }

}

/*

@Table(name = "events")
public class EventModel extends Model {

    @Column(name="id")
    private String id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;


    public EventModel(JSONObject object) {
        //super();
        try {
            this.id = object.getString("_id");
            this.title = object.getString("title");

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
        return events;
    }

    public String getTitle() {
        return id;
    }

    public static List<EventModel> byId(String id) {
        return new Select().from(EventModel.class).orderBy("id DESC").limit("100").execute();
    }

}*/
