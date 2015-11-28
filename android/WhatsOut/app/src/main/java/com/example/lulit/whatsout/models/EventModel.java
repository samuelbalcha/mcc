package com.example.lulit.whatsout.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
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
            System.out.println(sdate.toString(fmt));

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
